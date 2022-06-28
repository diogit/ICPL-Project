package AST;

import java.util.HashMap;
import java.util.Map;

import compiler.Address;
import exceptions.IdentifierAlreadyExistsException;
import exceptions.NullIdentifierException;

public class Environment<T> { //Environment<IValue>
	
	Environment<T> parent;
	Map<String, T> ids;
	
	
	public Environment() {
		parent = null;
		ids = new HashMap<String, T>();
	}

	public Environment(Environment<T> parent){
		this();
		this.parent = parent;
	}


	public Environment<T> beginScope(){
		//Create child environment
		return new Environment<T>(this);
	}
	
	public T find(String id) throws NullIdentifierException {
		T value = this.ids.get(id);
		Environment<T> parent = this.parent;
		//Recursively try to find the variable value in one of the parents
		while(value == null && parent != null){
			value = parent.find(id);
			parent = parent.parent;
		}
		if (value == null) {
			// Identifier was not declared
			throw new NullIdentifierException("The identifier was not declared");
		}
		return value;
	}
	
	public Address lookup(String id) throws NullIdentifierException {
		T value = this.ids.get(id);
		Environment<T> parent = this.parent;
		//Recursively try to find the variable value in one of the parents
		int jumps = 0;
		while(value == null && parent != null){
			value = parent.find(id);
			parent = parent.parent;
			jumps++;
		}
		if (value == null) {
			// Identifier was not declared
			throw new NullIdentifierException("The identifier was not declared");
		}
		((Address) value).setLevel(jumps);
		return (Address) value;
	}
	
	public void assoc(String id, T value) throws IdentifierAlreadyExistsException{
		if (ids.containsKey(id)) {
			// Identifiers must be unique per scope			
			throw new IdentifierAlreadyExistsException("Identifier already declared");
		}
		ids.put(id, value);
	}
	
	public Environment<T> endScope(){
		return parent;
	}
	
	public String toString(){
		return "Environment with "+this.ids.size()+" mappings stored.";
	}
}
