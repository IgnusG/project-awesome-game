/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary.Objects;

import ObjectLibrary.Interfaces.Door;
import ObjectLibrary.Item;
import DisplayDriver.SpecialChar;
import com.googlecode.lanterna.terminal.Terminal.Color;

public class Exit extends Item implements Door {
    
    private static SpecialChar style = new SpecialChar('\u2666',Color.BLUE,Color.DEFAULT);
    
    public static SpecialChar showOf() {
        return style;
    }
    
    public Exit(){
        super(Objects.EXIT);
    }
    
    @Override
    public String toString() {
        return "Exit";
    }
    
    @Override
    public SpecialChar display() {
        return style;
    }
}
