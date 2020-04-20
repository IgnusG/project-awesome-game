/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary.Objects;

import ObjectLibrary.Item;
import ObjectLibrary.Interfaces.canRender;
import DisplayDriver.SpecialChar;

public class Space extends Item implements canRender {
    
    private static SpecialChar style = new SpecialChar(' ');
    
    public static SpecialChar showOf() {
        return style;
    }
    
    public Space(){
        super(Objects.SPACE);
    }
    
    @Override
    public String toString(){
        return "Space";
    }
    
    @Override
    public SpecialChar display(){
        return style;
    }
}
