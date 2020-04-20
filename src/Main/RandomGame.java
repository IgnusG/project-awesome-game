/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Main
 */

package Main;

import DisplayDriver.AdvancedTerminal;
import DisplayDriver.DialogueBox;
import DisplayDriver.InputBox;
import DisplayDriver.ScreenBuffer;
import ObjectLibrary.Exceptions.InvalidFileException;
import ObjectLibrary.Exceptions.OutOfSpaceException;
import ObjectLibrary.Interfaces.Executable;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
// Creates a initiates a Random Game based on the difficulty setting
public class RandomGame implements Executable {

    private AdvancedTerminal terminal;
    
    public RandomGame(AdvancedTerminal terminal){
        this.terminal = terminal;
    }
    
    @Override
    public byte execute(ScreenBuffer buffer) {
        
        InputBox box = new InputBox("Player Name: ");
        box.attach(terminal);
        String playerName = box.active();
        
        ArrayList<Executable> array = new ArrayList<>();
        array.add(new Randomize(20));
        array.add(new Randomize(5));
        array.add(new Randomize(2));
        array.add(new Randomize(1));
        
        try {
            DialogueBox diff = new DialogueBox("Schwierigkeit",array);
            diff.attach(terminal);
            diff.render();
            diff.run();
        } catch (OutOfSpaceException ex) {}
        
        String type = "";
        
        try {
            FileInputStream file = new FileInputStream("parameters.txt");
            Properties props = new Properties();
            props.load(file);
            type = props.getProperty("type");
        } catch (IOException ex) {}
        
        
        Generate gen = new Generate();
        Properties props = gen.generate();
        props.setProperty("type", type);
        try {
            Game game;
            game = new Game(props,playerName,1,terminal);
            game.execute(terminal.requestBuffer());
        } catch (InvalidFileException ex) {assert false;}
        return 0;
    }

    @Override
    public String identify() {
        return "Spiel Einfach!";
    }

}
