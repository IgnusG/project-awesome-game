/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: projectawesomegame
 */

package DisplayDriver;
// Saves what's on the screen
public class ScreenBuffer {
    
    private SpecialChar[][] buffer;
    public final int height;
    public final int width;

    public ScreenBuffer(SpecialChar[][] buffer) {
        this.buffer = buffer;
        width = buffer.length;
        height = buffer[0].length;
    }
    public ScreenBuffer(ScreenBuffer buffer){
        width = buffer.width;
        height = buffer.height;
        
        this.buffer = new SpecialChar[width][height];
        
        for(int i = 0; i < width; i++)
            for(int k = 0; k < height; k++)
                this.buffer[i][k] = buffer.get(i,k);
    }
    
    public ScreenBuffer(int width, int height){
        buffer = new SpecialChar[width][height];
        for(int i = 0; i < width; i++)
            for(int k = 0; k < height; k++)
                buffer[i][k] = new SpecialChar(' ');
        this.width = width;
        this.height = height;
    }
    
    public void edit(int positionX, int positionY, SpecialChar c){
        buffer[positionX][positionY] = c;
    }
    public SpecialChar get(int positionX, int positionY){
        return buffer[positionX][positionY];
    }
}
