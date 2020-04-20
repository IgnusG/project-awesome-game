/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: projectawesomegame
 */

package Main;

import DisplayDriver.DialogueBox;
import DisplayDriver.ScreenBuffer;
import DisplayDriver.AdvancedTerminal;
import ObjectLibrary.Entity;
import ObjectLibrary.Interfaces.Executable;
import ObjectLibrary.Exceptions.InvalidFileException;
import ObjectLibrary.Exceptions.InvalidSaveException;
import ObjectLibrary.Objects.Player;
import ObjectLibrary.Exceptions.OutOfSpaceException;
import ObjectLibrary.World;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

public class Game implements Executable {
    
    private final World world;
    private final AdvancedTerminal terminal;
    
    // Offset to the map - scrolling
    private int offsetX = 0;
    private int offsetY = 0;
    // Absolute offsets - when the map is smaller than window
    private int absoluteOffsetX = 0;
    private int absoluteOffsetY = 1;
    
    @Override
    public byte execute(ScreenBuffer buffer) {
        terminal.clear();
        resume();
        while(true){
            render();
            if(!listenToInput()){
                System.out.println("Menu Call ended function");
                return 1;
            }
            if (checkForEndGame()){
                System.out.println("Game completed");
                return 0;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {}
        }
    }
    
    private void renderStats(){
        Player player = world.getPlayer();
        terminal.moveCursor(0, 0);
        terminal.write("Spieler "+player.getName()+" - "+world.name+"    ");
        terminal.applyForegroundColor(Terminal.Color.BLUE);
        terminal.write("Score: "+player.getScore()+" ");
        terminal.applyForegroundColor(Terminal.Color.RED);
        terminal.write("Leben: "+player.getLives()+" ");
        terminal.applyForegroundColor(Terminal.Color.YELLOW);
        terminal.write("Schlüssel: "+player.getAcquiredKeys()+"/"+world.getKeyCount());
        terminal.applyForegroundColor(Terminal.Color.DEFAULT);
        terminal.write("              ");
    }

    private void render(){
        terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
        terminal.applyForegroundColor(Terminal.Color.DEFAULT);
        
        renderStats();
        
        int objectAtX;
        int objectAtY = 0;
        
        for (int k = absoluteOffsetY; k < terminal.height; k++) {
for_loop:   for (int i = absoluteOffsetX; i < terminal.width; i++) {
                
                objectAtX = i - absoluteOffsetX + offsetX;
                objectAtY = k - absoluteOffsetY + offsetY;
                
                if(objectAtX >= world.width || objectAtY >= world.height){
                    if(terminal.getCharAt(i,k) != ' '){
                        terminal.moveCursor(i, k);
                        terminal.putCharacter(' ');
                    }
                    continue;
                }
                
                for(Entity entity : world.dynamicObjectList){
                    if(entity.getPositionX()==objectAtX && entity.getPositionY() == objectAtY){
                        if (!(terminal.getSpecialCharAt(i, k) == entity.display())){
                            terminal.moveCursor(i, k);
                            terminal.putCharacter(entity.display());
                        }
                        continue for_loop;
                    }
                }
                
                if(!terminal.getSpecialCharAt(i, k).equals(world.map[objectAtX][objectAtY].display())){
                    terminal.moveCursor(i, k);
                    terminal.putCharacter(world.map[objectAtX][objectAtY].display());
                }
            }
        }
    }
    
    public boolean listenToInput(){
        try {
            Player player = world.getPlayer();
            Key key =  terminal.readInput();
            if(key != null){
                switch(key.getKind()){
                    case Escape:
                        // Pausiert das Spiel
                        pause();
                        // Diese Array enthält Klassen die Executable implementieren
                        // Sie werden 'gestartet' wenn sie gewählt werden 
                        ArrayList<Executable> menuArray = new ArrayList();
                        menuArray.add(new Help(terminal));
                        menuArray.add(new Save(world,terminal));
                        menuArray.add(new BackToMenu(terminal));
                        menuArray.add(new Quit(terminal));
                        DialogueBox inGameMenu = new DialogueBox("Menu",menuArray);
                        inGameMenu.attach(terminal);
                        inGameMenu.render();
                        if(!inGameMenu.run()){
                            return false;
                        };
                        // Startet oder fortsetzt das Spiel
                        resume();
                        break;
                    case ArrowUp: player.moveUp(); 
                        if(absoluteOffsetY == 1)
                            if(player.getPositionY() - offsetY < 10 && offsetY > 0)
                                offsetY--;
                        break;
                    case ArrowRight: player.moveRight(); 
                        if(absoluteOffsetX == 0)
                            if(player.getPositionX() - offsetX > terminal.width - 10 && offsetX < world.width)
                                offsetX++;
                        break;
                    case ArrowDown: player.moveDown(); 
                        if(absoluteOffsetY == 1)
                            if(player.getPositionY() - offsetY > terminal.height - 10 && offsetY < world.height)
                                offsetY++;
                        break;
                    case ArrowLeft: player.moveLeft(); 
                        if(absoluteOffsetX == 0)
                            if(player.getPositionX() - offsetX < 10 && offsetX > 0)
                                offsetX--;
                        break;
                }
                world.setRenderingStart(offsetX, offsetY);
                world.setRenderingEnd(terminal.width + offsetX, terminal.height + offsetY);
            }
        } catch (OutOfSpaceException ex) {}
        return true;
    }
    
    @Override
    public String identify() {
        return "Spiel starten";
    }
    
    public enum GameType {
        easy, moderate, extreme
    }
    
    public void resume(){
        for(Entity entity : world.dynamicObjectList){
            entity.createThread();
            entity.getThread().start();
        }
    }
    public void pause(){
        for(Entity entity : world.dynamicObjectList){
            entity.getThread().interrupt();
            try {
                entity.getThread().join();
            } catch (InterruptedException ex) {}
        }
    }
    private boolean checkForEndGame(){
        if(world.halted()){
            System.out.println("This state is for DEV only");
            assert false;
        }
        if(world.getPlayer().hasDied()){
            pause();
            try {
                DialogueBox box = new DialogueBox("VERLOREN!","Ich zitiere: 'That escalated quickly'",new Key(Key.Kind.Enter));
                box.attach(terminal);
                box.render();
                // Creates a new Highscore board
                Highscore high = new Highscore(world.getPlayer().getName(),world.getPlayer().getScore(),terminal);
            } catch (OutOfSpaceException ex) {
                assert false;
            }
            return true;
        }
        if(world.getPlayer().hasWon()){
            pause();
            DialogueBox keepPlaying = new DialogueBox("Gewonnen!", "Weiterspielen? J/N",new Key('j'),new Key('n'));
            keepPlaying.attach(terminal);
            try {
                // Generates a new parameters.txt file and starts a new game
                if(keepPlaying.render()){
                    Generate gen = new Generate();
                    try {
                        Game game;
                        Properties props = gen.generate();
                        props.setProperty("type", world.type);
                        game = new Game(props,world.getPlayer().getName(),world.level+1,world.getPlayer().getScore(),terminal);
                        game.execute(terminal.requestBuffer());
                    } catch (InvalidFileException ex) {assert false;}
                } else {
                    Highscore high = new Highscore(world.getPlayer().getName(),world.getPlayer().getScore(),terminal);
                }
            } catch (OutOfSpaceException ex) {}
            return true;
        }
        
        return false;
    }
    
    private void initTerminalVariables(){
        if(world.width > terminal.width){
            offsetX = world.getPlayer().getPositionX() - (int) Math.round(terminal.width / 2.);
            offsetX = (offsetX < 0) ? 0 : offsetX;
        }
        if(world.height > terminal.height){
            offsetY = world.getPlayer().getPositionY() - (int) Math.round(terminal.height / 2.);
            offsetY = (offsetY < 0) ? 0 : offsetY;
        }

        world.setRenderingStart(offsetX, offsetY);
        world.setRenderingEnd(terminal.width + offsetX, terminal.height + offsetY);

        if (terminal.width > world.width) {
            absoluteOffsetX = (terminal.width - world.width) / 2;
        }
        if (terminal.height > world.height) {
            absoluteOffsetY = (terminal.height - world.height) / 2;
        }
    }
    
    public Game(File file, AdvancedTerminal terminal) throws InvalidSaveException {
        
        world = new World(World.readProperties(file));
        
        this.terminal = terminal;
        initTerminalVariables();
    }
    
    public Game(Properties props, String playername, int level, AdvancedTerminal terminal) throws InvalidFileException {
        
        world = new World(props, playername, level);
        
        this.terminal = terminal;
        initTerminalVariables();
    }
    public Game(Properties props, String playername,int level, int score,  AdvancedTerminal terminal) throws InvalidFileException {
        
        world = new World(props, playername, level);
        world.getPlayer().activateScore(score);
        
        this.terminal = terminal;
        initTerminalVariables();
    }
}
