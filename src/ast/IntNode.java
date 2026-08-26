package ast;

import java.util.ArrayList;

import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class IntNode implements Node {
	private final Integer val;
  
	public IntNode (Integer _val) {
		val = _val ;
	}
	
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST) {
 	 	  return new ArrayList<SemanticError>();
	}
 	  
	public Type typeCheck(){
		return new IntType();
	} 
  
	public String codeGeneration() {
		return "storei A0 "+val+"\n";
	}

	public String toPrint(String s) {
	    return s + val;
	}

	public Integer constValue(SymbolTable ST) {
		return val;
	}
}  