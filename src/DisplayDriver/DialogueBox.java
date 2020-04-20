/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: projectawesomegame
 */

package DisplayDriver;

import ObjectLibrary.Interfaces.Executable;
import ObjectLibrary.Exceptions.OutOfSpaceException;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal.Color;
import java.util.ArrayList;

// Used as a DialogueBox, self explenatory
public class DialogueBox {
    
    private enum Position {
        CENTER, LEFT, RIGHT
    }
    
    // Corner characters
    private static char left_top = '\u2554';
    private static char left_bottom = '\u255A';
    private static char right_top = '\u2557';
    private static char right_bottom = '\u255D';
    private static char vertical = '\u2551';
    private static char horizontal = '\u2550';

    private String name;
    
    // Possible background changing
    private Color backgroundColor = Color.BLACK;
    
    public void selectBackgroundColor(Color color){
        backgroundColor = color;
    }

    // Backed-up by an Advanced Terminal and Buffer
    private AdvancedTerminal terminal;
    private ScreenBuffer buffer;

    private int[] startIndex;
    private int[] endIndex;

    private int width;
    private int height;
    
    private int outerWidth;
    private int outerHeight;

    private int activeLine = 0;
    
    // Exit conditions for non-active menus
    private int wait = -1;
    private Key exitKey = null;
    private Key enterKey = null;
    
    private String text = "";
    
    // Active-Menus are Selection Based Menus
    private boolean activeMenu = false;
    private boolean activeInput = false;
    private ArrayList<Executable> elements;
    private int selection;

    // Constructors for Active-Menus
    public DialogueBox(String name, ArrayList<Executable> elements) throws OutOfSpaceException {
        this(name, elements, 0);
    }
    public DialogueBox(String name, ArrayList<Executable> elements, int defaultSelection) throws OutOfSpaceException {
        activeMenu = true;
        
        String longest = name;
        // Searches for the longest world and uses this to calculate the width that is needed
        for(Executable exec : elements){
            String s = exec.identify();
            if(s.length() > longest.length())
                longest = s;
        }
        
        width = longest.length()+4;
        height = (name.equals("")) ? elements.size()+2 : elements.size()+3;
        
        this.name = name;
        this.elements = elements;
        
        if(name.length() > width)
            this.name = "";
    }
    // Standart Constructors
    public DialogueBox(String name, String s, int wait) {
        this(name,s);
        
        this.wait = wait;
    }
    public DialogueBox(String name, String s, Key exitKey) {
        this(name,s);
        
        this.exitKey = exitKey;
    }
    public DialogueBox(String name, String s, Key exitKey, Key enterKey){
        this(name, s);

        this.exitKey = exitKey;
        this.enterKey = enterKey;
    }
    
    public DialogueBox(String s){
        this("",s);
    }
    
    public DialogueBox(String name, String s) {
        this.name = name;
        text = s;
        String[] textArray = text.split("\\n");
        
        // Same principal to calculate the width of the box
        String longest = name;
        for(String longer : textArray)
            if(longer.length() > longest.length())
                longest = longer;
        
        width = longest.length() + 4;
        height = (name.equals("")) ? textArray.length : textArray.length+1;
    }
    
    public void attach(AdvancedTerminal terminal){
        attach(terminal, terminal.requestBuffer());
    }
    // The terminal will get attached to the Box
    public void attach(AdvancedTerminal terminal, ScreenBuffer buffer) {
        this.terminal = terminal;
        
        initSize();
        
        this.buffer = buffer;
    }
    // Outer sizes are sizes with edges
    // Offsets calculate the center of the screen
    // StartIndex defines where the box will start 
    // EndIndex defines where the box will end 
    private void initSize(){
        outerHeight = this.height + 2; // 4 when margin
        outerWidth = this.width + 2;

        int offsetX = (int) Math.round((terminal.width - outerWidth) / 2.);
        int offsetY = (int) Math.round((terminal.height - outerHeight) / 2.);

        startIndex = new int[2];
        startIndex[0] = offsetX + 1; // 2 when margin
        startIndex[1] = offsetY + 1;

        endIndex = new int[2];
        endIndex[0] = startIndex[0] + width;
        endIndex[1] = startIndex[1] + height;
    }
    // Function for accepting user input
    public String enableInput() {
        // We add height for the extra input line
        height++;
        initSize();
        activeInput = true;
        try {
            render();
        } catch (OutOfSpaceException ex) {
            assert false;
        }
        
        nextLine();
        
        String out = "";
        
        // Prepare the cursor
        terminal.moveCursor(startIndex[0] + 2, startIndex[1] + activeLine);
        while(true){
            Key key = terminal.readInput();
            if(key != null){
                switch(key.getKind()){
                    case NormalKey: 
                        if(out.length() < width-4){
                            out+= key.getCharacter();
                            terminal.write(key.getCharacter()+"");
                        }
                        break;
                    case Enter:
                        return out;
                    case Backspace:
                        if(out.length() > 0){
                            out = out.substring(0,out.length()-1);
                            // Moves the cursor to the start and rewrites the whole new word plus blanks for deleting extra overflow
                            terminal.moveCursor(startIndex[0] + 2, startIndex[1] + activeLine);
                            terminal.write(out+repeat(' ',width-out.length()-2));
                            terminal.moveCursor(startIndex[0] + 2 +out.length(), startIndex[1] + activeLine);
                        }
                }
            }
        }
    }
    // Creates the style box around the Dialoguebox
    private void createBoxOutline(){

        // Width -2, our offset because of outline //and margin
        int offsetX = startIndex[0]-1;
        // Height -1, our outline
        int offsetY = startIndex[1]-1;
        
        int nameOffset = (int) Math.round((outerWidth-name.length())/2.);

        terminal.applyBackgroundColor(backgroundColor);
        for (int i = 0; i < outerHeight; i++) {
            for (int k = 0; k < outerWidth; k++) {
                terminal.moveCursor(offsetX + k, offsetY + i);
                if (i == 0 && k == 0) {
                    terminal.putCharacter(left_top);
                } else if (i == 0 && k == outerWidth - 1) {
                    terminal.putCharacter(right_top);
                } else if (i == outerHeight - 1 && k == 0) {
                    terminal.putCharacter(left_bottom);
                } else if (i == outerHeight - 1 && k == outerWidth - 1) {
                    terminal.putCharacter(right_bottom);
                } else if ((i == 0 || i == outerHeight - 1) && (k > 0 && k < outerWidth - 1)) {
                    terminal.putCharacter(horizontal);
                } else if (k == 0 || k == outerWidth - 1) {
                    terminal.putCharacter(vertical);
                } else {
                    terminal.putCharacter(' ');
                }
            }
        }
        terminal.applyBackgroundColor(Color.DEFAULT);
    }
    
