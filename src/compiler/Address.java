package compiler;

public class Address {
	int level; //número de frames a saltar
	int offset; //slot/index na frame
	
	public Address(int level, int offset) {
		this.level = 0;
		this.offset = 0;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setLoc(int offset) {
		this.offset = offset;
	}
	
	public int getLoc() {
		return this.offset;
	}
}
