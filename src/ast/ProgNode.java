package ast;

import java.util.ArrayList;

import semanticanalysis.SemanticError ;
import semanticanalysis.SymbolTable ;

public class ProgNode implements Node {
	private final ArrayList<Node> decList;
	private final ArrayList<Node> stmList;
	private final Node exp;
	

	public ProgNode (ArrayList<Node> _decList, ArrayList<Node> _stmList, Node _exp) {
		decList = _decList ;
		stmList = _stmList ;
		exp = _exp;
	}
  
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST) {	
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		  
		for (Node dec : decList)
				errors.addAll(dec.checkSemantics(ST));

		errors.addAll(stmList.checkSemantics(ST));	
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