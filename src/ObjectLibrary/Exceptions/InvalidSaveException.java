/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: Objects
 */

package ObjectLibrary.Exceptions;
// Falls die Save Dataeien Korrupt sind
public class InvalidSaveException extends Exception {

    @Override
    public String toString() {
        return "This .properties file is either invalid or corrupted";
    }
}