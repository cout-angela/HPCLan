package ast;
import java.util.ArrayList;

import semanticanalysis.STentry;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class CallNode implements Node {
	private final String id ;
	private STentry entry ; 
	private final ArrayList<Node> parameters ;
	private int nesting ;

  	public CallNode(String _id, ArrayList<Node> _parameters) {
		id = _id;
		parameters = _parameters ;
	}

	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		nesting = _nesting ;
		STentry tmp = ST.lookup(id) ;
		if (tmp != null) {
			entry = tmp ;
			for (Node par : parameters)
				errors.addAll(par.checkSemantics(ST, nesting));
		} else {
			errors.add(new SemanticError("Id " + id + " not declared")) ;
		}
		return errors;
  	}
  
	public Type typeCheck() {                            
		Type _type = entry.gettype() ;
		if (_type instanceof ArrowType) {			 
			ArrayList<Type> _partype = ((ArrowType) _type).get_inputtype();
			if ( _partype.size() != parameters.size() ) {
				System.out.println("Wrong number of parameters in the invocation of "+id);
				return new ErrorType() ;
			} else {
				boolean ok = true ;
				for (int i = 0 ; i < parameters.size() ; i++) {
					Type par_i = (parameters.get(i)).typeCheck() ;
					if ( !(par_i.getClass().equals(_partype.get(i).getClass()) )) {
							System.out.println("Wrong type for "+(i+1)+"-th parameter in the invocation of "+id);
							ok = false ;
					} 
				}
				if (ok) return ((ArrowType) _type).get_outputtype() ;
				else return new ErrorType() ;
			} 
		} else {
				System.out.println("Invocation of a non-function "+id) ;
				return new ErrorType() ;
		}
	}
  
  public String codeGeneration() {
	    String parCode="";
	    for (int i = 0; i < parameters.size() ; i = i+1)
	    		parCode += parameters.get(i).codeGeneration() + "pushr A0\n" ;

		String updateFP = "addi FP " + parameters.size() + "\n" + "addi FP 2\n";

		String getAR="";
		for (int i=0; i < nesting - entry.getnesting() ; i++) 
		    	getAR+="store T1 0(T1) \n";

		// formato AR: control_link + parameters + indirizzo di ritorno + dich_locali

		return  "pushr FP \n"			// carico il frame pointer; decrementa SP a causa della pushr
				+ "move AL T1\n"		// risalgo la catena statica
				+ getAR
				+ "pushr T1 \n"			// salvo sulla pila l'access link statico: si trovera` sempre a FP-1
				+ parCode 				// calcolo i parametri attuali con l'access link del chiamante
				+ "move SP FP \n"
				+ updateFP				// memorizzo in FP il valore SP - parameters.size() - 1
				+ "move FP AL \n"		// memorizzo in AL l'indirizzo della catena statica che e` FP-1
				+ "subi AL 1 \n"
				+ "jsub " + entry.getlabel() + "\n" ;
  }

	public String toPrint(String s) {
	    String parlstr="";
		for (Node par : parameters)
			parlstr += par.toPrint(s+"\t") ;

		return s+"Call:" + id + "\n" + parlstr ;
	}
	
	 
}  