package ast;

import java.util.ArrayList;

import evaluator.HPCLanlib;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class IfStmNode implements Node {
	private final Node guard ;
	private final ArrayList<Node> thenstmList ;
	private final ArrayList<Node> elsestmList ;
  
	public IfStmNode (Node _guard,   ArrayList<Node> _thenstmList,  ArrayList<Node> _elsestmList) {
    	guard = _guard ;
    	thenstmList = _thenstmList ;
    	elsestmList = _elsestmList ; //TODO: check if the else branch is empty, if so, create a new empty node
	}
  
   @Override
  public ArrayList<SemanticError> checkSemantics(SymbolTable ST) {
	  ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
	  
	  errors.addAll(guard.checkSemantics(ST));

	  for (Node stm : thenstmList)
		  errors.addAll(stm.checkSemantics(ST));
	  for (Node stm : elsestmList)
		  errors.addAll(stm.checkSemantics(ST));
		  
	  return errors;
  }
  
	public Type typeCheck() {
		if (guard.typeCheck() instanceof BoolType) {
			for (Node stm : elsestmList)
		  		if(stm.typeCheck() != null){
					System.out.println("Type Error: non void statement in else branch of if expression	");
        			return new ErrorType() ;
				}
			for (Node stm : thenstmList)
		  		if(stm.typeCheck() != null){
					System.out.println("Type Error: non void statement in then branch of if expression");
        			return new ErrorType() ;
				}
			return null;
		} else {
			System.out.println("Type Error: non boolean condition in if");
			return new ErrorType() ;
		}   
	}
  
  	public String codeGeneration() {
  		String lthen = HPCLanlib.freshLabel(); 
  		String lend = HPCLanlib.freshLabel();

		String thenStmCode = "" ;
	    if (thenstmList.size() != 0) {
	    		for (Node stm:thenstmList){
	    			thenStmCode = thenStmCode + stm.codeGeneration();
	    		}
 	    }
		String elseStmCode = "" ;
	    if (elsestmList.size() != 0) {
	    		for (Node stm:elsestmList){
	    			elseStmCode = elseStmCode + stm.codeGeneration();
	    		}
 	    }
  		return guard.codeGeneration() +
			"storei T1 1 \n" +
			"beq A0 T1 "+ lthen + "\n" +
			elseStmCode +
			"b " + lend + "\n" +
			lthen + ":\n" +
			thenStmCode +
	        lend + ":\n" ; 
  	}

  	public String toPrint(String s) {
		String thenStmStr = "" ;
	    if (thenstmList.size() != 0) {
	    		for (Node stm:thenstmList){
	    			thenStmStr = thenStmStr + stm.toPrint(s+"  ");
	    		}
 	    }
		String elseStmStr = "" ;
	    if (elsestmList.size() != 0) {
	    		for (Node stm:elsestmList){
	    			elseStmStr = elseStmStr + stm.toPrint(s+"  ");
	    		}
 	    }
	    return
					s+"If\n"
							+ guard.toPrint(s+"  ")
							+ thenStmStr
							+ "\n"
							+ elseStmStr;
	}
	  
} 