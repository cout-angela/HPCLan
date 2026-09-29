package ast;

import java.util.ArrayList;

import evaluator.HPCLanlib;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class IfStmNode implements Node {
	private final Node guard;
	private final ArrayList<Node> thenstmList;
	private final ArrayList<Node> elsestmList;

	public IfStmNode(Node _guard, ArrayList<Node> _thenstmList, ArrayList<Node> _elsestmList) {
		guard = _guard;
		thenstmList = _thenstmList;
		elsestmList = _elsestmList;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();

		errors.addAll(guard.checkSemantics(ST, _nesting));

		for (Node stm : thenstmList)
			errors.addAll(stm.checkSemantics(ST, _nesting));
		for (Node stm : elsestmList)
			errors.addAll(stm.checkSemantics(ST, _nesting));

		return errors;
	}

	public Type typeCheck() {
		if (guard.typeCheck() instanceof BoolType) {
			for (Node stm : elsestmList)
				if (stm.typeCheck() != null) {
					System.out.println("Type Error: non void statement in else branch of if expression	");
					return new ErrorType();
				}
			for (Node stm : thenstmList)
				if (stm.typeCheck() != null) {
					System.out.println("Type Error: non void statement in then branch of if expression");
					return new ErrorType();
				}
			return null;
		} else {
			System.out.println("Type Error: non boolean condition in if");
			return new ErrorType();
		}
	}

	public String codeGeneration() {
		String lthen = HPCLanlib.freshLabel();
		String lend = HPCLanlib.freshLabel();

		String thenStmCode = "";
		if (thenstmList.size() != 0) {
			for (Node stm : thenstmList) {
				thenStmCode = thenStmCode + stm.codeGeneration();
			}
		}
		String elseStmCode = "";
		if (elsestmList.size() != 0) {
			for (Node stm : elsestmList) {
				elseStmCode = elseStmCode + stm.codeGeneration();
			}
		}
		return guard.codeGeneration() +
				"storei T1 1 \n" +
				"beq A0 T1 " + lthen + "\n" +
				elseStmCode +
				"b " + lend + "\n" +
				lthen + ":\n" +
				thenStmCode +
				lend + ":\n";
	}

	public String toPrint(String s) {

		String thenStmStr = "\n" + s + "    then:\n ";
		if (thenstmList.size() != 0) {
			for (Node stm : thenstmList) {
				thenStmStr = thenStmStr + stm.toPrint(s + "    ") + "\n";
			}
		}

		String elseStmStr = "\n" + s + "    else:\n ";
		if (elsestmList.size() != 0) {
			for (Node stm : elsestmList) {
				elseStmStr = elseStmStr + stm.toPrint(s + "    ") + "\n";
			}
		}

		return s + "IfStm:"
				+ "\n" + guard.toPrint(s + "    ")
				+ thenStmStr
				+ elseStmStr;
	}

}