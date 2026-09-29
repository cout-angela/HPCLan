package ast;

import java.util.ArrayList;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;
import semanticanalysis.STentry;

public class ArrayStmNode implements Node {
	private final String id;
	private final Node exp;
	private final Node index;

	private STentry st;
	private int nesting;

	public ArrayStmNode(String _id, Node _exp, Node _index) {
		id = _id;
		exp = _exp;
		index = _index;
	}

	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		nesting = _nesting;
		errors.addAll(exp.checkSemantics(ST, _nesting));
		errors.addAll(index.checkSemantics(ST, _nesting));

		st = ST.lookup(id);
		if (st == null)
			errors.add(new SemanticError("Var id " + id + " not declared"));

		else if (st.getdim() == 0)
			errors.add(new SemanticError("Var id " + id + " is not an Array"));

		return errors;
	}

	public Type typeCheck() {
		if (!exp.typeCheck().getClass().equals(st.gettype().getClass())) {
			System.out.println("Type Error: incompatible type of expression for array type " + id);
			return new ErrorType();
		} else if (!index.typeCheck().getClass().equals(IntType.class)) {
			System.out.println("Type Error: index of array " + id + " must be an integer");
			return new ErrorType();
		}
		return null;
	}

	public String codeGeneration() {

		String getAR = "";
		for (int i = 0; i < nesting - st.getnesting(); i++)
			getAR += "store T1 0(T1) \n";

		return index.codeGeneration()

				// indirizzo dell'elemento: indirizzo base dell'array - indice.
				+ "move AL T1 \n"
				+ getAR
				+ "subi T1 " + st.getoffset() + "\n"
				+ "sub T1 A0 \n"

				// valore da assegnare
				+ exp.codeGeneration()

				// recupera indirizzo elem. e scrivi valore
				+ "popr T1 \n"
				+ "load A0 0(T1) \n";
	}

	public String toPrint(String s) {
		return s + "AsgArray:" + id + "\n" + exp.toPrint(s + "    ");
	}

}