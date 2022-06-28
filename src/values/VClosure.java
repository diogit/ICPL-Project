package values;

import java.util.ArrayList;
import java.util.List;

import AST.ASTNode;
import AST.Environment;
import AST.Parameter;

public class VClosure implements IValue{

	public List<Parameter> params;
	public ASTNode body;
	public Environment<IValue> env;
	
	public VClosure(List<Parameter> params, ASTNode body, Environment<IValue> env) {
		this.params = params;
		this.body = body;
		this.env = env;
	}
	
	public String toString(){
		List<String> params = new ArrayList<String>(this.params.size());
		for(Parameter param : this.params){
			params.add(param.toString());
		}
		String paramsString = "["+String.join(", ", params)+"]";
		return "Closure(Params: "+paramsString+", Body: "+this.body.toString()+", Environment: "+this.env.toString()+")"; 
	}
}
