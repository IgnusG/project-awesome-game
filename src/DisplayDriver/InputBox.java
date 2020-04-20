/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: DisplayDriver
 */

package DisplayDriver;

public class InputBox extends DialogueBox {

    public InputBox(String s) {
        super(s);
    }
    
    public String active(){
        return enableInput();
    }

}
