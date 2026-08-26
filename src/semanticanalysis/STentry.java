package semanticanalysis;

import ast.Type;

public class STentry {
	private final Type type ;
	private final int dim ;
	private final int offset ;
	private String label ;
	private int value ; // added to store the value of the constant

	
	public STentry(Type _type, int _offset, String  _label, int _dim, int _value) {
		type = _type ;
		offset = _offset ;
		label = _label ;
		dim = _dim ;
		value = _value ;
	}
	
	public Type gettype() {
		return type ;
	}

	public int getoffset() {
		return offset ;
	}

	public int getdim() {
		return dim ;
	}
	
	public String getlabel() {
		return label ;
	}

	public int getvalue() {
		return value ;
	}

}
