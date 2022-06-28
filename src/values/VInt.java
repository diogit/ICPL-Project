package values;

public class VInt implements IValue{
	private int value;
	
	public VInt(int v){
		this.value = v;
	}
	
	public int getVal(){
		return this.value;
	}
	
	public String toString(){
		return Integer.toString(this.value);
	}
}
