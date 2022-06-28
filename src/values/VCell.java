package values;

public class VCell implements IValue{
	private IValue value;
	
	public VCell(IValue v){
		this.value = v;
	}
	
	public void setVal(IValue v){
		this.value = v;
	}
	
	public IValue getVal(){
		return this.value;
	}
	
	public String toString(){
		return "Cell(Value: "+this.value.toString()+")";
	}
}
