package compiler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import types.IType;
import types.TBool;
import types.TInt;

public class Frame {
	
	static int frameCounter = 0;
	
	String name;
	Frame upperFrame;
	List<String> fields;
	
	public Frame() {
		name = "frame_" + Frame.frameCounter++;
		this.upperFrame = null;
		this.fields = new ArrayList<String>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setUpperFrame(Frame frame) {
		this.upperFrame = frame;
	}
	
	public Frame getUpperFrame() {
		return this.upperFrame;
	}
	
	public int newLoc() {
		return this.fields.size();
	}
	
	public String addLoc(IType type) {
		String typeField = this.convertType(type);
		this.fields.add(typeField);
		return typeField;
	}
	
	void dump() throws FileNotFoundException {
		PrintStream out = new PrintStream(new FileOutputStream(name + ".j")); // create a new file ref_id.j
	
		out.println(".source " + name + ".j");
		out.println(".class " + name);
		out.println(".super java/lang/Object");
		out.println();
		
		if(this.upperFrame != null)
			out.println(".field public SL L" + this.upperFrame.getName()+";");
		
		for(int i = 0; i < this.fields.size(); i++) {			
			out.println(".field public loc_"+i+" " +this.fields.get(i));
		}
		out.println();
		
		out.println(".method public <init>()V");
		out.println("   aload_0");
		out.println("   invokespecial java/lang/Object/<init>()V");
		out.println("   return");
		out.println(".end method");
		
		out.close();
	}
	
	private String convertType(IType type) {
		if(type instanceof TInt || type instanceof TBool) {
			return "I";
		} else {
			return "Ljava/lang/Object";
		}
	}
}
