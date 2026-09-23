package ast;

import evaluator.HPCLanlib;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

import java.util.ArrayList;

public class LeqNode implements Node {
    private final Node left ;
    private final Node right ;

    public LeqNode (Node _left, Node _right) {
        left = _left ;
        right = _right ;
    }

    @Override
    public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
        ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
        errors.addAll(left.checkSemantics(ST, _nesting));
        errors.addAll(right.checkSemantics(ST, _nesting));

        return errors;
    }

    public Type typeCheck() {
        if ((left.typeCheck() instanceof IntType) && (right.typeCheck() instanceof IntType) )
            return new BoolType() ;
        else {
            System.out.println("Type Error: Non integers in addition") ;
            return new ErrorType() ;
        }
    }

    public String codeGeneration() {
        String ltrue = HPCLanlib.freshLabel();
        String lend = HPCLanlib.freshLabel();
        return  left.codeGeneration()+
                "pushr A0 \n" +
                right.codeGeneration()+
                "popr T1 \n" +
                "bleq T1 A0 " + ltrue +"\n"+
                "storei A0 0 \n" +
                "b " + lend + "\n" +
                ltrue + ":\n"+
                "storei A0 1\n" +
                lend + ":\n";
    }

    public String toPrint(String s) {
        return s+"LessOrEqual:\n" + left.toPrint(s + "    ") + "\n" + right.toPrint(s + "    ") ;
    }

    public Integer constValue(SymbolTable ST) {
		  Integer leftVal = left.constValue(ST);
		  Integer rightVal = right.constValue(ST);
		  
		  if (leftVal != null && rightVal != null) {
			  return leftVal <= rightVal ? 1 : 0;
		  }
		  
		  return null;
	  }

}
