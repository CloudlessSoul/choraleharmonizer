
public enum Cadence {
	
	PERFECT("V I"),
	PLAGAL("IV I"),
	IMPERFECT_I("I V"),
	IMPERFECT_II("II V"),
	IMPERFECT_IV("IV V"),
	INTERRUPTED("V VI");

	String pattern;
	
	Cadence(String pattern) {
		this.pattern = pattern;
	}

}
