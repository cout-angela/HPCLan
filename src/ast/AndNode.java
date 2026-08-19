package ast;

import evaluator.HPCLanlib;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

import java.util.ArrayList;

public class AndNode implements Node {
    private final Node left ;
    private final Node right ;

    public AndNode (Node _left, Node _right) {
        left = _left ;
        right = _right ;
    }

    public ArrayList<SemanticError> checkSemantics(SymbolTable ST) {
        ArrayList<SemanticError> errors = new ArrayList<SemanticError>();

        errors.addAll(left.checkSemantics(ST));
        errors.addAll(right.checkSemantics(ST));

        return errors;
    }

    public Type typeCheck() {
        if ((left.typeCheck() instanceof BoolType) && (right.typeCheck() instanceof BoolType) )
            return new BoolType() ;
        else {
            System.out.println("Type Error: Non integers in addition") ;
            return new ErrorType() ;
        }
    }
    public String codeGeneration() {
        String contlab= HPCLanlib.freshLabel();
        return left.codeGeneration() +
                "storei T1 0 \n" +
                "beq A0 T1 "+ contlab + "\n" +
                right.codeGeneration()+
                contlab + ":\n";
    }

    public String toPrint(String s) {
        return s+"And\n" + left.toPrint(s+"  ") + right.toPrint(s+"  ") ;
    }

}
