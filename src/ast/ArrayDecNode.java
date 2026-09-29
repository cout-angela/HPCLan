package ast;

import java.util.ArrayList;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class ArrayDecNode implements Node {
	private final String id;
	private final Node type;
	private final Node dim;
	private Integer valueDim;

	public ArrayDecNode(String _id, Node _type, Node _dim) {
		id = _id;
		type = _type;
		dim = _dim;
	}

	@Override
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();

		if (ST.top_lookup(id))
			errors.add(new SemanticError("Var id " + id + " already declared"));
		else {
			errors.addAll(dim.checkSemantics(ST, _nesting));
			valueDim = dim.constValue(ST);
			if (valueDim == null)
				errors.add(new SemanticError("Array id " + id + " must be initialized with a constant value"));
			else if (valueDim <= 0)
				errors.add(new SemanticError("Array id " + id + " must be initialized with a positive constant value"));

			else
				ST.insert(id, (Type) type, "", valueDim, null, _nesting);

		}

		return errors;
	}

	@Override
	public Type typeCheck() {
		if (dim.typeCheck() instanceof IntType)
			return null;
		else {
			System.out.println("Type Error: dimension of array " + id + " must be an integer");
			return new ErrorType();
		}
	}

	@Override
	public String codeGeneration() {
		return "subi SP " + valueDim + "\n";

	}

	@Override
	public String toPrint(String s) {
		return s + "Array: " + id + type.toPrint(s) + " [" + dim.toPrint("") + "]";
	}

}