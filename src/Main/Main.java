/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: projectawesomegame
 */

package Main;

import DisplayDriver.ScreenBuffer;
import DisplayDriver.AdvancedTerminal;
import ObjectLibrary.Interfaces.Executable;
import ObjectLibrary.Exceptions.InvalidFileException;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal.Color;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import javax.swing.JFrame;

public class Main {
    
    public static AdvancedTerminal terminal;
    public static int offsetX;
    public static int offsetY;
    public static Executable[] menuItems;
    public static int selection;
    
    public static void quitGame(){
        // ...
        System.exit(1);
    }
    
    public static void printLogo(int positionX, int positionY){
        String firstRow =  "  ___  _    _ ________  ___     _____   ___  ___  ___ _____ ";
        String secondRow = " / _ \\| |  | /  ___|  \\/  |    |  __ \\ / _ \\ |  \\/  ||  ___|";
        String thirdRow =  "/ /_\\ \\ |  | \\ `--.| .  . |    | |  \\// /_\\ \\| .  . || |__  ";
        String fourthRow = "|  _  | |/\\| |`--. \\ |\\/| |    | | __ |  _  || |\\/| ||  __| ";
        String fifthRow =  "| | | \\  /\\  /\\__/ / |  | |    | |_\\ \\| | | || |  | || |___ ";
        String sixthRow =  "\\_| |_/\\/  \\/\\____/\\_|  |_/     \\____/\\_| |_/\\_|  |_/\\____/ ";
        
        String[] array = {firstRow, secondRow, thirdRow, fourthRow, fifthRow, sixthRow};

        for(int k = 0; k < 6; k++){
            terminal.moveCursor(positionX, positionY+k);
            terminal.write(array[k]);
        }
    }
    
    public static void renderMainMenu(){
        
        for(int i = 0; i < menuItems.length; i++){
            terminal.moveCursor(offsetX,offsetY+(i*2));
            if(i == 0)
                terminal.applyForegroundColor(Color.GREEN);
            else if(i == 1)
                terminal.applyForegroundColor(Color.RED);
            else if(i == 2)
                terminal.applyForegroundColor(Color.YELLOW);
            else if(i == 3)
                terminal.applyForegroundColor(Color.CYAN);
            else if(i == 4)
                terminal.applyForegroundColor(Color.MAGENTA);
            if(selection == i)
                terminal.write(">> ");
            else
                terminal.write("   ");
            terminal.write(menuItems[i].identify());
        }
    }
    
    public static void renderTheDesignDataStructure(){
        terminal.clear();
        printLogo(30, 8);

        terminal.moveCursor((terminal.width - 11) / 2, terminal.height - 1);
        terminal.write("Build 201-J");
    }
    
    public static void render(){
        
        offsetY = terminal.height - (menuItems.length*2) - 5;
        
        String longest = "";

        for (Executable item : menuItems) {
            if (longest.length() < item.identify().length()) {
                longest = item.identify();
            }
        }
        
        offsetX = 9;
        
        renderTheDesignDataStructure();
        
        while(true){
            
            renderMainMenu();
            listenToInput();
            
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {}
        }
    }
    
    public static void listenToInput(){
        Key key = terminal.readInput();
        
        if(key != null){
            switch(key.getKind()){
                case ArrowDown: 
                    if(selection < menuItems.length-1)
                        selection++;
                    break;
                case ArrowUp:
                    if(selection > 0)
                        selection--;
                    break;
                case Enter: 
                    ScreenBuffer buffer = terminal.requestBuffer();
                    menuItems[selection].execute(terminal.requestBuffer());
                    terminal.loadBuffer(buffer);
                    break;
                case Escape: quitGame();
                    break;
            }
        }
    }
    
    public static void main(String[] args) {
        
        SwingTerminal terminalFacade = TerminalFacade.createSwingTerminal();
        terminal = new AdvancedTerminal(terminalFacade);
        terminal.enterPrivateMode();
        terminal.setCursorVisible(false);

        JFrame frame = terminal.getJFrame();
        frame.setResizable(false);

        menuItems = new Executable[5];
        menuItems[0] = new RandomGame(terminal);
        menuItems[1] = new Highscore(terminal);
        menuItems[2] = new Help(terminal);
        menuItems[3] = new Load(terminal);
        menuItems[4] = new Quit(terminal);

        render();
            
    }
}
