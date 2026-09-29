package ast;

import java.util.ArrayList;

import evaluator.HPCLanlib;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class WhileStmNode implements Node {
	private final Node cond;
	private final ArrayList<Node> stmList;

	public WhileStmNode(Node _cond, ArrayList<Node> _stmList) {
		cond = _cond;
		stmList = _stmList;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();

		errors.addAll(cond.checkSemantics(ST, _nesting));

		for (Node stm : stmList)
			errors.addAll(stm.checkSemantics(ST, _nesting));

		return errors;
	}

	public Type typeCheck() {
		if (cond.typeCheck() instanceof BoolType) {
			for (Node stm : stmList)
				if (stm.typeCheck() != null) {
					System.out.println("Type Error: non void statement in while body");
					return new ErrorType();
				}
			return null;
		} else {
			System.out.println("Type Error: non boolean condition in while condition");
			return new ErrorType();
		}
	}

	public String codeGeneration() {
		String whileCont = HPCLanlib.freshLabel();
		String whileEnd = HPCLanlib.freshLabel();

		String stmCode = "";
		if (stmList.size() != 0) {
			for (Node stm : stmList) {
				stmCode = stmCode + stm.codeGeneration();
			}
		}
		return "b " + whileCont + "\n" +
				whileCont + ":\n" +
				cond.codeGeneration() +
				"storei T1 0 \n" +
				"beq A0 T1 " + whileEnd + "\n" +
				stmCode +
				"b " + whileCont + "\n" +
				whileEnd + ":\n";
	}

	public String toPrint(String s) {
		String stmStr = "\n" + s + "do:\n ";
		if (stmList.size() != 0) {
			for (Node stm : stmList) {
				stmStr = stmStr + stm.toPrint(s + "    ") + "\n";
			}
		}
		return s + "While\n"
				+ cond.toPrint(s + "    ")
				+ stmStr;
	}

}