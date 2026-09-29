package ast;

import evaluator.HPCLanlib;
import java.util.ArrayList;
import java.util.HashMap;

import semanticanalysis.STentry;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class MapredStmNode implements Node {
	private final String i;
	private Node iNode;

	private final Node n;
	private final Node RESstm;
	private Integer nesting;
	private STentry iEntry;

	public MapredStmNode(String _i, Node _n, Node _RESstm) {
		i = _i;
		n = _n;
		RESstm = _RESstm;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();

		if (ST.lookup(i) != null)
			errors.add(new SemanticError("Identifier " + i + " already declared"));
		else {
			HashMap<String, STentry> HM = new HashMap<String, STentry>();
			nesting = _nesting + 1;
			ST.add(HM);
			ST.insert(i, new IntType(), "", 0, null, _nesting + 1);

			iEntry = ST.lookup(i);

			errors.addAll(n.checkSemantics(ST, _nesting + 1));

			errors.addAll(RESstm.checkSemantics(ST, _nesting + 1));

			ST.remove();
		}

		return errors;
	}

	public Type typeCheck() {
		if (n.typeCheck() instanceof IntType) {
			return RESstm.typeCheck();
		} else {
			System.out.println("Type Error: type mismatch for mapred statement, expected int for n");
			return new ErrorType();
		}
	}

	/*
	 * 
	 * int i = n/2
	 * while (i >= 0) { // i < 0
	 * RES[j] = e
	 * i = i - 1
	 * }
	 * 
	 * i = n/2 + 1
	 * while (i < n) { // n <= i
	 * RES[j] = e
	 * i = i + 1
	 * }
	 */

	public String codeGeneration() {
		String whileCont = HPCLanlib.freshLabel();
		String whileEnd = HPCLanlib.freshLabel();
		String while2Cont = HPCLanlib.freshLabel();
		String while2End = HPCLanlib.freshLabel();
		String getAR = "move AL T1 \n";

		for (int i = 0; i < nesting - iEntry.getnesting(); i++)
			getAR += "store T1 0(T1) \n";

		getAR += "subi T1 " + iEntry.getoffset() + "\n";

		return

		// CREAZIONE AR
		"pushr FP \n"
				+ "pushr AL \n"
				+ "move SP FP \n"
				+ "addi FP 2\n"
				+ "move FP AL \n"
				+ "subi AL 1 \n"

				// INIZIALIZZIONE VARIABILE i
				+ n.codeGeneration()
				+ "divi A0 2 \n"
				+ "pushr A0 \n"

				// WHILE 1
				+ "b " + whileCont + "\n"
				+ whileCont + ":\n"
				+ "storei T1 0 \n"
				+ "blt A0 T1 " + whileEnd + "\n"
				+ RESstm.codeGeneration()
				+ getAR
				+ "store A0 0(T1) \n"
				+ "subi A0 1 \n"
				+ getAR
				+ "load A0 0(T1)\n"
				+ "b " + whileCont + "\n"

				+ whileEnd + ":\n"

				// WHILE 2
				// INIZIALIZZIONE VARIABILE i
				+ n.codeGeneration()
				+ "divi A0 2 \n"
				+ "addi A0 1 \n"
				+ getAR
				+ "load A0 0(T1) \n"

				+ "b " + while2Cont + "\n"
				+ while2Cont + ":\n"
				+ n.codeGeneration()
				+ "store T1 0(T1) \n"
				+ "bleq A0 T1 " + while2End + "\n"
				+ RESstm.codeGeneration()
				+ getAR
				+ "store A0 0(T1) \n"
				+ "addi A0 1 \n"
				+ getAR
				+ "load A0 0(T1) \n"
				+ "b " + while2Cont + "\n"

				+ while2End + ":\n"

				// RIMOZIONE AR
				+ "addi SP 1 \n" // rimuove la viariabile i
				+ "pop \n" // rimozione AL
				+ "store FP 0(FP) \n"
				+ "move FP AL \n"
				+ "subi AL 1 \n"
				+ "pop \n"; // rimozione control link
	}

	public String toPrint(String s) {

		return s + "Mapred:\n"
				+ s + "    " + i
				+ " upto " + n.toPrint("")
				+ "\n" + RESstm.toPrint(s + "    ");
	}

}