package ast;

import java.util.ArrayList;

import evaluator.HPCLanlib;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class EqualNode implements Node {
	private final Node left;
	private final Node right;

	public EqualNode(Node _left, Node _right) {
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
		Type tl = left.typeCheck();
		Type tr = right.typeCheck();
		if (tl.getClass().equals(tr.getClass()))
			return new BoolType();
		else {
			System.out.println("Type Error: Different types in equality");
			return new ErrorType();
		}
	}

	public String codeGeneration() {
		String ltrue = HPCLanlib.freshLabel();
		String lend = HPCLanlib.freshLabel();
		return left.codeGeneration() +
				"pushr A0 \n" +
				right.codeGeneration() +
				"popr T1 \n" +
				"beq A0 T1 " + ltrue + "\n" +
				"storei A0 0\n" +
				"b " + lend + "\n" +
				ltrue + ":\n" +
				"storei A0 1\n" +
				lend + ":\n";
	}

	public String toPrint(String s) {
		return s + "Equal:\n" + left.toPrint(s + "    ") + "\n" + right.toPrint(s + "    ");
	}

	public Integer constValue(SymbolTable ST) {
		Integer leftValue = left.constValue(ST);
		Integer rightValue = right.constValue(ST);
		if (leftValue != null && rightValue != null) {
			return (leftValue.equals(rightValue)) ? 1 : 0;
		}
		return null; // null if either side is not a constant
	}
}