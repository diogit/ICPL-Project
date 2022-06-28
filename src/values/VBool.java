package values;

public class VBool implements IValue{
	private boolean value;

	public VBool(boolean b){
		this.value = b;
	}
	
	public boolean getVal(){
		return this.value;
	}
	
	public String toString(){
		return Boolean.toString(this.value);
	}
}
