package ast;

import java.util.ArrayList;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class ConstNode implements Node {
	private final String id;
	private final Node type;
	private final Node exp;
	
	public ConstNode(String _id, Node _type, Node _exp) {
		id = _id ;
		type = _type ;
		exp = _exp ;
	}
  
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST) {
   		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
        errors.addAll(exp.checkSemantics(ST));
        
        if (ST.top_lookup(id))
        	errors.add(new SemanticError("Var id " + id + " already declared"));
        else ST.insert(id, (Type) type,"") ;
 
        return errors ;
	}
  
	public Type typeCheck () {
		if (exp.typeCheck().getClass().equals(type.getClass() )) 
			return null ;
		else {
			System.out.println("Type Error: incompatible type of expression for variable "+id) ;
			return new ErrorType() ;
		}     
	}
  
	public String codeGeneration() {
		return exp.codeGeneration() +
				"pushr A0 \n" ;
	}  
    
	public String toPrint(String s) {
		return s + "Var:" + id + type.toPrint(" ")  + exp.toPrint(s+" ") + "\t" ;
	}

}  