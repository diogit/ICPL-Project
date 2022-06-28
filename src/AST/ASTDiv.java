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
import types.TInt;
import values.IValue;
import values.VInt;

public class ASTDiv implements ASTNode{
	ASTNode left;
	ASTNode right;
	
	public ASTDiv(ASTNode f1, ASTNode f2) {
		left = f1;
		right = f2;
	}
	
	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		IValue v1 = left.eval(env);
		IValue v2 = right.eval(env);
		if(v1 instanceof VInt && v2 instanceof VInt){
			if(((VInt) v2).getVal() == 0) throw new DivisionByZeroException("Illegal arithmetic operation. Division by zero.");
			return new VInt(((VInt) v1).getVal() / ((VInt) v2).getVal());
		} else {
			throw new ValueException("Illegal values to /");
		}
	}
	
	public String toString(){
		return "ASTDiv(ID: "+this.left.toString()+", "+this.right.toString()+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		IType t1 = left.typecheck(env);
		IType t2 = right.typecheck(env);
		if(t1 instanceof TInt && t2 instanceof TInt){
			return t1;
		} else {
			throw new TypeException("Illegal types to /");
		}
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit(";	ASTDiv");
		
		this.left.compile(code, env);
		this.right.compile(code, env);
		code.emit("idiv");
	}

	@Override
	public IType getType() {
		return TInt.getInstance();
	}
}
