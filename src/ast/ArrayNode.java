package ast;

import java.util.ArrayList;

import evaluator.HPCLanlib;
import semanticanalysis.STentry;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class ArrayNode implements Node {
	private final String id ;
	private Node index ;
	private STentry type ;
	private int nesting;
  
	public ArrayNode (String _id, Node _index) {
		id = _id ;
		index = _index ;
	}
  
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		nesting = _nesting;
		
		STentry st_type = ST.lookup(id) ;
	
		if (st_type == null)
			errors.add(new SemanticError("Id " + id + " not declared"));
		else if (st_type.getdim() == 0)
			errors.add(new SemanticError("Id " + id + " is not an array"));
		else type = st_type ;

		errors.addAll(index.checkSemantics(ST, _nesting));
		return errors;
	}

  
	public Type typeCheck() {
		if (type.gettype() instanceof ArrowType) {
			System.out.println("Wrong usage of function identifier");
			return new ErrorType();
		}

		if(index.typeCheck() instanceof IntType) {
			return type.gettype();
		}
	
		System.out.println("Type Error: index of array "+id+" must be an integer") ;
		return new ErrorType() ;
	}

  
	public String codeGeneration() {
		//String err = HPCLanlib.getBoundsErrorLabel();

		String getAR="";
		for (int i=0; i < nesting - type.getnesting(); i++) 
			getAR += "store T1 0(T1) \n";
		
		return 
			index.codeGeneration() //metto indice sullo stack
			+ "move AL T1 \n"
			+ getAR  //risalgo la catena statica
			+ "subi T1 " + type.getoffset() +"\n" //metto offset sullo stack
			+ "sub T1 A0 \n"
			+ "store A0 0(T1) \n" ; //carico sullo stack il valore all'indirizzo ottenuto
	}
	
	

	public String toPrint(String s) {
		return s+"Array: " + id   +"\n" + index.toPrint(s + "    ") ;
	}

	public Integer constValue(SymbolTable ST) {
		if(type != null)
			return type.getvalue();  
		else return null; 
	}
  
}  