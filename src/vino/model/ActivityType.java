package vino.model;

public enum ActivityType {
	LIKE("like");
	
	private String mnemonic;
	
	private ActivityType(String mnemonic) {
		this.mnemonic = mnemonic;
	}
	
	public String getMnemonic() {
		return mnemonic;
	}

	public static ActivityType lookup(String str) {
		for(ActivityType each : values()) {
			if (each.mnemonic.equals(str)) return each;
		}
		return null;
	}
	
}
