/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary.Objects;

import ObjectLibrary.Item;
import ObjectLibrary.Interfaces.canRender;
import com.googlecode.lanterna.terminal.Terminal.Color;
import DisplayDriver.SpecialChar;

public class Trap extends Item implements canRender {
    
    private static SpecialChar style = new SpecialChar('\u25BC',Color.RED,Color.DEFAULT);
    
    public static SpecialChar showOf() {
        return style;
    }
    
    public Trap(){
        super(Objects.TRAP);
    }
    
    @Override
    public String toString(){
        return "Trap";
    }
    
    @Override
    public SpecialChar display(){
        return style;
    }
}
