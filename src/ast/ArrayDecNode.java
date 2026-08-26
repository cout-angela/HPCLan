package ast;

import java.util.ArrayList;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class ArrayDecNode implements Node {
	private final String id;
	private final Node type;
	private final Node dim;
	private final Integer valueDim ;

	public ArrayDecNode(String _id, Node _type, Node _dim ) {
		id = _id ;
		type = _type ;
		dim = _dim ;
	}
  
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST) {
   		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		st = ST.top_lookup(id) ;
        if (st)
        	errors.add(new SemanticError("Var id " + id + " already declared"));
		else {
			errors.addAll(dim.checkSemantics(ST));
			valueDim = dim.constValue(ST) ;
			if(valueDim == null)
				errors.add(new SemanticError("Array id " + id + " must be initialized with a constant value"));
			else if(valueDim <= 0)
				errors.add(new SemanticError("Array id " + id + " must be initialized with a positive constant value"));
			
			else ST.insert(id, (Type) type,"", valueDim, null) ;
			
			
        //else ST.insert(id, (Type) type,"") ; //TODO(): aggiungere dimensione array come flag di symbol table

        return errors ;
	}
  
	public Type typeCheck () {
		if (dim.typeCheck() instanceof IntType)
			return null ;
		else {
			System.out.println("Type Error: dimension of array " + id + " must be an integer") ;
			return new ErrorType() ;
		}     
	}
  
	public String codeGeneration() {
		return "subi SP "+ valueDim +"\n" ;
				
	}  
    
	public String toPrint(String s) {
		return s + "Array:" + id + type.toPrint(" ")  + dim.toPrint(s+" ") + "\t" ;
	}

	

}  