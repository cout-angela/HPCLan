package ast;

import java.util.ArrayList;

import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public interface Node {

    ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting);

    Type typeCheck();

    String codeGeneration();

    String toPrint(String s);

    default Integer constValue(SymbolTable ST) {
        return null;
    }

}
