package ast;

import evaluator.HPCLanlib;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

import java.util.ArrayList;

public class UnEqualNode implements Node {
    private final Node left ;
    private final Node right ;

    public UnEqualNode (Node _left, Node _right) {
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
        Type tl = left.typeCheck() ;
        Type tr = right.typeCheck();
        if (tl.getClass().equals(tr.getClass()))
            return new BoolType() ;
        else {
            System.out.println("Type Error: Different types in equality") ;
            return new ErrorType() ;
        }
    }

    public String codeGeneration() {
        String ltrue = HPCLanlib.freshLabel();
        String lend = HPCLanlib.freshLabel();
        return left.codeGeneration()+
                "pushr A0 \n" +
                right.codeGeneration() +
                "popr T1 \n" +
                "bneq A0 T1 "+ ltrue +"\n"+
                "storei A0 0\n"+
                "b " + lend + "\n" +
                ltrue + ":\n"+
                "storei A0 1\n" +
                lend + ":\n";
    }

    public String toPrint(String s) {
            return s+"NotEqual\n" + left.toPrint(s+"  ") + right.toPrint(s+"  ") ;
    }

}
