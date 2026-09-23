package ast;

import java.util.ArrayList;
import java.util.HashMap;

import evaluator.HPCLanlib;
import semanticanalysis.STentry;
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
  
	@Override
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {	
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();

		
	    HashMap<String,STentry> H = new HashMap<String, STentry>();
	    ST.add(H);
		  
		for (Node dec : decList)
				errors.addAll(dec.checkSemantics(ST, _nesting));

		for (Node stm : stmList)
				errors.addAll(stm.checkSemantics(ST, _nesting));

		errors.addAll(exp.checkSemantics(ST, _nesting));
		ST.remove();
		
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
				+ "move SP AL \n"
				+ "pushr AL \n"
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
				decListStr += dec.toPrint(s + "    ") + "\n";
			}

		String stmListStr= "";
		if (stmList!=null) 
		  for (Node stm:stmList) {
				stmListStr += stm.toPrint(s + "    ") + "\n";
		  }
		return "Prog \n" 
			+ decListStr 
			+ stmListStr 
			+ exp.toPrint(s+"    ") ;
	}

}  