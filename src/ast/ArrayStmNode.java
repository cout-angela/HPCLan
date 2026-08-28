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
	//TODO(): da completare
	
	public ArrayStmNode(String _id, Node _exp, Node _index) {
		id = _id ;
		exp = _exp ;
		index = _index ;
	}
  
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
   		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		nesting = _nesting;
        errors.addAll(exp.checkSemantics(ST, _nesting));
        errors.addAll(index.checkSemantics(ST, _nesting));

        st = ST.lookup(id) ;
        if (st==null)
        	errors.add(new SemanticError("Var id " + id + " not declared"));

		else if (st.getdim() == 0)
        	errors.add(new SemanticError("Var id " + id + " is not an Array"));

		
        return errors ;

		

	}
  
	public Type typeCheck () {
		if (!exp.typeCheck().getClass().equals(st.gettype().getClass())){
				System.out.println("Type Error: incompatible type of expression for array type "+id) ;
				return new ErrorType() ;
		}else if(!index.typeCheck().getClass().equals(IntType.class)){
				System.out.println("Type Error: index of array "+id+" must be an integer") ;
				return new ErrorType() ;
		}
		return null;  
	}
   
	public String codeGeneration() {
		String getAR="";
		for (int i=0; i < st.getnesting() - nesting; i++) 
			getAR += "store T1 0(T1) \n";
		
		return exp.codeGeneration() +
				"move AL T1 \n" +
				getAR + //risalgo la catena statica
				"subi T1 " + st.getoffset() +"\n" + //
				"load A0 " + st.getoffset() + "(T1) \n" ;
	}  
    
	public String toPrint(String s) {
		return s + "Asg:" + id + st.gettype().toPrint(" ")  + exp.toPrint(s+" ") + "\t" ;
	}

}  