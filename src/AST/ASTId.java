package AST;

import java.io.IOException;

import compiler.Address;
import compiler.Frame;
import compiler.ICodeBlock;
import exceptions.NullIdentifierException;
import exceptions.TypeException;
import types.IType;
import types.TBool;
import types.TInt;
import values.IValue;

public class ASTId implements ASTNode{
	String id;
	IType type;
	
	public ASTId(String image) {
		this.id = image;
	}
	
	@Override
	public IValue eval(Environment<IValue> env) throws NullIdentifierException {
		return env.find(id);
	}
	
	public String toString(){
		return "ASTId("+this.id+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException {
		this.type = env.find(id);
		return this.type;
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, NullIdentifierException {
		code.emit(";	ASTId("+this.id+")");
		code.emit("aload_0");
		
		Frame currentFrame = code.getCurrentFrame();
		code.emit("checkcast "+currentFrame.getName());
		
		Address address = env.lookup(id);
		
		for(int level = address.getLevel(); level > 0; level--) {
			code.emit("getfield "+currentFrame.getName()+"/SL L"+currentFrame.getUpperFrame().getName()+";");			
			currentFrame = currentFrame.getUpperFrame();
		}
		
		code.emit("getfield "+currentFrame.getName()+"/loc_"+address.getLoc()+" "+this.convertType(this.type));
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
