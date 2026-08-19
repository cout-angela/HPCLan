package ast;

import java.util.ArrayList;

import semanticanalysis.SemanticError ;
import semanticanalysis.SymbolTable ;

public class ProgNode implements Node {
	private final Node exp;
  
	public ProgNode (Node _exp) {
		exp = _exp ;
	}
  
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST) {		
		return exp.checkSemantics(ST);
	}

	public Type typeCheck() {
		return exp.typeCheck();
	}  
  
	public String codeGeneration() {
		return exp.codeGeneration()+"halt\n";
	}  
  
	public String toPrint(String s) {
		return "Prog\n" + exp.toPrint("  ") ;
	}

}  