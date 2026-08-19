package ast;

import evaluator.HPCLanlib;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

import java.util.ArrayList;

public class GeqNode implements Node {
    private final Node left ;
    private final Node right ;

    public GeqNode (Node _left, Node _right) {
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

}
