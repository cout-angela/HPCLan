package ast;

import evaluator.HPCLanlib;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

import java.util.ArrayList;

public class GtNode implements Node {
    private final Node left ;
    private final Node right ;

    public GtNode (Node _left, Node _right) {
        left = _left ;
        right = _right ;
    }

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

        // A0 = right, T1 = left
        // left > right
        // rigth < left
        // A0 < T1

        return  left.codeGeneration()+
                "pushr A0 \n" +
                right.codeGeneration()+
                "popr T1 \n" +
                "blt A0 T1 "+ ltrue +"\n"+
                "storei A0 0\n"+
                "b " + lend + "\n" +
                ltrue + ":\n"+
                "storei A0 1\n" +
                lend + ":\n";
    }

    public String toPrint(String s) {
        return s+"GT:\n" + left.toPrint(s + "    ") + "\n" + right.toPrint(s + "    ") ;
    }

    public Integer constValue(SymbolTable ST) {
		  Integer leftVal = left.constValue(ST);
		  Integer rightVal = right.constValue(ST);
		  
		  if (leftVal != null && rightVal != null) {
			  return leftVal > rightVal ? 1 : 0;
		  }
		  
		  return null;
	  }

}
