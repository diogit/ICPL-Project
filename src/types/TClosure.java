package types;

import java.util.ArrayList;
import java.util.List;

public class TClosure implements IType {

	public List<IType> params;
	public IType body;
	
	public TClosure(List<IType> params, IType body) {
		this.params = params;
		this.body = body;
	}
	
	public String toString(){
		List<String> params = new ArrayList<String>(this.params.size());
		for(IType param : this.params){
			params.add(param.toString());
		}
		String paramsString = "["+String.join(", ", params)+"]";
		return "Closure(Params: "+paramsString+", Body: "+this.body.toString()+")"; 
	}
	
	public boolean equals(Object other) {
		if (other instanceof TClosure) {
			return this.params.equals(((TClosure) other).params) && this.body.equals(((TClosure)other).body);
		} else {
			return false;
		}
	}
}
