package ast;

import java.util.ArrayList;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;
import semanticanalysis.STentry;
import evaluator.HPCLanlib;

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
   
/* 	public String codeGeneration() {
		String getAR="";
		for (int i=0; i < st.getnesting() - nesting; i++) 
			getAR += "store T1 0(T1) \n";

			//valutare exp dell'indice (index) -> risultato in A0
			//mettere in T1 0
			//valutare A0 < T1 -> label errore
			//mettere in T1 st.getDim 
			//valutare T1 <= A0 -> label errore
			//accesso alla variabile offset + index

			
		
		return exp.codeGeneration() +
				"move AL T1 \n" +
				getAR + //risalgo la catena statica
				"subi T1 " + st.getoffset() +"\n" + //
				"load A0 " + st.getoffset() + "(T1) \n" ;
	}   */

	public String codeGeneration() {
		//TODO(): da capire se gestione errore OutOfBounds è corretto (si fa qui? da un'altra parte? è corretto con la label??)
		String err = HPCLanlib.getBoundsErrorLabel();

		String getAR = "";
		for (int i = 0; i < nesting - st.getnesting(); i++)
			getAR += "store T1 0(T1) \n";

		return
			// 1. indice -> A0
			index.codeGeneration()

			// 2. indice < 0
			+ "storei T1 0 \n"
			+ "blt A0 T1 " + err + "\n"

			// 3. indice >= dim
			+ "storei T1 " + st.getdim() + "\n"
			+ "bleq T1 A0 " + err + "\n"

			// 4. indirizzo dell'elemento: base - indice.
			+ "move AL T1 \n"
			+ getAR
			+ "subi T1 " + st.getoffset() + "\n"
			+ "sub T1 A0 \n"

			// 5. valore da assegnare
			+ exp.codeGeneration()

			// 6. recupera l'indirizzo e scrivi
			+ "popr T1 \n"
			+ "load A0 0(T1) \n";
	}
    
	public String toPrint(String s) {
		return s + "Asg Array:" + id + st.gettype().toPrint(" ")  + exp.toPrint(s+" ") + "\t" ;
	}

}  