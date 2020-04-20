/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: ObjectLibrary
 */

package ObjectLibrary.Exceptions;

public class OutOfSpaceException extends Exception {

    @Override
    public String toString() {
        return "Out of space in dialogue box";
    }
}
