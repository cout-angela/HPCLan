package ast;
import evaluator.HPCLanlib;
import java.util.ArrayList;
import java.util.HashMap;
import semanticanalysis.STentry;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class FunNode implements Node {
	private final String id;
	private final Type returntype ;
	private final ArrayList<ParNode> parlist ;
	private final ArrayList<Node> declist ;
	private final ArrayList<Node> stmList ;
	private final Node body ;
	private ArrowType type ;
	private String flabel ;
	private int nesting ;
  
	public FunNode (String _id, Type _type, ArrayList<ParNode> _parlist, ArrayList<Node> _declist, ArrayList<Node> _stmList, Node _body) {
		id = _id ;
		returntype = _type;
		parlist = _parlist ;
		declist = _declist ;
		stmList = _stmList ;
		body = _body ;
	}

	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {

		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();
		nesting = _nesting ;
		
		if (ST.top_lookup(id))
			errors.add(new SemanticError("Identifier " + id + " already declared"));
		else {
			HashMap<String,STentry> HM = new HashMap<String,STentry>() ;
			ArrayList<Type> partypes = new ArrayList<Type>() ;

			for (ParNode arg : parlist)
				partypes.add(arg.getType());

			type = new ArrowType(partypes, returntype) ;
			flabel = HPCLanlib.freshFunLabel() ;
			ST.insert(id, type, flabel, 0, null, nesting) ;

			ST.add(HM);
			for (ParNode arg : parlist){
				if (HM.containsKey(arg.getId()))
					errors.add(new SemanticError("Parameter id " + arg.getId() + " already declared")) ;
				else {
					ST.insert(arg.getId(), arg.getType(), "", 0, null, nesting+1) ;
				}
			}

			ST.increaseoffset() ; // aumentiamo di 1 l'offset per far posto al return value

			for (Node dec : declist)
				errors.addAll(dec.checkSemantics(ST, nesting+1));

			for (Node stm : stmList)
				errors.addAll(stm.checkSemantics(ST, nesting+1));

			errors.addAll(body.checkSemantics(ST, nesting+1));
			
			ST.remove();
		}
		return errors ; // problemi con la generazione di codice!
	}
  
 	public Type typeCheck () {
		if (declist!=null) 
			for (Node dec:declist)
				dec.typeCheck();
		if (stmList!=null) 
			for (Node stm:stmList)
				stm.typeCheck();
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
		String stmCode = "" ;
	    if (stmList.size() != 0) {
	    		for (Node stm:stmList){
	    			stmCode = stmCode + stm.codeGeneration();
	    		}
 	    }
	     
	    HPCLanlib.putCode(
	    		flabel + ":\n"
					+ "pushr RA \n"
					+ declCode
					+ stmCode
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
		if (parlist!=null) 
			for (Node par:parlist)
				parlstr += par.toPrint(s+"    ")+ "\n";


		String declstr= "";
		if (declist!=null) 
		  for (Node dec:declist)
		    declstr+=dec.toPrint(s+"    ") + "\n";

			
		String stmstr= "";
		if (stmList!=null)
		  for (Node stm:stmList)
		    stmstr+=stm.toPrint(s+"    ") + "\n";


	    return s+"Fun " + id +" -> " + returntype.toPrint(" ")
			   + "\n" + parlstr
		   	   + declstr
			   + stmstr
	           + body.toPrint(s+"    ") ;
	  }
	  
}  