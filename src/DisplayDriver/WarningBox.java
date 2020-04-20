/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: DisplayDriver
 */

package DisplayDriver;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal.Color;
// Warning message - not used often
public class WarningBox extends DialogueBox {
    public WarningBox(String s){
        super("Warnung",s);
        selectBackgroundColor(Color.RED);
    }
    public WarningBox(String s, Key one, Key two){
        super("Warnung",s,one,two);
        selectBackgroundColor(Color.RED);
    }
}
