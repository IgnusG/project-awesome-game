package Alpha;

import java.io.*;
import java.util.Properties;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.TerminalFacade.*;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.*;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import java.util.Hashtable;

/*
 *  Autor: Jonatan Juhas - Informatik WS14
 *  Projekt: 
 */

interface MenuItem {
    boolean run();
}

class Menu {
    private Hashtable<Key, MenuItem> items;
    private Terminal terminal;
    private MenuItem allways;
    
    public Menu(Hashtable items, Terminal terminal){
        this(items,terminal,null);
    }
    public Menu(Hashtable items, Terminal terminal, MenuItem allways){
        this.items = items;
        this.terminal = terminal;
        this.allways = allways;
    }
    
    public void activate(){
        Key key;
        
        while(true) {
            key = terminal.readInput();
            if(key != null){
                if(!items.get(key).run())
                    break;
                allways.run();
            }
            
            try {
                Thread.sleep(50);
            } catch (Exception error){}
        }
    }
    
    
}

class World {
    public final int[][] map;

    public World(Properties worldDefinition) {

        int mapHeight = Integer.parseInt(worldDefinition.getProperty("Height"));
        int mapWidth = Integer.parseInt(worldDefinition.getProperty("Width"));

        map = new int[mapHeight][mapWidth];

        // Populate World map with values
        for(int i = 0; i < mapHeight; i++){
            for(int k = 0; k < mapWidth; k++){
                // If the parsing fails the value is undefined, therefor we replace it with Air
                try{
                    int predefinedObject = Integer.parseInt(worldDefinition.getProperty(k+","+i));
                    map[i][k] = predefinedObject;
                } catch (NumberFormatException error){
                    map[i][k] = -1;
                }

            }
        }
    }
}

public class TestingCameraMovement {
    
    public static Properties readProperties(String file){
        try{
            FileInputStream in = new FileInputStream(file+".properties");
            Properties props = new Properties();
            
            props.load(in);
            
            return props;
        } catch (FileNotFoundException error){
            System.err.println("File could not be located");
        } catch (IOException error){
            System.err.println("IO Exception occured");
        }
        
        return null;
    }
    
    public static char transcribeStream(int type){
        switch(type){
            case -1: return ' ';
                // Wall
            case 0: return 'X';
                // Start
            case 1: return 'S';
                // Exit
            case 2: return 'O';
                // Static Enemy
            case 3: return 'E';
                // Dynamic Enemy
            case 4: return 'E';
                // Key
            case 5: return 'K';
                // Undefined
            default: return 'U';
        }
    }
    
    public static void writeToTerminal(Terminal terminal, String s){
        for(int i = 0; i < s.length(); i++)
            terminal.putCharacter(s.charAt(i));
    }
    
    public static void clearLine(Terminal terminal, int row){
        TerminalSize size = terminal.getTerminalSize();
        
        for(int i = 0; i < size.getColumns(); i++){
            terminal.moveCursor(i, row);
            terminal.putCharacter(' ');
        }
    }
    
    public static void renderScreen(Terminal terminal, World world, int[] offset){
        clearLine(terminal, 0);
        TerminalSize size = terminal.getTerminalSize();
        int maxHeight = size.getRows();
        int maxWidth = size.getColumns();
        
        terminal.moveCursor(0, 0);
        writeToTerminal(terminal,"Testing Camera Movement; Current Offset: X="+offset[0]+" Y="+offset[1]+"; Level Size: X="+world.map.length+" Y="+world.map[0].length);
        
        for(int y = 1; y < maxHeight; y++){
            for(int x = 0; x < maxWidth; x++){
                terminal.moveCursor(x,y);
                if((y-1)+offset[0]>=world.map.length||x+offset[1]>=world.map[0].length)
                    terminal.putCharacter(' ');
                else {
                    int type = world.map[(y-1)+offset[0]][x+offset[1]];
                    if(type > 0)
                        terminal.applyForegroundColor(Terminal.Color.RED);
                    terminal.putCharacter(transcribeStream(type));
                }
                terminal.applyForegroundColor(Terminal.Color.WHITE);
            }
        }
    }
    
    public static void showRectangle() {
    
    }
    
    public static void main(String[] args) {
        World level = new World(readProperties("extreme"));
        
        Terminal terminal = TerminalFacade.createSwingTerminal();
        
        int[] offset = {0,0};
        
        terminal.enterPrivateMode();
        terminal.setCursorVisible(false);
        renderScreen(terminal, level, offset);
        
        TerminalSize size = terminal.getTerminalSize();
        
        Hashtable<Key,MenuItem> hashes = new Hashtable<>();
        
        hashes.put(new Key(Key.Kind.ArrowDown), new whenDown(offset,(level.map.length - size.getRows())-1));
        hashes.put(new Key(Key.Kind.ArrowUp), new whenUp(offset));
        hashes.put(new Key(Key.Kind.ArrowLeft), new whenLeft(offset));
        hashes.put(new Key(Key.Kind.ArrowRight), new whenRight(offset,(level.map[0].length - size.getColumns())-1));
        hashes.put(new Key(Key.Kind.Escape), new whenEscape());
        
        Menu move = new Menu(hashes,terminal, new Allways(terminal, level, offset));
        
        
        move.activate();
        
        terminal.exitPrivateMode();
    }
    
    static class whenRight implements MenuItem {

        int[] offset;
        int max;
        
        public whenRight(int[] offset, int max) {
            this.offset = offset;
            this.max = max;
        }

        @Override
        public boolean run() {
            if(offset[1]<max)
                offset[1]++;
            return true;
        }
    }
    static class whenLeft implements MenuItem {

        int[] offset;
        
        public whenLeft(int[] offset) {
            this.offset = offset;
        }

        @Override
        public boolean run() {
            if(offset[1]>0)
                offset[1]--;
            return true;
        }
    }
    static class whenUp implements MenuItem {

        int[] offset;
        
        public whenUp(int[] offset) {
            this.offset = offset;
        }

        @Override
        public boolean run() {
            if(offset[0]>0)
                offset[0]--;
            return true;
        }
    }
    static class whenDown implements MenuItem {

        int[] offset;
        int max;
        
        public whenDown(int[] offset, int max) {
            this.offset = offset;
            this.max = max;
        }

        @Override
        public boolean run() {
            if(offset[0]<max)
                offset[0]++;
            return true;
        }
    }
    static class whenEscape implements MenuItem {
        @Override
        public boolean run() {
            return false;
        }
    }
    static class Allways implements MenuItem {

        Terminal terminal;
        World level;
        int[] offset;
        
        public Allways(Terminal terminal, World level, int[] offset) {
            this.terminal = terminal;
            this.level = level;
            this.offset = offset;
        }
        
        @Override
        public boolean run() {
            renderScreen(terminal, level, offset);
            return true;
        }
    }
}
