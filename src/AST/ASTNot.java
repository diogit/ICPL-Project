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

public class ASTNot implements ASTNode{
	ASTNode node;
	
	public ASTNot(ASTNode node){
		this.node= node;
	}

	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		IValue v = node.eval(env);
		if(v instanceof VBool){
			return new VBool(!((VBool) v).getVal());
		} else {
			throw new ValueException("Illegal value to ~");
		}
	}
	
	public String toString(){
		return "ASTNot("+this.node.toString()+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		IType t = node.typecheck(env);
		if(t instanceof TBool){
			return t;
		} else {
			throw new TypeException("Illegal type to ~");
		}
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit(";	ASTNot");
		
		this.node.compile(code, env);
		String TL = code.getLabel();
		String EL = code.getLabel();
		
		code.emit("ifeq "+TL); //jumps to the True Label if node == false(0)
		//False branch
		code.emit("iconst_0");
		code.emit("goto "+EL); //Exit label to avoid going into the True Label branch
		//True branch
		code.emit(TL+":"); //True label
		code.emit("iconst_1");
		
		code.emit(EL+":"); //Exit label
	}

	@Override
	public IType getType() {
		return TBool.getInstance();
	}
}
