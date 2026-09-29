package ast;

import java.util.ArrayList;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;
import semanticanalysis.STentry;

public class AsgNode implements Node {
	private final String id;
	private final Node exp;
	private STentry st;
	private int nesting;

	public AsgNode(String _id, Node _exp) {
		id = _id;
		exp = _exp;
	}

	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		nesting = _nesting;
		errors.addAll(exp.checkSemantics(ST, _nesting));
		st = ST.lookup(id);
		if (st == null)
			errors.add(new SemanticError("Var id " + id + " not declared"));

		else if (st.getvalue() != null)
			errors.add(new SemanticError("Var id " + id + " is constant and cannot be assigned"));
		else if (st.getdim() > 0)
			errors.add(new SemanticError("Array identifier " + id + " used without index"));

		return errors;
	}

	public Type typeCheck() {
		if (exp.typeCheck().getClass().equals(st.gettype().getClass()))
			return null;
		else {
			System.out.println("Type Error: incompatible type of expression for variable " + id);
			return new ErrorType();
		}
	}

	public String codeGeneration() {
		String getAR = "";
		for (int i = 0; i < nesting - st.getnesting(); i++)
			getAR += "store T1 0(T1) \n";

		return exp.codeGeneration() +
				"move AL T1 \n" +
				getAR + // risalgo la catena statica
				"subi T1 " + st.getoffset() + "\n" + //
				"load A0 0(T1) \n";
	}

	public String toPrint(String s) {
		return s + "Asg: " + id + "\n" + exp.toPrint(s + "    ");
	}

}