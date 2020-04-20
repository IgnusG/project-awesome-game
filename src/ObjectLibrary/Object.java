/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary;

import ObjectLibrary.Interfaces.canRender;
import ObjectLibrary.Objects.Wall;
import ObjectLibrary.Objects.Entry;
import ObjectLibrary.Objects.Exit;
import ObjectLibrary.Objects.Enemy;
import ObjectLibrary.Objects.Key;
import ObjectLibrary.Objects.Trap;
import com.googlecode.lanterna.terminal.Terminal;
import DisplayDriver.SpecialChar;
// A holder Class for all Objects in the Game
public abstract class Object implements canRender {
    
    private final Objects type;
    private SpecialChar style = new SpecialChar('#');
    
    // Contains all possible objects
    // All subclasses must call super(Object Type)
    public enum Objects {
        SPACE, WALL, PLAYER, ENEMY, TRAP, KEY, EXIT, ENTRY
    }
    // Constructor
    public Object(Objects type){
        this.type = type;
    }
    
    // Checks if the current object is the same type as the parameter object
    @Override
    public boolean is(Objects type){
        return this.type == type;
    }
    @Override
    public Objects is(){
        return type;
    }
    
    // Overrides canRender interface
    // Changes the style of this object (meaning its char representation and back/fore-ground colors)
    @Override
    public void applyStyle(char style){
        this.style = new SpecialChar(style);
    }
    @Override
    public void applyStyle(char style, Terminal.Color foreground, Terminal.Color background){
        this.style = new SpecialChar(style, foreground, background);
    }
    
    @Override
    public SpecialChar display(){
        return style;
    }

    public static Item createItem(int type) {
        switch(type){
            case 0: return new Wall();
            case 1: return new Entry();
            case 2: return new Exit();
            case 3: return new Trap();
            case 5: return new Key();
            default: return null;
        }
    }
    public int instanceOf(){
        switch(is()){
            case WALL: return 0;
            case ENTRY: return 1;
            case EXIT: return 2;
            case TRAP: return 3;
            case KEY: return 5;
            default: return -1;
        }
    }
    public static Entity createEntity(int type, int positionX, int positionY, World world){
        switch(type){
            case 4: return new Enemy(positionX, positionY, world);
            default: return null;
        }
    }
}
