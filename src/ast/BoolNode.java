package ast;

import java.util.ArrayList;

import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class BoolNode implements Node {

	private final boolean val;

	public BoolNode(boolean _val) {
		val = _val;
	}

	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		return new ArrayList<SemanticError>();
	}

	public Type typeCheck() {
		return new BoolType();
	}

	public String codeGeneration() {
		return "storei A0 " + (val ? 1 : 0) + "\n";
	}

	public String toPrint(String s) {
		return s + val;
	}

	public Integer constValue(SymbolTable ST) {
		return val ? 1 : 0;
	}

}