package AST;

import java.io.IOException;

import compiler.Address;
import compiler.ICodeBlock;
import compiler.Reference;
import exceptions.DivisionByZeroException;
import exceptions.IdentifierAlreadyExistsException;
import exceptions.IncorrectNumberOfArgumentsException;
import exceptions.NullIdentifierException;
import exceptions.TypeException;
import exceptions.ValueException;
import types.IType;
import types.TCell;
import values.IValue;
import values.VCell;

public class ASTDeref implements ASTNode{
	ASTNode node;
	IType type;
	
	public ASTDeref(ASTNode node){
		this.node = node;
		this.type = null;
	}
	
	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		IValue cell = this.node.eval(env);
		if(cell instanceof VCell){
			return ((VCell) cell).getVal();
		} else {
			throw new ValueException("Not a valid memory cell.");
		}
	}

	public String toString(){
		return "ASTDeref("+this.node.toString()+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		IType cell = this.node.typecheck(env);
		if(cell instanceof TCell){
			this.type = ((TCell) cell).getType();
			return this.type;
		} else {
			throw new TypeException("Not a valid memory cell.");
		}
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit(";	ASTDeref");
		
		//E
		this.node.compile(code, env);
		
		Reference ref = code.getReference(this.type);
		
		code.emit("checkcast "+ref.getName());
		code.emit("getfield "+ref.getName()+"/value "+ref.getRefField());
	}

	@Override
	public IType getType() {
		return this.type;
	}
}
