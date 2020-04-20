/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: projectawesomegame
 */

package Main;

import DisplayDriver.ScreenBuffer;
import DisplayDriver.AdvancedTerminal;
import DisplayDriver.WarningBox;
import ObjectLibrary.Exceptions.InvalidSaveException;
import ObjectLibrary.Exceptions.OutOfSpaceException;
import ObjectLibrary.Interfaces.Executable;
import ObjectLibrary.World;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
// Loads a new game
public class Load implements Executable {
    
    private final AdvancedTerminal terminal;
    private ArrayList<String> filenames;
    private ArrayList<File> filelist;
    private int selection;
    private int longest;
    
    ScreenBuffer buffer;
    
    int menuOffset = 0;
    int offsetX = 50;
    int offsetY;
    
    public Load(AdvancedTerminal terminal){
        this.terminal = terminal;
        offsetY = terminal.height - 10;
    }
    
    private String repeat(char c, int i){
        String out = "";
        for(int k = 0; k < i; k++){
            out += c;
        }
        return out;
    }
    
    public void intro(){
        terminal.moveCursor(50, terminal.height - 11);
        terminal.lockAtColumn(50);
        terminal.applyForegroundColor(Terminal.Color.GREEN);
        terminal.write("Laden mit Enter");
        terminal.applyForegroundColor(Terminal.Color.DEFAULT);
        terminal.write(" - ");
        terminal.applyForegroundColor(Terminal.Color.RED);
        terminal.write("Löschen mit Delete");
    }
    
    @Override
    public byte execute(ScreenBuffer buffer) {
        
        filenames = new ArrayList<>();
        filelist = new ArrayList<>();
        selection = 0;
        longest = 0;
        
        this.buffer = buffer;
        
        intro();
        
        int counter = 1;

        while(true){
            File file = new File("AwsmGameSave-"+counter+".properties");
            if(file.exists()){
                filelist.add(file);
                Properties props = World.readProperties(file.getName().split("\\.")[0]);
                filenames.add(props.getProperty("saveinfo"));
            } else if(counter > 999)
                break;
                
            counter++;
        }
        
        while(true){
            try {
                render();
                if(!listenToInput())
                    break;
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                assert false;
            }
        }
        return 0;
    }
    
    public void render(){
        terminal.moveCursor(offsetX, offsetY);
        terminal.lockAtColumn(offsetX);
        
        String longestStr = "";
        
        for(String s : filenames)
            if(s.length() > longestStr.length())
                longestStr = s;
        
        longest = longestStr.length();
        
        for(int i = menuOffset; i < Math.min(menuOffset+5, filenames.size()); i++){
            
            if(selection == i)
                terminal.applyForegroundColor(Terminal.Color.BLUE);
            else
                terminal.applyForegroundColor(Terminal.Color.DEFAULT);
            terminal.writeln(filenames.get(i));
            terminal.applyForegroundColor(Terminal.Color.DEFAULT);
        }
        
        terminal.applyForegroundColor(Terminal.Color.DEFAULT);
        if(filenames.isEmpty())
            terminal.write("Keine Dataein");
        else {
            terminal.write(repeat(' ',(longest-3)/2)+"-|-");
        }
        terminal.unlockColumn();
    }
    
    public boolean listenToInput() {
        Key key = terminal.readInput();

        if (key != null) {
            switch (key.getKind()) {
                case ArrowDown:
                    if (selection < filelist.size() - 1) {
                        selection++;
                        if(selection > menuOffset+5)
                            menuOffset++;
                    }
                    break;
                case ArrowUp:
                    if (selection > 0) {
                        selection--;
                        if(selection < menuOffset)
                            menuOffset--;
                    }
                    break;
                case Delete:
                    filelist.get(selection).delete();
                    filelist.remove(selection);
                    filenames.remove(selection);
                    terminal.loadBuffer(buffer);
                    intro();
                    if(selection > 0)
                        selection--;
                    break;
                case Enter:
                    try {
                        load(filelist.get(selection));
                    } catch (InvalidSaveException ex) {
                        try {
                            WarningBox box = new WarningBox("Datei kann leider nicht gelesen werden");
                            box.attach(terminal);
                            box.render();
                        } catch (OutOfSpaceException ex1) {
                            assert false;
                        }
                    }
                    return false;
                case Escape:
                    return false;
            }
        }
        return true;
    }

    private void load(File file) throws InvalidSaveException {
        Game game;
        game = new Game(file, terminal);
        game.execute(terminal.requestBuffer());
    }
    
    @Override
    public String identify() {
        return "Spiel Laden";
    }

}
