package ast;

import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;
import evaluator.HPCLanlib;

import java.util.ArrayList;

public class NotNode implements Node {
    private final Node body;

    public NotNode(Node _body) {
        body = _body;
    }

    public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
        ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
        errors.addAll(body.checkSemantics(ST, _nesting));
        return errors;
    }

    public Type typeCheck() {
        if (body.typeCheck() instanceof BoolType)
            return new BoolType();
        else {
            System.out.println("Type Error: Non integers in negative expression");
            return new ErrorType();
        }
    }

    public String codeGeneration() {
        String ltrue = HPCLanlib.freshLabel();
        String lcont = HPCLanlib.freshLabel();
        return body.codeGeneration()
                + "storei T1 0 \n"
                + "beq A0 T1 " + ltrue + "\n"
                + "storei A0 0 \n"
                + "b " + lcont + "\n"
                + ltrue + ":\n"
                + "storei A0 1 \n"
                + lcont + ":\n";
    }

    public String toPrint(String s) {
        return s + "Not:\n" + body.toPrint(s + "    ");
    }

    public Integer constValue(SymbolTable ST) {
        Integer bd = body.constValue(ST);
        if (bd != null) {
            return 1 - bd;
        }
        return null;
    }

}