package types;

public class TBool implements IType {

	private static TBool instance = null;
	
	public static TBool getInstance() {
		if(instance == null) {
			instance = new TBool();
		}
		return instance;
	}
	
	public String toString() {
		return "Boolean";
	}
	
	public boolean equals(Object other) {
		return (other instanceof TBool) ? true : false;
	}
}
