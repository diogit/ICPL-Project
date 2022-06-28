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

public class ASTAssign implements ASTNode{
	ASTNode id;
	ASTNode exp;
	IType type;
	
	public ASTAssign(ASTNode id, ASTNode exp){
		this.id = id;
		this.exp = exp;
		this.type = null;
	}

	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		IValue cell = this.id.eval(env);
		if(cell instanceof VCell){
			IValue value = exp.eval(env);
			if(value instanceof IValue){				
				((VCell) cell).setVal(value);
				return value;
			} else {
				throw new ValueException("Not a valid value to store in memory cell.");
			}
		} else {
			throw new ValueException("Not a memory cell.");
		}
	}

	public String toString(){
		return "ASTAssign(ID: "+this.id.toString()+", Expression: "+this.exp.toString()+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		IType cell = this.id.typecheck(env);
		if(cell instanceof TCell){
			IType value = exp.typecheck(env);
			if(value instanceof IType){				
				((TCell) cell).setType(value);
				this.type = value;
				return this.type;
			} else {
				throw new TypeException("Not a valid type to store in memory cell.");
			}
		} else {
			throw new TypeException("Not a memory cell.");
		}
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit(";	ASTAssign");
		
		this.id.compile(code, env);
		Reference ref = code.getReference(this.type);
		code.emit("dup");
		this.exp.compile(code, env);
		code.emit("putfield "+ref.getName()+"/value "+ref.getRefField());
		code.emit("getfield "+ref.getName()+"/value "+ref.getRefField());
	}

	@Override
	public IType getType() {
		return this.type;
	}
}
