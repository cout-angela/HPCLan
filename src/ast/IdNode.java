package ast;

import java.util.ArrayList;

import semanticanalysis.STentry;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class IdNode implements Node {
	private final String id ;
	private STentry type ;
  
	public IdNode (String _id) {
		id = _id ;
	}
  
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		
		STentry st_type = ST.lookup(id) ;
		if (st_type == null)
			errors.add(new SemanticError("Id " + id + " not declared"));
		else type = st_type ;

		return errors;
	}
  
	public Type typeCheck () {
		if (type.gettype() instanceof ArrowType) { //
			System.out.println("Wrong usage of function identifier");
			return new ErrorType() ;
		} else return type.gettype() ;
	}
  
	public String codeGeneration() {
	    return 
		       //"subi FP " + type.getoffset() +"\n" //metto offset sullo stack
			   // "move FP T1 \n"
		      // + "subi T1 " + type.getoffset() +"\n" //metto offset sullo stack
			   //+ "store A0 0(T1) \n" ; //carico sullo stack il valore all'indirizzo ottenuto
			   "store A0 " + type.getoffset() +"(FP) \n" ; //carico sullo stack il valore all'indirizzo ottenuto
	}

	public String toPrint(String s) {
		return s+"Id:" + id  ;
	}

	public Integer constValue(SymbolTable ST) {
	    STentry e = ST.lookup(id);
		if (e == null) return null; 
		if (!e.isConstant()) return null; 
		return e.getValue();   
	}
  
}  