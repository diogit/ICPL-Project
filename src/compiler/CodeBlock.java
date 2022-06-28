package compiler;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import types.IType;

public class CodeBlock implements ICodeBlock{
	final static String LABEL = "Label";
	
	String filename;
	
	int labelCounter;
	int frameCounter;
	Frame currentFrame;
	
	List<String> jvmOperations;
	List<Frame> frames;
	Map<IType, Reference> references;
	
	public CodeBlock() {
		this.filename = "";
		this.labelCounter = 0;
		this.frameCounter = 0;
		this.currentFrame = null;
		this.jvmOperations = new LinkedList<String>();
		this.references = new HashMap<IType, Reference>();
		this.frames = new ArrayList<Frame>();
	}
	
	public CodeBlock(int level, int offset) {
		this.labelCounter = 0;
		this.jvmOperations = new LinkedList<String>();
	}
	
	@Override
	public String getLabel() {
		return LABEL+this.labelCounter++;
	}
	
	@Override
	public void emit(String code) {
		this.jvmOperations.add(code);
	}
	
	@Override
	public void dump(String filename) throws IOException {
		this.filename = filename;
		PrintWriter out = new PrintWriter(new FileWriter(this.filename+".j"));
		this.writeHeader(out);
		for (String instruction : this.jvmOperations) {
			out.println(instruction);
		}
		this.writeFooter(out);
		this.dumpFrames();
		this.dumpRefs();
		out.close();
	}
	
	private void writeHeader(PrintWriter out) {
		out.println(".class public "+this.filename);
		out.println(".super java/lang/Object");
		out.println("");
		out.println(";");
		out.println("; standard initializer");
		out.println(".method public <init>()V");
		out.println("   aload_0");
		out.println("   invokenonvirtual java/lang/Object/<init>()V");
		out.println("   return");
		out.println(".end method");
		out.println("");
		out.println(".method public static main([Ljava/lang/String;)V");
		out.println("       ; set limits used by this method");
		out.println("       .limit locals 10");
		out.println("       .limit stack 256");
		out.println("");
		out.println("       ; setup local variables:");
		out.println("");
		out.println("       ;    1 - the PrintStream object held in java.lang.out");
		out.println("       getstatic java/lang/System/out Ljava/io/PrintStream;");
		out.println("");
		out.println("       ; place your bytecodes here");
		out.println("       ; START");
		out.println("");
	}
	
	private void writeFooter(PrintWriter out) {
		out.println("       ; END");
		out.println("");
		out.println("");
		out.println("       ; convert to String;");
		out.println("       ;invokestatic java/lang/String/valueOf(I)Ljava/lang/String;");
		out.println("       ; call println ");
		out.println("       ;invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
		out.println("");
		out.println("       return");
		out.println("");
		out.println(".end method");
	}
	
	private void dumpFrames() throws FileNotFoundException {
		for (Frame frame: this.frames) frame.dump();
	}
	
	private void dumpRefs() throws FileNotFoundException {
		for(Reference ref: this.references.values()) ref.dump();
	}
	
	public Reference createReference(IType type) throws IOException {
		Reference ref = new Reference(type);
		
		this.references.put(type, ref);

		return ref;
	}
	
	public Frame createFrame() {
		Frame frame = new Frame();
		
		this.frames.add(frame);
		
		frame.setUpperFrame(this.currentFrame);
		
		return frame;
	}
	
	@Override
	public void setCurrentFrame(Frame frame) {
		this.currentFrame = frame;
	}
	
	@Override
	public Frame getCurrentFrame() {
		return this.currentFrame;
	}
	
	public Reference getReference(IType type) {
		return this.references.get(type);
	}

}