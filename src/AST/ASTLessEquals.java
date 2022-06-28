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
import values.VBool;
import values.VInt;

public class ASTLessEquals implements ASTNode{
	private ASTNode left;
	private ASTNode right;
	
	public ASTLessEquals(ASTNode t1, ASTNode t2) {
		left = t1;
		right = t2;
	}
	
	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		IValue v1 = left.eval(env);
		IValue v2 = right.eval(env);
		if(v1 instanceof VInt && v2 instanceof VInt){
			return new VBool(((VInt) v1).getVal() <= ((VInt) v2).getVal());
		} else {
			throw new ValueException("Illegal values to <=");
		}
	}
	
	public String toString(){
		return "ASTLessEquals("+this.left.toString()+", "+this.right.toString()+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		IType t1 = left.typecheck(env);
		IType t2 = right.typecheck(env);
		if(t1 instanceof TInt && t2 instanceof TInt){
			return TBool.getInstance();
		} else {
			throw new TypeException("Illegal types to <=");
		}
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit(";	ASTLessEquals");
		
		this.left.compile(code, env);
		this.right.compile(code, env);
		String TL = code.getLabel();
		String EL = code.getLabel();
		
		code.emit("if_icmple "+TL); //jumps to the True Label if left (2nd on stack) <= right (top of stack)
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
