/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary.Objects;

import ObjectLibrary.Item;
import ObjectLibrary.Interfaces.canRender;
import DisplayDriver.SpecialChar;

public class Entry extends Item implements canRender {

    private static SpecialChar style = new SpecialChar('\u2726');
    
    public static SpecialChar showOf() {
        return style;
    }
    
    public Entry() {
        super(Objects.ENTRY);
    }
    
    @Override
    public String toString(){
        return "Entry";
    }
    
    @Override
    public SpecialChar display(){
        return style;
    }
}
