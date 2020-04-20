/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary.Interfaces;

import ObjectLibrary.Object;
import com.googlecode.lanterna.terminal.Terminal;
import DisplayDriver.SpecialChar;

public interface canRender {
    public void applyStyle(char style);
    public void applyStyle(char style, Terminal.Color foreground, Terminal.Color background);
    public SpecialChar display();
    public boolean is(Object.Objects type);
    public Object.Objects is();
}
