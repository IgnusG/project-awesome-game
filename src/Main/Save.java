/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: projectawesomegame
 */

package Main;

import DisplayDriver.DialogueBox;
import DisplayDriver.ScreenBuffer;
import DisplayDriver.AdvancedTerminal;
import ObjectLibrary.Exceptions.OutOfSpaceException;
import ObjectLibrary.Interfaces.Executable;
import ObjectLibrary.World;
import com.googlecode.lanterna.input.Key;

public class Save implements Executable {

    private final World world;
    private final AdvancedTerminal terminal;
    
    public Save(World world, AdvancedTerminal terminal){
        this.world = world;
        this.terminal = terminal;
    }
    
    @Override
    public byte execute(ScreenBuffer buffer) {
        World.saveGame(world);
        DialogueBox box = new DialogueBox("","Spiel gespeichert...",new Key(Key.Kind.Enter));
        box.attach(terminal);
        try {
            box.render();
        } catch (OutOfSpaceException ex) {}
        return 0;
    }

    @Override
    public String identify() {
        return "Spiel Speichern";
    }

}
