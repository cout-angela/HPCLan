package ast;

import java.util.ArrayList;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class ConstDecNode implements Node {
	private final String id;
	private final Node type;
	private final Node exp;
	
	public ConstDecNode(String _id, Node _type, Node _exp) {
		id = _id ;
		type = _type ;
		exp = _exp ;
	}
  
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
   		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
        errors.addAll(exp.checkSemantics(ST, _nesting)) ;

		if (ST.top_lookup(id))
        	errors.add(new SemanticError("Var id " + id + " already declared"));
        else if(exp.constValue(ST) == null)
			errors.add(new SemanticError("Const id " + id + " must be initialized with a constant value"));
		//no offest per constanti, quindi 0 --> il valore non è nella pila ma nella symboltable	
        else ST.insert(id, (Type) type,"", 0, exp.constValue(ST), _nesting) ;
 
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
		return "";
	}  
    
	public String toPrint(String s) {
		return s + "Var:" + id + type.toPrint(" ")  + exp.toPrint(s+" ") + "\t" ;
	}

	
}  