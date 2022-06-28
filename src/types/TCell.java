package types;

public class TCell implements IType {

	private IType type;
	
	public TCell(IType t) {
		this.type = t;
	}
	
	public void setType(IType t){
		this.type = t;
	}
	
	public IType getType(){
		return this.type;
	}
	
	public String toString(){
		return "Cell(Type: "+this.type.toString()+")";
	}
	
	public boolean equals(Object other) {
		if (other instanceof TCell) {
			return this.type.equals(((TCell)other).getType());
		} else {
			return false;
		}
	}
}
