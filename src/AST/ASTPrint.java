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
import types.TInt;
import values.IValue;

public class ASTPrint implements ASTNode{
	ASTNode node;
	IType type;
	
	public ASTPrint(ASTNode node) {
		this.node = node;
		this.type = null;
	}
	
	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		IValue v = this.node.eval(env);
		System.out.println(v.toString());
		return v;
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		this.type = this.node.typecheck(env);
		return this.type;
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
		this.node.compile(code, env);
		code.emit("invokestatic java/lang/String/valueOf("+this.convertType(this.type)+")Ljava/lang/String;");
	    code.emit("invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
	}

	@Override
	public IType getType() {
		return this.type;
	}

	private String convertType(IType type) {
		if(type instanceof TInt || type instanceof TBool) {
			return "I";
		} else {
			return "Ljava/lang/Object";
		}
	}
}
