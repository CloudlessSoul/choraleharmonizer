
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.theory.ChordProgression;

public class Main {

    public static void main(String[] args) {

        Player player = new Player();

        Pattern phrase1 = new Pattern("C6q G5q A5q G5i F5i | E5h G5h");

        Pattern phrase2 = new Pattern("A4i G4i A4q G4q A4i B4i | C5h C5h");

        // now manually add the chords to see how that would work with JFugue

        ChordProgression cp = new ChordProgression("I I^ IV V I")
                .allChordsAs("$0q $1q $2q $3q $4w")
                .setKey("C");

        //player.play(phrase1.setVoice(0).add(cp.getPattern().setVoice(1)));

        cp = new Harmonizer().generateChordProgressionFromPhrase(phrase2);

        player.play(phrase2.setVoice(0).add(cp.getPattern().setVoice(1)));

    }
}
