/*
 *  Autor: Jonatan Juhas - Informatik WS15
 *  Projekt: projectawesomegame
 */

package Main;

import DisplayDriver.DialogueBox;
import DisplayDriver.ScreenBuffer;
import DisplayDriver.AdvancedTerminal;
import ObjectLibrary.Interfaces.Executable;
import ObjectLibrary.Exceptions.OutOfSpaceException;
import ObjectLibrary.Objects.Enemy;
import ObjectLibrary.Objects.Entry;
import ObjectLibrary.Objects.Player;
import ObjectLibrary.Objects.Trap;
import com.googlecode.lanterna.input.Key;
// Help for the user
public class Help implements Executable {

    private final AdvancedTerminal terminal;
    
    public Help(AdvancedTerminal terminal) {
        this.terminal = terminal;
    }
    
    @Override
    public byte execute(ScreenBuffer buffer){
        try {
            DialogueBox help = new DialogueBox("Hilfe", "Das bist du "+Player.showOf().display+". Im Gegensatz zu den alten 2D Spielen,\nkann sich dein Charakter nicht in 2, sondern sogar in 4 Richtigungen bewegen!\nDies ist eine sehr wichtige Spielmechanik, weiß du? [Enter]",new Key(Key.Kind.Enter), new Key(Key.Kind.Escape));
            help.attach(terminal,buffer);
            if(!help.render())
                return 1;
            help = new DialogueBox("Hilfe", "Oben siehst du deinen Namen, Den Namen des Levels und deine Statistik. Die beinhaltet\n Informationen über dein verbleibendes Leben und dein Score welches wir durch die Gleichung: \n\u2211(j=n n-k)(-1)^j * (n+j-1 C k-1)*(2n-k C n-k-j)*(-1)^n-1 * Svn-k+j,j \nerhalten, wobei n,k,j völig irrelevant für die Lösung sind und wir die Existenz\nder Zweiten Sonne bewiesen haben.",new Key(Key.Kind.Enter), new Key(Key.Kind.Escape));
            help.attach(terminal,buffer);
            if(!help.render())
                return 1;
            help = new DialogueBox("Hilfe", "Das ist ein Schlüssel "+ObjectLibrary.Objects.Key.showOf().display+". Diesen Zeichen siehst du oben (also das Wort Schlüssel)\nals auch auf der Weltkarte. Oben steht wie viele von denen du hast. Je mehr, desto besser\nfür dein Score. Vergiss nicht, dass du die Mindestanzahl (Zahl oben links)\nzuerst erreichen musst dammit du den Level zu Ende spielen kannst. [Enter]",new Key(Key.Kind.Enter), new Key(Key.Kind.Escape));
            help.attach(terminal,buffer);
            if(!help.render())
                return 1;
            help = new DialogueBox("Hilfe", "Dieshier ist dein Ziel "+Entry.showOf().display+". Falls du genug Schlüssel hast und dich dieses Spiel\nzum Tode langweilt geh einfach auf den Sieg los! [Enter]",new Key(Key.Kind.Enter), new Key(Key.Kind.Escape));
            help.attach(terminal,buffer);
            if(!help.render())
                return 1;
            help = new DialogueBox("Hilfe", "Oh, fast vergessen. Deine Feinde "+Enemy.showOf().display+" und " + Trap.showOf().display+" [Enter]",new Key(Key.Kind.Enter), new Key(Key.Kind.Escape));
            help.attach(terminal,buffer);
            if(!help.render())
                return 1;
            help = new DialogueBox("Hilfe", "Dein erster Feind "+Enemy.showOf().display+" ist sehr agressiv und mag es nicht,\nwenn du zu nahe kommst, oder weit weg von ihm bist. Er magt dich ganz und gar nicht und wird\nsein bestes versuchen dich zu töten. Also komm lieber nicht zu nah.  [Enter]",new Key(Key.Kind.Enter), new Key(Key.Kind.Escape));
            help.attach(terminal,buffer);
            if(!help.render())
                return 1;
            help = new DialogueBox("Hilfe", "Der zweite "+Trap.showOf().display+" ist ziemlich inkompetent und deshalb steht er nur rum und tut gar nichts.\nBleib lieber weg, sie mögen es nicht aufgewacht zu werden [Enter]",new Key(Key.Kind.Enter), new Key(Key.Kind.Escape));
            help.attach(terminal,buffer);
            if(!help.render())
                return 1;
            help = new DialogueBox("Credits", "NEEEEEEEEEIIIIIIIIN - Du. Ok dann lieber keine Credits ;)",new Key(Key.Kind.Enter));
            help.attach(terminal,buffer);
            if(!help.render())
                return 1;
            
            
        } catch (OutOfSpaceException ex) {}
        return 1;
    }

    @Override
    public String identify() {
        return "Hilfe";
    }
    
}
