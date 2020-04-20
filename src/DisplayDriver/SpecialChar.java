/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: projectawesomegame
 */

package DisplayDriver;

import com.googlecode.lanterna.terminal.Terminal;
import java.util.Objects;
// Basically stores the colors plus the character
public class SpecialChar implements Comparable {
    public final Terminal.Color foregroundColor;
    public final Terminal.Color backgroundColor;
    public final char display;

    public SpecialChar(char display, Terminal.Color foregroundColor, Terminal.Color backgroundColor) {
        
        assert foregroundColor != null && backgroundColor != null;
        
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.display = display;
    }

    public SpecialChar(char display) {
        this.display = display;
        foregroundColor = Terminal.Color.DEFAULT;
        backgroundColor = Terminal.Color.DEFAULT;
    }
    public SpecialChar(SpecialChar c) {
        display = c.display;
        foregroundColor = c.foregroundColor;
        backgroundColor = c.backgroundColor;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof SpecialChar){
            SpecialChar c = (SpecialChar) o;
            if((c.display == display)&&(c.foregroundColor == foregroundColor)&&(c.backgroundColor == backgroundColor))
                return 0;
            else
                return 1;
        }
        return -1;
    }
    @Override
    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.foregroundColor);
        hash = 59 * hash + Objects.hashCode(this.backgroundColor);
        hash = 59 * hash + this.display;
        return hash;
    }
}
