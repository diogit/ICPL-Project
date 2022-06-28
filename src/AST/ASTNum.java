package AST;

import compiler.Address;
import compiler.ICodeBlock;
import exceptions.TypeException;
import exceptions.ValueException;
import types.IType;
import types.TInt;
import values.IValue;
import values.VInt;

public class ASTNum implements ASTNode{
	private IValue value;
	
	public ASTNum(IValue value){
		this.value = value;
	}
	
	@Override
	public IValue eval(Environment<IValue> env) throws ValueException {
		if(value instanceof VInt){
			return value;
		} else {
			throw new ValueException("Not a valid integer value");
		}
	}
	
	public String toString(){
		return "ASTNum("+this.value.toString()+")";
	}
	
	@Override
	public IType typecheck(Environment<IType> env) throws TypeException {
		if(value instanceof VInt){
			return TInt.getInstance();
		} else {
			throw new TypeException("Not a valid integer type");
		}
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) {
		code.emit(";	ASTNum");
		
		code.emit("sipush "+this.value);
	}

	@Override
	public IType getType() {
		return TInt.getInstance();
	}

}
