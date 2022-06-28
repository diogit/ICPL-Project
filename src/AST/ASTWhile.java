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

public class ASTWhile implements ASTNode {
	ASTNode condition;
	ASTNode body;
	
	public ASTWhile(ASTNode condition, ASTNode body){
		this.condition = condition;
		this.body = body;
	}

	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		IValue v1 = condition.eval(env);
		if(v1 instanceof VBool){
			boolean hasRun = ((VBool) v1).getVal();
			while(((VBool) v1).getVal()){ //while
				this.body.eval(env);
				v1 = condition.eval(env);
				if(!(v1 instanceof VBool)){ throw new ValueException("Illegal conditional value. Must be boolean."); }
			}
			//Return if the while loop was ever run or not
			return new VBool(hasRun);
		} else {
			throw new ValueException("Illegal conditional value. Must be boolean.");
		}
	}
	
	public String toString(){
		return "ASTWhile(While: "+this.condition.toString()+", Do: "+this.body.toString()+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		IType t1 = condition.typecheck(env);
		if(t1 instanceof TBool){
			this.body.typecheck(env);
			
			return TBool.getInstance();
		} else {
			throw new TypeException("Illegal conditional type. Must be boolean.");
		}
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit(";	ASTWhile");
		
		String CL = code.getLabel();
		String EL = code.getLabel();
		
		code.emit("iconst_0"); //False because body didn't run
		code.emit(CL+":");
		this.condition.compile(code, env);
		code.emit("ifeq "+EL); //jumps to the Exit Label if condition == false(0)
		code.emit("pop"); //Remove false
		code.emit("iconst_1"); //Push true because body is being ran
		//Body execution
		this.body.compile(code, env);
		code.emit("pop"); //Trash the body
		code.emit("goto "+CL); //Return to Condition label to reevaluate
		
		code.emit(EL+":"); //Exit label
		
	}

	@Override
	public IType getType() {
		return TBool.getInstance();
	}
}