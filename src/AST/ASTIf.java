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

public class ASTIf implements ASTNode{
	ASTNode ifnode;
	ASTNode thennode;
	ASTNode elsenode;
	
	public ASTIf(ASTNode t1, ASTNode t2, ASTNode t3){
		this.ifnode = t1;
		this.thennode = t2;
		this.elsenode = t3;
	}

	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		IValue v1 = ifnode.eval(env);
		if(v1 instanceof VBool){
			if(((VBool) v1).getVal()){ //then
				this.thennode.eval(env);
			} else { //else
				this.elsenode.eval(env);
			}
			return v1;
		} else {
			throw new ValueException("Illegal conditional value. Must be boolean.");
		}
	}
	
	public String toString(){
		return "ASTIf(If: "+this.ifnode.toString()+", Then: "+this.thennode.toString()+", Else: "+this.elsenode.toString()+")";
	}

	@Override
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		IType t1 = ifnode.typecheck(env);
		if(t1 instanceof TBool){
			// Typecheck the then and else nodes
			this.thennode.typecheck(env);
			this.elsenode.typecheck(env);
			// Return the type of the if
			return TBool.getInstance();
		} else {
			throw new TypeException("Illegal conditional type. Must be boolean.");
		}
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit(";	ASTIf");
		
		this.ifnode.compile(code, env);
		String TL = code.getLabel();
		String EL = code.getLabel();
		
		code.emit("ifne "+TL); //jumps to the True Label if ifnode == true(not 0)
		//Else branch
		this.elsenode.compile(code, env);
		code.emit("pop"); //Discard else body
		code.emit("goto "+EL); //Exit label to avoid going into the True Label branch
		//Then branch
		code.emit(TL+":"); //True label
		this.thennode.compile(code, env);
		code.emit("pop"); //Discard then body
		
		code.emit(EL+":"); //Exit label
		this.ifnode.compile(code, env); //Leave if condition evaluation value on stack
	}

	@Override
	public IType getType() {
		return TBool.getInstance();
	}
}
