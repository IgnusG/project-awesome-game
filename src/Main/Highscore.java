/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Main
 */

package Main;

import DisplayDriver.AdvancedTerminal;
import DisplayDriver.DialogueBox;
import DisplayDriver.ScreenBuffer;
import ObjectLibrary.Exceptions.OutOfSpaceException;
import ObjectLibrary.Interfaces.Executable;
import com.googlecode.lanterna.input.Key;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
// Shows Highscore leaderboards
public class Highscore implements Executable {
    
    private AdvancedTerminal terminal;
    
    public Highscore(AdvancedTerminal terminal){
        this.terminal = terminal;
    }
    public Highscore(String name, int score, AdvancedTerminal terminal) {
        this.terminal = terminal;
        
        ArrayList<ArrayList> array = createArray(loadHighscores());
        addYourself(name, score, array);
        render(array);
        saveHighscores(array);
            
    }
    
    private void render(ArrayList<ArrayList> scores){
        String out = "";

        for (ArrayList list : scores) {
            out += list.get(0) + " : " + list.get(1) + " Punkte\n";
        }

        out += "\n[Enter]";

        try {
            DialogueBox box = new DialogueBox("Leaderboards", out, new Key(Key.Kind.Enter));
            box.attach(terminal);
            box.render();
        } catch (OutOfSpaceException ex) {
        }

    }
    
    private void saveHighscores(ArrayList<ArrayList> array){
        int counter = 1;
        Properties props = new Properties();
        for (ArrayList list : array) {
            FileOutputStream outStream = null;
            try {
                props.setProperty("Position-" + counter, list.get(0) + ":" + list.get(1));
                outStream = new FileOutputStream("Highscore.txt");
                props.store(outStream, "Glorious Highscores!!!");
            } catch (FileNotFoundException ex) {} catch (IOException ex) {
            } finally {
                try {
                    outStream.close();
                } catch (IOException ex) {}
            }
        }
    }
    
    private Properties loadHighscores(){
        try {
            File file = new File("Highscore.txt");
            ArrayList<ArrayList> array = new ArrayList<>();

            if (!file.exists()) {
                file.createNewFile();
            }

            FileInputStream in = new FileInputStream(file);
            Properties props = new Properties();
            props.load(in);
            return props;
        } catch (IOException ex) {return null;}
    }
    
    private void addYourself(String name, int score, ArrayList<ArrayList> array){
        boolean added = false;
        for(int i = 0; i < array.size(); i++){
            if(score > (int) array.get(i).get(1)){
                array.add(new ArrayList());
                for(int k = array.size()-1; k > i; k--){
                    array.set(k, array.get(k-1));
                }
                ArrayList current = new ArrayList();
                current.add(name);
                current.add(score);
                array.set(i,current);
                added = true;
                break;
            }
        }
        if(!added){
            ArrayList current = new ArrayList();
            current.add(name);
            current.add(score);
            array.add(current);
        }
    }
    
    private ArrayList<ArrayList> createArray(Properties props){
        ArrayList<ArrayList> array = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ArrayList arrayUser = new ArrayList();
            if (props.containsKey("Position-" + (i + 1))) {
                String[] s = props.getProperty("Position-" + (i + 1)).split("\\:");
                arrayUser.add(s[0]);

                try {
                    arrayUser.add(Integer.parseInt(s[1]));
                } catch (NumberFormatException err) {
                    arrayUser.add(0);
                }
            } else {
                break;
            }
            
            array.add(arrayUser);
        }
        return array;
    }

    @Override
    public byte execute(ScreenBuffer buffer) {
        ArrayList<ArrayList> array = createArray(loadHighscores());
        render(array);
        saveHighscores(array);
        return 0;
    }

    @Override
    public String identify() {
        return "Leaderboards";
    }
}
