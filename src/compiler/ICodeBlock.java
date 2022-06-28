package compiler;

import java.io.IOException;

import types.IType;

public interface ICodeBlock {
	String getLabel();
	void emit(String s);
	void dump(String filename) throws IOException;
	Reference createReference(IType type) throws IOException;
	Reference getReference(IType type);
	Frame createFrame();
	void setCurrentFrame(Frame frame);
	Frame getCurrentFrame();
}
