package ast;

import java.util.ArrayList;

import evaluator.HPCLanlib;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class IfNode implements Node {
	private final Node guard ;
	private final Node thenbranch ;
	private final Node elsebranch ;
  
	public IfNode (Node _guard, Node _thenbranch, Node _elsebranch) {
    	guard = _guard ;
    	thenbranch = _thenbranch ;
    	elsebranch = _elsebranch ;
  }
  
   @Override
  public ArrayList<SemanticError> checkSemantics(SymbolTable ST) {
	  ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
	  
	  errors.addAll(guard.checkSemantics(ST));
	  errors.addAll(thenbranch.checkSemantics(ST));
	  errors.addAll(elsebranch.checkSemantics(ST));
	  
	  return errors;
  }
  
	public Type typeCheck() {
		if (guard.typeCheck() instanceof BoolType) {
			Type thenexp = thenbranch.typeCheck() ;
			Type elseexp = elsebranch.typeCheck() ;
			if (thenexp.getClass().equals(elseexp.getClass()))
        		return thenexp;
			else {
        		System.out.println("Type Error: incompatible types in then and else branches");
        		return new ErrorType() ;	
			}
		} else {
			System.out.println("Type Error: non boolean condition in if");
			return new ErrorType() ;
		}   
	}
  
  	public String codeGeneration() {
  		String lthen = HPCLanlib.freshLabel(); 
  		String lend = HPCLanlib.freshLabel();
  		return guard.codeGeneration() +
			 "storei T1 1 \n" +
			 "beq A0 T1 "+ lthen + "\n" +			  
			 elsebranch.codeGeneration() +
			 "b " + lend + "\n" +
			 lthen + ":\n" +
			 thenbranch.codeGeneration() +
	         lend + ":\n" ; 
  	}

  	public String toPrint(String s) {
	    return
					s+"If\n"
							+ guard.toPrint(s+"  ")
							+ thenbranch.toPrint(s+"  ")
							+ "\n"
							+ elsebranch.toPrint(s+"  ") ;
	}
	  
}  