package AST;

import java.util.ArrayList;
import java.util.List;

import compiler.Address;
import compiler.ICodeBlock;
import exceptions.DivisionByZeroException;
import exceptions.IdentifierAlreadyExistsException;
import exceptions.IncorrectNumberOfArgumentsException;
import exceptions.NullIdentifierException;
import exceptions.TypeException;
import exceptions.ValueException;
import types.IType;
import types.TClosure;
import values.IValue;
import values.VClosure;

public class ASTApply implements ASTNode {
	ASTNode function;
	List<ASTNode> args;
	IType type;
	
	public ASTApply(ASTNode function, List<ASTNode> args){
		this.function = function;
		this.args = args;
		this.type = null;
	}

	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		IValue closure = this.function.eval(env);
		if (closure instanceof VClosure){
			Environment<IValue> envlocal = ((VClosure) closure).env.beginScope();
			List<Parameter> params = ((VClosure) closure).params;
			if (params.size() == this.args.size()){
				for (int i = 0; i < params.size(); i++){					
					String id = params.get(i).id;
					IValue arg = this.args.get(i).eval(env);
					if(id instanceof String && arg instanceof IValue){
						envlocal.assoc(id, arg);
					} else {
						throw new ValueException("Parameter isn't a string or argument isn't an IValue.");
					}
				}
			} else {
				throw new IncorrectNumberOfArgumentsException("Wrong number of arguments.");
			}
			IValue result = ((VClosure) closure).body.eval(envlocal);
			envlocal.endScope();
			return result;
		} else {
			throw new ValueException("This isn't a function.");
		}
	}
	
	public String toString(){
		List<String> args = new ArrayList<String>(this.args.size());
		for(ASTNode arg: this.args){
			args.add(arg.toString());
		}
		String argsString = "["+String.join(", ",args)+"]";
		return "ASTApply(Function: "+this.function.toString()+", Arguments: "+argsString+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		IType closure = this.function.typecheck(env);
		if (closure instanceof TClosure) {
			List<IType> paramTypes = ((TClosure) closure).params;
			if (paramTypes.size() == args.size()) {
				for (int i = 0; i < paramTypes.size(); i++) {
					IType argType = args.get(i).typecheck(env);
					if (!argType.equals(paramTypes.get(i))) {
						throw new TypeException("Argument type does not match parameter type");
					}
				}
				// Argument types all match the parameters
				this.type = ((TClosure) closure).body;
				return this.type;
			} else {
				throw new IncorrectNumberOfArgumentsException("Wrong number of arguments.");
			}
		} else {
			throw new TypeException("This ain't a function, dummy.");
		}
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) {
		// TODO Auto-generated method stub
		code.emit(";	ASTApply");
		System.out.println("Sempila");
	}

	@Override
	public IType getType() {
		return this.type;
	}
}
