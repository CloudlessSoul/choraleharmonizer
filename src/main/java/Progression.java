
public enum Progression {
	
	Ic_V("I^^ V"),
	Ic_V_I("I^^ V I"),
	Ic_V7_I("I^^ V7 I"),
	Ic_V7_IVc_I("I^^ V7 IV^^ I"),
	IVc_I("IV^^ I");
	
	String pattern;
	
	
	Progression(String pattern) {
		this.pattern = pattern;
	}

}
