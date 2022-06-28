package AST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import compiler.Address;
import compiler.Frame;
import compiler.ICodeBlock;
import exceptions.DivisionByZeroException;
import exceptions.IdentifierAlreadyExistsException;
import exceptions.IncorrectNumberOfArgumentsException;
import exceptions.NullIdentifierException;
import exceptions.TypeException;
import exceptions.ValueException;
import types.IType;
import values.IValue;

public class ASTLet implements ASTNode {

	List<Declaration> declarations;
	ASTNode body;
	IType type;
	
	public ASTLet(List<Declaration> decl, ASTNode t2) {
		this.declarations = decl;
		this.body = t2;
		this.type = null;
	}

	@Override
	public IValue eval(Environment<IValue> env) throws ValueException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException, DivisionByZeroException {
		Environment<IValue> envlocal = env.beginScope();
		for(Declaration decl : this.declarations){
			IValue v1 = decl.node.eval(env);
			envlocal.assoc(decl.id, v1);
		}
		IValue val = body.eval(envlocal);
		env = envlocal.endScope();
		return val;
	}
	
	public String toString(){
		List<String> declarations = new ArrayList<String>(this.declarations.size());
		for(Declaration decl : this.declarations){
			declarations.add(decl.toString());
		}
		String declsString = "["+String.join(", ", declarations)+"]";
		return "ASTLet(Declarations: "+declsString+", Body: "+this.body.toString()+")";
	}
	
	public IType typecheck(Environment<IType> env) throws TypeException, NullIdentifierException, IdentifierAlreadyExistsException, IncorrectNumberOfArgumentsException {
		Environment<IType> envlocal = env.beginScope();
		for(Declaration decl : this.declarations){
			IType nodeType = decl.node.typecheck(env);
			if (!decl.type.equals(nodeType)) {
				throw new TypeException("Expression type does not match declared type");
			}
			envlocal.assoc(decl.id, decl.type);
		}
		this.type = body.typecheck(envlocal);
		env = envlocal.endScope();
		return this.type;
	}

	@Override
	public void compile(ICodeBlock code, Environment<Address> env) throws IOException, IdentifierAlreadyExistsException, NullIdentifierException {
		code.emit(";	ASTLet");
		
		Frame frame = code.createFrame();
		String frameName = frame.getName();
				
		code.emit("new " + frameName);
		code.emit("dup");
		code.emit("invokespecial "+frameName+"/<init>()V");

		Frame upperFrame = frame.getUpperFrame();
		if(upperFrame != null) {
			code.emit("dup");
			code.emit("aload_0");
			code.emit("putfield "+frame.getName()+"/SL L"+upperFrame.getName()+";");
		}
		
		Environment<Address> newEnv = env.beginScope();

		for (Declaration decl : this.declarations) {
			int loc = frame.newLoc();
			Address address = new Address(0, loc);
			
			newEnv.assoc(decl.id, address);
			
			code.emit("dup");
			
			decl.node.compile(code, env);

			String typeField = frame.addLoc(decl.node.getType());
			
			code.emit("putfield "+frame.getName()+"/loc_"+loc+" "+typeField);
		}

		code.emit("astore_0");

		code.setCurrentFrame(frame);
		
		this.body.compile(code, newEnv);
		newEnv.endScope();
		if(upperFrame != null) {
			code.emit("aload_0");
			code.emit("checkcast "+frameName);	
			code.emit("getfield "+frame.getName()+"/SL L"+upperFrame.getName()+";");	
		}
		else {
			code.emit("aconst_null");
		}
		
		code.emit("astore_0");
	}

	@Override
	public IType getType() {
		return this.type;
	}
}
