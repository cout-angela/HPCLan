package ast;

import evaluator.HPCLanlib;
import java.util.ArrayList;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class GeqNode implements Node {
    private final Node left ;
    private final Node right ;

    public GeqNode (Node _left, Node _right) {
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

        String true_lab = HPCLanlib.freshLabel();
        String cont_lab = HPCLanlib.freshLabel();

        return left.codeGeneration()+
                "pushr A0 \n" +
                right.codeGeneration()+
                "popr T1 \n" +
                "bleq A0 T1 " + true_lab + " \n" +
                "storei A0 0 \n" +
                "b " + cont_lab + " \n" +
                true_lab + ": \n storei A0 1 \n" +
                cont_lab + ": \n";
    }

    public String toPrint(String s) {
        return s+"Geq\n" + left.toPrint(s+"  ") + right.toPrint(s+"  ") ;
    }

    public Integer constValue(SymbolTable ST) {
		  Integer leftVal = left.constValue(ST);
		  Integer rightVal = right.constValue(ST);
		  
		  if (leftVal != null && rightVal != null) {
			  return leftVal >= rightVal ? 1 : 0;
		  }
		  
		  return null;
	  }
}
