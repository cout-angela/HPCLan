package ast;

import java.util.ArrayList;

import semanticanalysis.STentry;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class IdNode implements Node {
	private final String id ;
	private STentry type ;
	private int nesting;
  
	public IdNode (String _id) {
		id = _id ;
	}
  
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		nesting = _nesting;
		
		STentry st_type = ST.lookup(id) ;
	
		if (st_type == null)
			errors.add(new SemanticError("Id " + id + " not declared"));
		else if (st_type.getdim() > 0)
			errors.add(new SemanticError("Array identifier " + id + " used without index"));
		else type = st_type ;

		return errors;
	}
  
	public Type typeCheck() {
		if (type.gettype() instanceof ArrowType) {
			System.out.println("Wrong usage of function identifier");
			return new ErrorType();
		}
		return type.gettype();
	}
  
	public String codeGeneration() {
		if(type.getvalue() == null){
			String getAR="";
			for (int i=0; i < nesting - type.getnesting(); i++) 
				getAR += "store T1 0(T1) \n";
	    	return 
				"move AL T1 \n"
				+ getAR  //risalgo la catena statica
				+ "subi T1 " + type.getoffset() +"\n" //metto offset sullo stack
				+ "store A0 0(T1) \n" ; //carico sullo stack il valore all'indirizzo ottenuto
		}else{
			//costante (no offset, si trova su symbol table -> non devo risalire la catena)
			return "storeI A0 " + type.getvalue() + " \n";
		}
	}
	

	public String toPrint(String s) {
		return s+"Id:" + id  ;
	}

	public Integer constValue(SymbolTable ST) {
		if(type != null)
			return type.getvalue();  
		else return null; 
	}
  
}  