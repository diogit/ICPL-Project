package AST;

import types.IType;

public class Parameter {
	String id;
	IType type;
	
	public Parameter(String id, IType type){
		this.id = id;
		this.type = type;
	}
	
	public String toString(){
		return "("+this.id.toString()+":"+this.type.toString()+")";
	}
}
