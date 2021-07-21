import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.Token;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Note;
import org.staccato.StaccatoParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Harmonizer {

    // TODO does this kind of table already exist?
    public static double WHOLE_NOTE = 1.0;
    public static double HALF_NOTE = 0.5;
    public static double QUARTER_NOTE = 0.25;
    public static double EIGHTH_NOTE = 0.125;
    public static double SIXTEENTH_NOTE = 0.0625;

    public ChordProgression generateChordProgressionFromPhrase(Pattern phrase) {
        StaccatoParser staccatoParser = new StaccatoParser();
        NoteParserListener noteParserListener = new NoteParserListener();
        staccatoParser.addParserListener(noteParserListener);
        staccatoParser.parse(phrase);
        System.out.println(noteParserListener.getNotes());

        Map<Integer, List<Note>> beatToNotesMap = parseNotesIntoBeats(noteParserListener.getNotes());
        return new ChordProgression("I I^ IV V I")
                .allChordsAs("$0q $1q $2q $3q $4w")
                .setKey("C");
    }

    private Map<Integer, List<Note>> parseNotesIntoBeats(List<Note> notes) {
        Map<Integer, List<Note>> beatNotes = new HashMap<>();

        int beatCount = 0;
        double withinBeat = 0;

        List<Note> notesWithinBeat = new ArrayList<>();
        for (Note note : notes) {
            notesWithinBeat.add(note);

            withinBeat += note.getDuration();
            if (withinBeat >= QUARTER_NOTE) {
                beatNotes.put(beatCount, notesWithinBeat);
                beatCount++;
                withinBeat -= QUARTER_NOTE;

                double beatsCoveredByNote = withinBeat / QUARTER_NOTE;

                for(int i = 0; i < beatsCoveredByNote; i++) {
                    beatNotes.put(beatCount, new ArrayList<>());
                    beatCount++;
                    withinBeat -= QUARTER_NOTE;
                }

                notesWithinBeat = new ArrayList<>();
            }
        }

        return beatNotes;
    }

    private static class NoteParserListener extends ParserListenerAdapter {

        private List<Note> notes = new ArrayList<>();

        @Override
        public void onNoteParsed(Note note) {
            notes.add(note);
        }

        public List<Note> getNotes() {
            return notes;
        }
    }
}
