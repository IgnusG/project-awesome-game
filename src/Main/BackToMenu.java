/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Main
 */

package Main;

import DisplayDriver.AdvancedTerminal;
import DisplayDriver.ScreenBuffer;
import ObjectLibrary.Interfaces.Executable;

// Return to the main menu
public class BackToMenu implements Executable {
    
    private final AdvancedTerminal terminal;

    public BackToMenu(AdvancedTerminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public byte execute(ScreenBuffer buffer) {
        return -1;
    }

    @Override
    public String identify() {
        return "Zurück zum Hauptmenu";
    }

}
