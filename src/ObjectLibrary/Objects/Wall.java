/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary.Objects;

import ObjectLibrary.Item;

public class Wall extends Item {
    
    public Wall(){
        super(Objects.WALL);
    }
    
    @Override
    public String toString(){
        return "Wall";
    }
}
