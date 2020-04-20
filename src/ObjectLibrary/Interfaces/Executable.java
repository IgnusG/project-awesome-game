/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: ObjectLibrary
 */

package ObjectLibrary.Interfaces;

import DisplayDriver.ScreenBuffer;

public interface Executable {
    // Return -1: Parent Menu quits - Parent Menu asks Parent to quit
    // Return 0: Parent Menu quits - Parent Menu asks Parent to continue, or to ignore it's return value
    // Return 1: Parent Menu continues
    // Other values prohibited
    public byte execute(ScreenBuffer buffer);
    public String identify();
}
