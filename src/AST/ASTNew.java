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

public class ASTNew implements ASTNode{
	ASTNode node;
	IType type;
	
	public ASTNew(ASTNode node){
		this.node = node;
	}

	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		IValue value = this.node.eval(env);
		return new VCell(value);
	}
	
	public String toString(){
		return "ASTNew("+this.node.toString()+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		IType value = this.node.typecheck(env);
		this.type = new TCell(value);
		return this.type;
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit(";	ASTNew");
		
		Reference ref = code.createReference(((TCell) this.type).getType());
		
		//Create ref
		code.emit("new "+ref.getName());
		code.emit("dup");
		//Init ref
		code.emit("invokespecial "+ref.getName()+"/<init>()V");
		code.emit("dup");
		
		//Compile E
		this.node.compile(code, env);
		
		//Put E value inside ref
		code.emit("putfield "+ref.getName()+"/value "+ref.getRefField());
	}

	@Override
	public IType getType() {
		return this.type;
	}
}
