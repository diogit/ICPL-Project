package compiler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import types.IType;
import types.TBool;
import types.TInt;

public class Reference {
	
	static int refCounter = 0;
	
	String name;
	IType type;
	String refField;
	
	public Reference(IType t) {
		type = t;
		name = "ref_" + Reference.refCounter++;
		refField = this.convertType(this.type);
	}
	
	public IType getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getRefField() {
		return this.refField;
	}
	
	void dump() throws FileNotFoundException {
		PrintStream out = new PrintStream(new FileOutputStream(name + ".j")); // create a new file ref_id.j
	
		out.println(".source " + name + ".j");
		out.println(".class " + name);
		out.println(".super java/lang/Object");
		out.println();
		out.println(".field public value " + refField);
		out.println();
		out.println(".method public <init>()V");
		out.println("   aload_0");
		out.println("   invokespecial java/lang/Object/<init>()V");
		out.println("   return");
		out.println(".end method");
		
		out.close();
	}

	public void addRefField(String fieldValue) {
		this.refField = fieldValue;
	}
	
	private String convertType(IType type) {
		if(type instanceof TInt || type instanceof TBool) {
			return "I";
		} else {
			return "Ljava/lang/Object";
		}
	}
}
