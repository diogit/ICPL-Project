package AST;

import compiler.Address;
import compiler.ICodeBlock;
import exceptions.TypeException;
import exceptions.ValueException;
import types.IType;
import types.TBool;
import values.IValue;
import values.VBool;

public class ASTBool implements ASTNode{
	private IValue value;

	public ASTBool(IValue value){
		this.value = value;
	}
	
	@Override
	public IValue eval(Environment<IValue> env) throws ValueException {
		if(value instanceof VBool){
			return value;
		} else {
			throw new ValueException("Not a valid boolean value");
		}
	}
	
	public String toString(){
		return "ASTBool("+this.value.toString()+")";
	}
	
	@Override
	public IType typecheck(Environment<IType> env) throws TypeException {
		if(value instanceof VBool){
			return TBool.getInstance();
		} else {
			throw new TypeException("Not a valid boolean type");
		}
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) {
		code.emit(";	ASTBool");
		if (((VBool) this.value).getVal()) {
			code.emit("iconst_1");
		} else {
			code.emit("iconst_0");
		}
	}

	@Override
	public IType getType() {
		return TBool.getInstance();
	}
}
