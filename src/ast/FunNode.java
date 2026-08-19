package ast;
import java.util.ArrayList;
import java.util.HashMap;

import evaluator.HPCLanlib;
import semanticanalysis.STentry;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class FunNode implements Node {
	private final String id;
	private final Type returntype ;
	private final ArrayList<ParNode> parlist ;
	private final ArrayList<Node> declist ;
	private final Node body ;
	private ArrowType type ;
	private int nesting ;
	private String flabel ;
  
	public FunNode (String _id, Type _type, ArrayList<ParNode> _parlist, ArrayList<Node> _declist, Node _body) {
		id = _id ;
		returntype = _type;
		parlist = _parlist ;
		declist = _declist ;
		body = _body ;
	}

	public ArrayList<SemanticError> checkSemantics(SymbolTable ST) {

		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		
		if (ST.top_lookup(id))
			errors.add(new SemanticError("Identifier " + id + " already declared"));
		else {
			HashMap<String,STentry> HM = new HashMap<String,STentry>() ;
			ArrayList<Type> partypes = new ArrayList<Type>() ;

			for (ParNode arg : parlist)
				partypes.add(arg.getType());

			type = new ArrowType(partypes, returntype) ;
			flabel = HPCLanlib.freshFunLabel() ;
			ST.insert(id, type, flabel) ;

			ST.add(HM);
			for (ParNode arg : parlist){
				if (HM.containsKey(arg.getId()))
					errors.add(new SemanticError("Parameter id " + arg.getId() + " already declared")) ;
				else {
					ST.insert(arg.getId(), arg.getType(), "") ;
				}
			}

			ST.increaseoffset() ; // aumentiamo di 1 l'offset per far posto al return value

			for (Node dec : declist)
				errors.addAll(dec.checkSemantics(ST));

			errors.addAll(body.checkSemantics(ST));
			ST.remove();

			/*
			HashMap<String,STentry> HM = new HashMap<String,STentry>() ;
			ArrayList<Type> partypes = new ArrayList<Type>() ;

			ST.add(HM);

			for (ParNode arg : parlist){
    	  			partypes.add(arg.getType());
    	  			if (ST.top_lookup(arg.getId()))
    	  					errors.add(new SemanticError("Parameter id " + arg.getId() + " already declared")) ;
    	  			else ST.insert(arg.getId(), arg.getType(), nesting+1, "") ;
    	  		}

			type = new ArrowType(partypes, returntype) ;

			ST.increaseoffset() ; // aumentiamo di 1 l'offset per far posto al return value
			for (Node dec : declist)
  				errors.addAll(dec.checkSemantics(ST, nesting+1));
			
			errors.addAll(body.checkSemantics(ST, nesting+1));
			ST.remove();
			
			flabel = HPCLanlib.freshFunLabel() ;
			
			ST.insert(id, type, nesting, flabel) ;

			 */
		}
		return errors ; // problemi con la generazione di codice!
	}
  
 	public Type typeCheck () {
		if (declist!=null) 
			for (Node dec:declist)
				dec.typeCheck();
		if ( (body.typeCheck()).getClass().equals(returntype.getClass())) 
    			return null ;
		else {
			System.out.println("Wrong return type for function "+id);
			return new ErrorType() ;
		}  
  	}
  
  public String codeGeneration() {
	  
	    String declCode = "" ;
	    if (declist.size() != 0) {
	    		for (Node dec:declist){
	    			declCode = declCode + dec.codeGeneration();
	    		}
 	    }
	     
	    HPCLanlib.putCode(
	    			flabel + ":\n"
	    			+ "pushr RA \n"
	    			+ declCode
	    			+ body.codeGeneration()
	    			+ "addi SP " + 	declist.size() + "\n"
	    			+ "popr RA \n"
	    			+ "addi SP " + 	parlist.size() + "\n" // pop di tutti i parametri
				    + "pop \n"
					+ "store FP 0(FP) \n"
					+ "move FP AL \n"
					+ "subi AL 1 \n"
					+ "pop \n"
	    			+ "rsub RA \n" 
	    		);
	    
		return "push "+ flabel +"\n"; // e` lo stesso che scrivere "push 0 \n" : non ci accede mai
  }
  
  public String toPrint(String s) {
		String parlstr="";
		for (Node par:parlist){
		  parlstr += par.toPrint(s);
		}
		String declstr= "";
		if (declist!=null) 
		  for (Node dec:declist)
		    declstr+=dec.toPrint(s+" ");
	    return s+"Fun " + id +": " + returntype.toPrint(" ") + "\n\t"
			   +parlstr + "\n\t"
		   	   +declstr
		   	   + "\n"
	           +body.toPrint(s+"  ") ;
	  }
	  
}  