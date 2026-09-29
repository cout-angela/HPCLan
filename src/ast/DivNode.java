package ast;

import java.util.ArrayList;

import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class DivNode implements Node {
	private final Node left;
	private final Node right;

	public DivNode(Node _left, Node _right) {
		left = _left;
		right = _right;
	}

	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();

		errors.addAll(left.checkSemantics(ST, _nesting));
		errors.addAll(right.checkSemantics(ST, _nesting));

		return errors;
	}

	public Type typeCheck() {
		if ((left.typeCheck() instanceof IntType) && (right.typeCheck() instanceof IntType))
			return new IntType();
		else {
			System.out.println("Type Error: Non integers in division");
			return new ErrorType();
		}
	}

	public String codeGeneration() {
		return left.codeGeneration()
				+ "pushr A0 \n"
				+ right.codeGeneration()
				+ "popr T1 \n"
				+ "div T1 A0 \n"
				+ "popr A0 \n";
	}

	public String toPrint(String s) {
		return s + "Div:\n" + left.toPrint(s + "    ") + "\n" + right.toPrint(s + "    ");
	}

	public Integer constValue(SymbolTable ST) {
		Integer leftVal = left.constValue(ST);
		Integer rightVal = right.constValue(ST);

		if (leftVal != null && rightVal != null) {
			return leftVal / rightVal;
		}

		return null;
	}

}
