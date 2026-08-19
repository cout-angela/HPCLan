package ast;

import java.util.ArrayList;

import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class Type implements Node {
	public boolean isEqual(Type A, Type B) {
        return A.getClass().equals(B.getClass());
	}
	public String toPrint(String s) {
		if (isEqual(this, new IntType()))  return ("Int") ;
		else return ("Bool");
	}

	public ArrayList<SemanticError> checkSemantics(SymbolTable ST) {
		// It is never invoked
		return null;
	}
	@Override
	public Type typeCheck() {
		// It is never invoked
		return null;
	}
	@Override
	public String codeGeneration() {
		// It is never invoked
		return "";
	}

}
