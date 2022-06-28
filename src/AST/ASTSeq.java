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
import values.IValue;

public class ASTSeq implements ASTNode {
	ASTNode left;
	ASTNode right;
	IType type;
	
	public ASTSeq(ASTNode t1, ASTNode t2){
		this.left = t1;
		this.right = t2;
		this.type = null;
	}

	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		this.left.eval(env);
		return this.right.eval(env);
	}

	public String toString(){
		return "ASTSeq("+this.left.toString()+", "+this.right.toString()+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		this.left.typecheck(env);
		this.type = this.right.typecheck(env);
		return this.type;
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit(";	ASTSeq");
		
		this.left.compile(code, env);
		code.emit("pop");
		this.right.compile(code, env);
	}

	@Override
	public IType getType() {
		return this.type;
	}
}
