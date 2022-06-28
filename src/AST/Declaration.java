package AST;

import types.IType;

public class Declaration {
	String id;
	IType type;
	ASTNode node;
	
	public Declaration(String id, IType type, ASTNode node){
		this.id = id;
		this.type = type;
		this.node = node;
	}
	
	public String toString(){
		return "("+this.id.toString()+":"+this.type.toString()+", "+this.node.toString()+")";
	}
}
