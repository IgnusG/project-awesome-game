/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: projectawesomegame
 */

package DisplayDriver;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import javax.swing.JFrame;

// Helps the terminal handle the requests:
// - Adds a buffer which can be requested or reloaded at any time
// - More Free Movement Controlls
public final class AdvancedTerminal {
    
    private final SwingTerminal terminal;
    
    private ScreenBuffer screenBuffer;
    // The minimum activeX can become | blocks the left part of the screen at minActiveX column
    private int minActiveX = 0;
    // The cursor position in x axis (columns)
    private int activeX;
    // The cursor position in y axis (rows)
    private int activeY;
    
    public final int width;
    public final int height;
    
    // Current back/fore-grounds
    private Terminal.Color foreground = Terminal.Color.DEFAULT;
    private Terminal.Color background = Terminal.Color.DEFAULT;

    public AdvancedTerminal(SwingTerminal terminal) {
        this.terminal = terminal;
        TerminalSize size = terminal.getTerminalSize();
        width = size.getColumns();
        height = size.getRows();
        refreshBuffer();
    }
    
    // Not advised to use
    public Terminal returnTerminal(){
        System.err.println("Warning: Changes made to Terminal will not propagate to Buffer!");
        return terminal;
    }
    
    // Recreates the whole screen from the buffer
    // This has no effect, if a new buffer hasn't been loaded previous to this
    // Rememeber, the buffer is the representation of what is on the screen
    public void flushScreen() {
        for (int k = 0; k < screenBuffer.height; k++) {
            for (int i = 0; i < screenBuffer.width; i++) {
                terminal.moveCursor(i, k);
                if(screenBuffer.get(i,k).backgroundColor != background)
                    applyBackgroundColor(screenBuffer.get(i,k).backgroundColor);
                if(screenBuffer.get(i,k).foregroundColor != foreground)
                    applyForegroundColor(screenBuffer.get(i,k).foregroundColor);
                terminal.putCharacter(screenBuffer.get(i,k).display);
            }
        }
        terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
        terminal.applyForegroundColor(Terminal.Color.DEFAULT);
        terminal.moveCursor(activeX, activeY);
    }
    
    // Creates a blank buffer
    public void refreshBuffer(){
        screenBuffer = new ScreenBuffer(width,height);
    }
    
    public void clearScreen(){
        refreshBuffer();
        terminal.clearScreen();
    }
    
    // Same as original functions with added support for buffer writing
    public void putCharacter(char c){
        terminal.putCharacter(c);
        screenBuffer.edit(activeX,activeY, new SpecialChar(c,foreground,background));
        activeX++;
    }
    public void putCharacter(SpecialChar c){
        if(background != c.backgroundColor)
            applyBackgroundColor(c.backgroundColor);
        if(foreground != c.foregroundColor)
            applyForegroundColor(c.foregroundColor);
        terminal.putCharacter(c.display);
        screenBuffer.edit(activeX,activeY, new SpecialChar(c));
        activeX++;
    }
    
    public void moveCursor(int x, int y){
        terminal.moveCursor(x, y);
        activeX = x;
        activeY = y;
    }
    
    // Functionality described by minActiveX
    public void lockAtColumn(int i){
        minActiveX = i;
    }
    public void unlockColumn(){
        minActiveX = 0;
    }
    
    public void nextLine(){
        activeX = minActiveX;
        activeY++;
        
        terminal.moveCursor(activeX, activeY);
    }
    
    // Works with Strings, prints them one character at a time
    public void write(String s) {
        for (int i = 0; i < s.length(); i++) 
            putCharacter(s.charAt(i));
    }
    // ... with new line at the end
    public void writeln(String s){
        write(s);
        nextLine();
    }
    // With specific coordinates
    public void write(String s, int x, int y) {
        moveCursor(x, y);
        write(s);
    }

    // Clearing of one row
    public void clear(int y) {
        for (int i = 0; i < width; i++) {
            write(" ", i, y);
        }
    }
    // Clearing the whole screen, custom function for clearScreen
    public void clear(){
        for(int y = 0; y < height; y++){
            clear(y);
        }
    }

    // Returns a new buffer based on the old one, not a reference since that would propagate changes across them
    public ScreenBuffer requestBuffer() {
        return new ScreenBuffer(screenBuffer);
    }
    
    // Returns the character from the screen
    public char getCharAt(int positionX, int positionY){
        return screenBuffer.get(positionX, positionY).display;
    }
    public SpecialChar getSpecialCharAt(int positionX, int positionY){
        return screenBuffer.get(positionX, positionY);
    }

    // Load a new buffer and propagate changes to the creen with flush
    public void loadBuffer(ScreenBuffer buffer) {
        screenBuffer = new ScreenBuffer(buffer);
        flushScreen();
    }
    
    // Standard functions
    public void applyForegroundColor(Terminal.Color color){
        foreground = color;
        terminal.applyForegroundColor(color);
    }
    public void applyBackgroundColor(Terminal.Color color){
        background = color;
        terminal.applyBackgroundColor(color);
    }
    
    public JFrame getJFrame(){
        return terminal.getJFrame();
    }
    public void setCursorVisible(boolean value){
        terminal.setCursorVisible(value);
    }
    public void enterPrivateMode(){
        terminal.enterPrivateMode();
    }
    public void exitPrivateMode(){
        terminal.exitPrivateMode();
    }
    public Key readInput(){
        return terminal.readInput();
    }
}
