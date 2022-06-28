package AST;

import java.io.IOException;

import compiler.Address;
import compiler.ICodeBlock;
import exceptions.DivisionByZeroException;
import exceptions.IdentifierAlreadyExistsException;
import exceptions.IncorrectNumberOfArgumentsException;
import exceptions.NullIdentifierException;
import exceptions.TypeException;
import exceptions.ValueException;
import types.IType;
import types.TBool;
import values.IValue;
import values.VBool;

public class ASTAnd implements ASTNode{
	ASTNode left;
	ASTNode right;
	
	public ASTAnd(ASTNode t1, ASTNode t2){
		this.left = t1;
		this.right = t2;
	}

	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		IValue v1 = left.eval(env);
		IValue v2 = right.eval(env);
		if(v1 instanceof VBool && v2 instanceof VBool){
			return new VBool(((VBool) v1).getVal() && ((VBool) v2).getVal());
		} else {
			throw new ValueException("Illegal values to &&");
		}
	}
	
	public String toString(){
		return "ASTAnd("+this.left.toString()+", "+this.right.toString()+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		IType t1 = left.typecheck(env);
		IType t2 = right.typecheck(env);
		if(t1 instanceof TBool && t2 instanceof TBool){
			return t1;
		} else {
			throw new TypeException("Illegal types to &&");
		}
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit(";	ASTAnd");
		
		this.left.compile(code, env);
		this.right.compile(code, env);
		code.emit("iand");
	}

	@Override
	public IType getType() {
		return TBool.getInstance();
	}
}
