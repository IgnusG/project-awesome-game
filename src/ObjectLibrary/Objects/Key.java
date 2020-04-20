/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary.Objects;

import ObjectLibrary.Item;
import ObjectLibrary.Interfaces.canRender;
import com.googlecode.lanterna.terminal.Terminal.Color;
import DisplayDriver.SpecialChar;

public class Key extends Item implements canRender {
    
    private static SpecialChar style = new SpecialChar('\u212A',Color.YELLOW,Color.DEFAULT);
    
    public static SpecialChar showOf() {
        return style;
    }
    
/*
     public Set<Door> doors = new HashSet<>();

     public Key(Door door) {
         doors.add(door);
     }
     public Key(Set<Door> doors){
         this.doors = doors;
     }

     @Override
     public String toString(){
         String result = super.toString()+":";
         for(Door door : doors)
         result += door + ";";
         result = result.substring(result.length()-1);
         return result;
     } */ 
    
    public Key(){
        super(Objects.KEY);
    }
    
    @Override
    public String toString() {
        return "Key";
    }
    
    @Override
    public SpecialChar display() {
        return style;
    }
 }
