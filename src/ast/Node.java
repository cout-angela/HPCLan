package ast;

import java.util.ArrayList;

import semanticanalysis.SemanticError ;
import semanticanalysis.SymbolTable ;

public interface Node {

	ArrayList<SemanticError> checkSemantics(SymbolTable ST);
	Type typeCheck();
	String codeGeneration();

	String toPrint(String s);

	default Integer constValue(SymbolTable ST){
		return null;
	}

}  