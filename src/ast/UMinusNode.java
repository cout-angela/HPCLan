package ast;

import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

import java.util.ArrayList;

public class UMinusNode implements Node {
    private final Node body ;

    public UMinusNode(Node _body) {
        body = _body ;
    }

    public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
        ArrayList<SemanticError> errors = new ArrayList<SemanticError>();

        errors.addAll(body.checkSemantics(ST, _nesting));

        return errors;
    }

    public Type typeCheck() {
        if (body.typeCheck() instanceof IntType)
            return new IntType() ;
        else {
            System.out.println("Type Error: Non integers in negative expression") ;
            return new ErrorType() ;
        }
    }

    public String codeGeneration() {
        return 		body.codeGeneration()
                + "storei T1 0 \n"
                + "sub T1 A0 \n"
                + "popr A0 \n";
    }

    public String toPrint(String s) {
        return s+"Neg\n" + body.toPrint(s+"  ") ;
    }

    public Integer constValue(SymbolTable ST) {
		Integer bd = body.constValue(ST);
		  
		if (bd != null) {
		    return 0 - bd;
        }
		  
		return null;
	  }

}