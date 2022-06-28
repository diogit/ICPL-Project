package types;

public class TInt implements IType {

	private static TInt instance = null;
	
	public static TInt getInstance() {
		if(instance == null) {
			instance = new TInt();
		}
		return instance;
	}
	
	public String toString() {
		return "Integer";
	}
	
	public boolean equals(Object other) {
		return (other instanceof TInt) ? true : false;
	}
}
