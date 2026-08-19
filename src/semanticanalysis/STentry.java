package semanticanalysis;

import ast.Type;

public class STentry {
	private final Type type ;
	private final int offset ;
	private String label ;

	
	public STentry(Type _type, int _offset) {
		type = _type ;
		offset = _offset ;

	}
	
	public STentry(Type _type, int _offset, String  _label) {
		type = _type ;
		offset = _offset ;
		label = _label ;
	}
	
	public Type gettype() {
		return type ;
	}

	public int getoffset() {
		return offset ;
	}
	
	public String getlabel() {
		return label ;
	}

}
