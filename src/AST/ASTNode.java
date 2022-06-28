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

public interface ASTNode {
	IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException;
	IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException;
	void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException;
	IType getType();
}
