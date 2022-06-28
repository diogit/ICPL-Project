package AST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import compiler.Address;
import compiler.ICodeBlock;
import exceptions.IdentifierAlreadyExistsException;
import exceptions.IncorrectNumberOfArgumentsException;
import exceptions.NullIdentifierException;
import exceptions.TypeException;
import types.IType;
import types.TClosure;
import values.IValue;
import values.VClosure;

public class ASTFun implements ASTNode{
	List<Parameter> params;
	ASTNode body;
	IType type;
	
	public ASTFun(List<Parameter> params, ASTNode body) {
		this.params = params;
		this.body = body;
		this.type = null;
	}
	
	@Override
	public IValue eval(Environment<IValue> env) throws NullIdentifierException {
		return new VClosure(params, body, env);
	}
	
	public String toString(){
		List<String> params = new ArrayList<String>(this.params.size());
		for(Parameter param : this.params){
			params.add(param.toString());
		}
		String paramsString = "["+String.join(", ", params)+"]";
		return "ASTFun(Parameters: "+paramsString+", Body: "+this.body.toString()+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		List<IType> paramTypes = new ArrayList<IType>(params.size());
		Environment<IType> envlocal = env.beginScope();
		for (Parameter param : params) {
			paramTypes.add(param.type);
			envlocal.assoc(param.id, param.type);
		}
		IType bodyType = body.typecheck(envlocal);
		envlocal.endScope();
		this.type = new TClosure(paramTypes, bodyType);
		return this.type;
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException {
		// TODO Auto-generated method stub
		code.emit(";	ASTFun");
		System.out.println("Sempila");
	}

	@Override
	public IType getType() {
		return this.type;
	}
}
