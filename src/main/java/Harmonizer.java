import org.jfugue.parser.ParserListenerAdapter;
import org.jfugue.pattern.Pattern;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Note;
import org.staccato.StaccatoParser;

import java.util.*;


public class Harmonizer {

    // TODO does this kind of table already exist?
    public static final double WHOLE_NOTE = 1.0;
    public static final double HALF_NOTE = 0.5;
    public static final double QUARTER_NOTE = 0.25;
    public static final double EIGHTH_NOTE = 0.125;
    public static final double SIXTEENTH_NOTE = 0.0625;

    public static final Map<Double, String> durationsToSymbolMap = new HashMap<>();
    static {
        durationsToSymbolMap.put(WHOLE_NOTE, "w");
        durationsToSymbolMap.put(HALF_NOTE, "h");
        durationsToSymbolMap.put(QUARTER_NOTE, "q");
        durationsToSymbolMap.put(EIGHTH_NOTE, "i");
        durationsToSymbolMap.put(SIXTEENTH_NOTE, "s");
    }

    public static String[] cMajorNotes = new String[]{"C","D","E","F","G","A","B"};
    public static String[] majorChords = new String[]{"I", "ii", "iii", "IV", "V", "vi", "vii"};


    public ChordProgression generateChordProgressionFromPhrase(Pattern phrase) {
        StaccatoParser staccatoParser = new StaccatoParser();
        NoteParserListener noteParserListener = new NoteParserListener();
        staccatoParser.addParserListener(noteParserListener);
        staccatoParser.parse(phrase);

        Map<Integer, List<Note>> beatToNotesMap = parseNotesIntoBeats(noteParserListener.getNotes());
        Map<Integer, String> beatToHarmonicCandidatesMap = generateHarmonicCandidates(beatToNotesMap);
        ChordProgression cp = generateChordProgressionFromChords(beatToHarmonicCandidatesMap);
        return cp;
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

    private Map<Integer, String> generateHarmonicCandidates(Map<Integer, List<Note>> beatToNotesMap) {
        Map<Integer, List<Note>> beatToHarmonyNotesMap = new HashMap<>();

        for(Map.Entry<Integer, List<Note>> entry : beatToNotesMap.entrySet()) {
            List<Note> harmonicNotes = new ArrayList<>();

            for(int i = 0; i < entry.getValue().size(); i++) {
                entry.getValue().get(i).setMelodicNote(true);

                // for now we just make the first note of the beat a harmonic one
                // logic to decide whether it should be considered a passing note, auxiliary, etc, must come later
                if(i == 0) {
                    entry.getValue().get(i).setHarmonicNote(true);
                    harmonicNotes.add(entry.getValue().get(i));
                }
                else {
                    entry.getValue().get(i).setHarmonicNote(false);
                }
            }
            beatToHarmonyNotesMap.put(entry.getKey(), harmonicNotes);
        }

        Random rand = new Random();
        Map<Integer, String> beatToHarmonyMap = new HashMap<>();

        for(Map.Entry<Integer, List<Note>> entry : beatToNotesMap.entrySet()) {
            if(!entry.getValue().isEmpty()) {
                Note note = entry.getValue().get(0);
                List<String> chordCandidates = chordCandidates(note);
                beatToHarmonyMap.put(entry.getKey(), chordCandidates.get(rand.nextInt(chordCandidates.size())));
            }
        }
        return beatToHarmonyMap;
    }

    private List<String> chordCandidates(Note note) {
        String noteLetter = note.originalString.substring(0, note.originalString.length() - 1);
        Double duration = note.getDuration() >= QUARTER_NOTE ? note.getDuration() : QUARTER_NOTE;

        List<String> chordCandidates = new ArrayList<>();
        for(int i = 0; i < cMajorNotes.length; i++) {
            if(cMajorNotes[i].equals(noteLetter)) {
                chordCandidates.add(String.format("%s-%s", majorChords[i], duration));
                if(i - 2 < 0) {
                    i += 7;
                }
                chordCandidates.add(String.format("%s-%s", majorChords[i - 2], duration));
                if(i - 4 < 0) {
                    i += 7;
                }
                chordCandidates.add(String.format("%s-%s", majorChords[i - 4], duration));
            }
        }
        return chordCandidates;
    }

    private ChordProgression generateChordProgressionFromChords(Map<Integer, String> beatToHarmonyMap) {
        String chords = "";
        String beatReplacement = "";

        List<Integer> beats = new ArrayList<>(beatToHarmonyMap.keySet());
        int i = 0;
        for(Integer beat : beats) {
            String chordWithDuration = beatToHarmonyMap.get(beat);
            String chord = chordWithDuration.split("-")[0];
            String duration = chordWithDuration.split("-")[1];

            chords = chords.concat(chord + " ");
            beatReplacement = beatReplacement.concat(String.format("$%s/%s ", i, duration));
            i++;
        }

        System.out.println(chords);
        System.out.println(beatReplacement);
        return new ChordProgression(chords.trim())
                .allChordsAs(beatReplacement)
                .setKey("C");
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
