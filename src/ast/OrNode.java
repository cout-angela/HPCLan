package ast;

import evaluator.HPCLanlib;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

import java.util.ArrayList;

public class OrNode implements Node {
    private final Node left ;
    private final Node right ;

    public OrNode (Node _left, Node _right) {
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

    @Override
    public Type typeCheck() {
        if ((left.typeCheck() instanceof BoolType) && (right.typeCheck() instanceof BoolType) )
            return new BoolType() ;
        else {
            System.out.println("Type Error: Non integers in addition") ;
            return new ErrorType() ;
        }
    }

    @Override
    public String codeGeneration() {
        String contlab= HPCLanlib.freshLabel();
        return left.codeGeneration() +
                "storei T1 0 \n" +
                "bneq A0 T1 "+ contlab + "\n" +
                right.codeGeneration()+
                contlab + ":\n";
    }

    @Override
    public String toPrint(String s) {
        return s+"Or\n" + left.toPrint(s+"  ") + right.toPrint(s+"  ") ;
    }

    @Override
    public Integer constValue(SymbolTable ST) {
		  Integer leftVal = left.constValue(ST);
		  Integer rightVal = right.constValue(ST);
		  
		  if (leftVal != null && rightVal != null) {
            if (leftVal != 0 || rightVal != 0) {
                return 1;
            }
            return 0;
		  }
		  
		  return null;
	}
}
