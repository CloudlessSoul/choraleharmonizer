
public enum Progression {
	
	Ic_V("Ic V"),
	Ic_V_I("Ic V I"),
	Ic_V7_I("Ic V7 I"),
	Ic_V7_IVc_I("Ic V7 IVc I"),
	IVc_I("IVc I");
	
	String pattern;
	
	
	Progression(String pattern) {
		this.pattern = pattern;
	}

}
