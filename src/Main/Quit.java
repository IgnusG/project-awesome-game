/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: projectawesomegame
 */

package Main;

import DisplayDriver.DialogueBox;
import DisplayDriver.ScreenBuffer;
import DisplayDriver.AdvancedTerminal;
import ObjectLibrary.Interfaces.Executable;
import ObjectLibrary.Exceptions.OutOfSpaceException;

public class Quit implements Executable {

    private final AdvancedTerminal terminal;
    
    public Quit(AdvancedTerminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public byte execute(ScreenBuffer buffer) {
        terminal.clearScreen();
        try {
            DialogueBox quit = new DialogueBox("Spiel Beenden", "Bye!", 2);
            quit.attach(terminal);
            quit.render();
        } catch (OutOfSpaceException err) {}

        terminal.exitPrivateMode();
        System.exit(1);
        
        return 0;
    }

    @Override
    public String identify() {
        return "Spiel Beenden";
    }
    
}
