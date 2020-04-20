/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary;

import java.util.concurrent.atomic.AtomicInteger;
// Takes care of all Stationary Objects
public class Item extends Object {
    private final static AtomicInteger nextId = new AtomicInteger(0);
    public final int id;
    
    public Item(Objects type) {
        super(type);
        id = nextId.get();
        nextId.addAndGet(1);
    }
    
    @Override
    public String toString(){
        return "#"+id;
    }
}
