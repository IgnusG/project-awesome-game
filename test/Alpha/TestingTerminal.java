package Alpha;

/*
 *  Autor: Jonatan Juhas - Informatik WS14
 *  Projekt: 
 */



import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.*;

public class TestingTerminal {
    
    public static void outString(Terminal terminal, String s){
        for(int i = 0; i < s.length(); i++){
            terminal.putCharacter(s.charAt(i));
        }
    }
    
    // Testing Method for calculation of the middle
    public static int[] calculateCenter(TerminalSize size, String s){
        int posX = (size.getColumns() - s.length()) / 2;
        int posY = (size.getRows()-1)/2;
        
        System.out.println("Calculating center:\nMax X: "+size.getColumns()+" Units   Max Y: "+size.getRows()+" Units");
        System.out.println("Word "+s+" ("+s.length()+") has to be positioned at\nCenter X: "+posX+" Units   Center Y: "+posY+" Units");
        
        int[] m = {posX,posY};
        return m;
    }
    
    public static void main(String[] args) {
        Terminal terminal = TerminalFacade.createSwingTerminal();
        TerminalSize size = terminal.getTerminalSize();
        
        terminal.enterPrivateMode();
        terminal.setCursorVisible(false);
        
        String hello = "Hello World!";
        
        int[] center = calculateCenter(size,hello);
        
        terminal.moveCursor(center[0],center[1]);
        terminal.applyBackgroundColor(Terminal.Color.RED);
        terminal.applyForegroundColor(Terminal.Color.WHITE);
        
        outString(terminal, hello);
        
        Key key;
        
        while(true){
            key = terminal.readInput();
            if(key != null){
                if(key.getKind()==Key.Kind.Escape)
                    break;
                else {
                    if(key.getKind()==Key.Kind.NormalKey){
                        center = calculateCenter(size, "That's an "+key.getCharacter()+"      ");
                        terminal.moveCursor(center[0], center[1]+1);
                        outString(terminal,"   "+"That's an "+key.getCharacter()+"   ");
                    } else {
                        center = calculateCenter(size, "That's a Special");
                        terminal.moveCursor(center[0], center[1]+1);
                        outString(terminal,"That's a Special");
                    }
                }
                    
            }
            
            try{
                Thread.sleep(100);
            } catch (InterruptedException error){
                System.err.println("Interrupted!");
            }
        }
        
        terminal.exitPrivateMode();
    }
}
