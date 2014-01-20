package vino.model;

public enum AvatarSource {
	GRAVATAR("gravatar"), LOCAL("local");
	
	private String mnemonic;
	
	private AvatarSource(String mnemonic) {
		this.mnemonic = mnemonic;
	}
	
	public String getMnemonic() {
		return mnemonic;
	}

	public static AvatarSource lookup(String str) {
		for(AvatarSource each : values()) {
			if (each.mnemonic.equals(str)) return each;
		}
		return null;
	}
	
}