    public boolean render() throws OutOfSpaceException {
        terminal.applyBackgroundColor(Color.DEFAULT);
        terminal.applyForegroundColor(Color.DEFAULT);
        createBoxOutline();
        
        if(!name.equals("")){
            write(name);
        }
        // If the menu is not active just write the text and if it's not an input run any Exit keys
        if(!activeMenu){
            write(text);
            if(!activeInput)
                return run();
        } else {
            // Else print out the Menu structure and wait for run to be called on its own
            nextLine();
            for(int i = 0; i < elements.size(); i++){
                if(i == selection)
                    write(elements.get(i).identify(), Position.LEFT, Color.BLUE);
                else
                    write(elements.get(i).identify(), Position.LEFT, Color.DEFAULT);
            }
        }
        activeLine = 0;
        return true;
    }
    // Writes the text inside the box
    private void write(String s) throws OutOfSpaceException {
        write(s, Position.CENTER, Color.DEFAULT);
    }

    private void write(String s, Position position, Color color) throws OutOfSpaceException {
        terminal.applyBackgroundColor(backgroundColor);
        terminal.applyForegroundColor(color);
        if (activeLine == height) {
            throw new OutOfSpaceException();
        }
        
        String[] stringArray = s.split("\\n");
        
        for(String string : stringArray){
            int offset = 1;

            switch(position){
                case CENTER: 
                    if (string.length() < width) {
                        offset = (int) Math.round((width - string.length()) / 2.);
                    }
                    break;
                case LEFT: offset = 2;
                default:
            }

            terminal.moveCursor(startIndex[0] + offset, startIndex[1] + activeLine);
            for (int i = 0; i < string.length(); i++) {
                terminal.putCharacter(string.charAt(i));
            }
            nextLine();
        }
        terminal.applyBackgroundColor(Color.DEFAULT);
        terminal.applyForegroundColor(Color.DEFAULT);
    }
    private void nextLine(){
        activeLine++;
    }
    // Exits the Dialogue and resores previous content below it
    public void exitDialogueBox(){
        terminal.applyBackgroundColor(Color.DEFAULT);
        terminal.applyForegroundColor(Color.DEFAULT);
        terminal.loadBuffer(buffer);
    }

    public boolean run() {
        if(activeMenu){
            try {
                while(true){
                    Key key = terminal.readInput();
                    if(key != null){
                        switch(key.getKind()){
                            case ArrowUp: if(selection > 0) selection--;
                                break;
                            case ArrowDown: if(selection < elements.size()-1) selection++;
                                break;
                            case Enter: 
                                // Executes the selection, from the class Executable, depending on return state the method can
                                switch(elements.get(selection).execute(buffer)){
                                    // False - quit method that started menu
                                    case -1: exitDialogueBox();
                                        return false;
                                    // True - continue method that started menu
                                    case 0: exitDialogueBox();
                                        return true;
                                    // Keep running
                                    case 1:
                                        break;
                                    default: assert false;
                                }
                                break;
                            case Escape: exitDialogueBox();
                                return true;
                        }
                        try {
                            render();
                        } catch(OutOfSpaceException error){
                            assert false;
                        }
                    }
                    Thread.sleep(20);
                }
            } catch(InterruptedException interruptRequest){
                System.out.println(this+" Interrupted?");
            }
        // The non active part
        } else {
            // Either init the timer
            if (wait != -1) {
                try {
                    Thread.sleep(wait * 1000);
                } catch (InterruptedException ex) {
                    System.out.println(this+" Interrupted?");
                }
                exitDialogueBox();
            } else {
                while (true) {
                    // Or wait for input from the user
                    try {
                        Key key = terminal.readInput();
                        if (key != null) {
                            if (key.equals(exitKey)) {
                                exitDialogueBox();
                                return true;
                            }
                            if (enterKey != null)
                                if (key.equals(enterKey)){
                                    exitDialogueBox();
                                    return false;
                                }
                        }
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        System.out.println(this+" Interrupted?");
                        assert false;
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public String toString(){
        return "DialogueBox "+name;
    }
    
    private String repeat(char c, int i){
        String out = "";
        for(int k = 0; k < i; k++)
            out+= c;
        return out;
    }
}
