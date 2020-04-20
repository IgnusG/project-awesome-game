/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary.Exceptions;
// Falls die Properties Dataein Korrupt sind
public class InvalidFileException extends Exception {

    @Override
    public String toString() {
        return "This .properties file is either invalid or corrupted";
    }
}
