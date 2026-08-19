package ast;

import java.util.ArrayList;

public class ArrowType extends Type {
	private final ArrayList<Type> inputtype;
	private final Type outputtype;
	  
	public ArrowType(ArrayList<Type> _inputtype , Type _outputtype) {
	    inputtype = _inputtype ;
	    outputtype = _outputtype;
	}

	public Type get_outputtype () { //
		return outputtype;
	}
		  
	public ArrayList<Type> get_inputtype() { //
		return inputtype ;
	}
	
	public String toPrint(String s) {
		String tmp = "" ;
		for (Type t : inputtype)
			tmp = tmp + t.toPrint("") + " " ;
		tmp = tmp + "-> "+ outputtype.toPrint("") ;
		return s + "Function Type: " + tmp +  "\n" ;  
	  }
}
