package ast;

import java.util.ArrayList;

import evaluator.HPCLanlib;
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

		for (Node stm : stmList)
				errors.addAll(stm.checkSemantics(ST));

		errors.addAll(exp.checkSemantics(ST));
		
		return errors;
	}

	public Type typeCheck() {
		if (decList!=null) 
			for (Node dec:decList)
				dec.typeCheck();
		if (stmList!=null) 
			for (Node stm:stmList)
				stm.typeCheck();

		return exp.typeCheck();
	}  
  
	public String codeGeneration() {
		
		String declCode="";
		if(decList.size() != 0) 
			for (Node d: decList)
				declCode += d.codeGeneration();
			
		String stmCode="";
		if(stmList.size() != 0)
			for (Node s: stmList)
				stmCode += s.codeGeneration();


		return  "move SP FP  \n"
				+ "pushr FP \n"
				+ declCode
				+ stmCode 
				+ exp.codeGeneration() 
				+ "halt\n" +
				HPCLanlib.getCode();
	} 
  
	public String toPrint(String s) {
		String decListStr="";
		if (decList!=null) 
			for (Node dec:decList) {
				decListStr += dec.toPrint(s);
			}

		String stmListStr= "";
		if (stmList!=null) 
		  for (Node stm:stmList) {
				stmListStr += stm.toPrint(s);
		  }
		return "Prog\n" 
		+ decListStr + "\n\t"
		+ stmListStr + "\n\t"
		+ exp.toPrint("  ") ;
	}

}  