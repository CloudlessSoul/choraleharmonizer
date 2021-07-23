
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.theory.ChordProgression;

public class Main {

    public static void main(String[] args) {

        Player player = new Player();

        Pattern phrase1 = new Pattern("C6q G5q A5q G5i F5i | E5h G5h");

        Pattern phrase2 = new Pattern("A5i G5i A5q G5q A5i B5i | C6h C6h");

        ChordProgression chordsForPhrase1 = new Harmonizer().generateChordProgressionFromPhrase(phrase1);
        ChordProgression chordsForPhrase2 = new Harmonizer().generateChordProgressionFromPhrase(phrase2);

        Pattern phrase1WithHarmony = phrase1.setVoice(0).add(chordsForPhrase1.getPattern().setVoice(1));
        Pattern phrase2WithHarmony = phrase2.setVoice(0).add(chordsForPhrase2.getPattern().setVoice(1));
        player.play(phrase1WithHarmony, phrase2WithHarmony);
    }
}
