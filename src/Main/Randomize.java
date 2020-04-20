/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Main
 */

package Main;

import DisplayDriver.ScreenBuffer;
import ObjectLibrary.Interfaces.Executable;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class Randomize implements Executable {
    
    private int difficulty;
    
    public Randomize(int difficulty){
        this.difficulty = difficulty;
    }
    
    @Override
    public byte execute(ScreenBuffer buffer){
        PrintWriter writer = null;
        Random random = new Random();
        try {
            
            int height = (30+random.nextInt(300));
            int width = (30+random.nextInt(300));
            int doors = (2*height+2*width)/100;
            
            writer = new PrintWriter("parameters.txt", "UTF-8");
            writer.println("Width="+width);
            writer.println("Height="+height);
            writer.println("NrIn=1");
            writer.println("NrOut="+doors);
            writer.println("Keys="+Math.max((height*width)/300,1));
            writer.println("StaticTraps="+(height*width)/(80*difficulty));
            writer.println("DynamicTraps="+(height*width)/(120*difficulty));
            writer.println("Density="+(1+random.nextDouble()+random.nextInt(4)));
            
            switch (difficulty) {
                case 1: writer.println("type=EXTREME");
                    break;
                case 2: writer.println("type=HARD");
                    break;
                case 5: writer.println("type=MEDIUM");
                    break;
                case 20: writer.println("type=SIMPLE");
            }
            
            writer.close();
        } catch (FileNotFoundException ex) {
        } catch (UnsupportedEncodingException ex) {
        } finally {
            writer.close();
        }
        return 0;
    }

    @Override
    public String identify() {
        switch(difficulty){
            case 1: return "Super Sayan (EXTREME)";
            case 2: return "Hard as Wood (HARD)";
            case 5: return "Fight! (MEDIUM)";
            case 20: return "Paradise (SIMPLE)";
        }
        return "?";
    }
}
