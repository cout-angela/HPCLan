package ast;
import evaluator.HPCLanlib;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import semanticanalysis.STentry;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class MapredStmNode implements Node {
	private final String index ;
	private final Node n;
    private final Node arrayStm;
    private final String arrayId;
	private Integer arrayDim;
  
	public MapredStmNode (String _index, Node _n, Node _arrayStm, String _arrayId) {
    	index = _index ;
    	n = _n;
        arrayStm = _arrayStm;
        arrayId = _arrayId;
	}
  
	@Override
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();

        if (ST.top_lookup(index))
			errors.add(new SemanticError("Identifier " + index + " already declared"));
		else {
			HashMap<String,STentry> HM = new HashMap<String,STentry>() ;

			ST.add(HM);
			ST.insert(index, new IntType(), "", 0, null, _nesting + 1) ;
			
			errors.addAll(n.checkSemantics(ST, _nesting + 1));
			
			if(ST.lookup(arrayId) != null){
				arrayDim = ST.lookup(arrayId).getdim();
			}

			errors.addAll(arrayStm.checkSemantics(ST, _nesting + 1));
			
			ST.remove();
		}


		return errors;
	}
  
	public Type typeCheck() {
		if (n.typeCheck() instanceof IntType) {
		  	return arrayStm.typeCheck();
		} else {
			System.out.println("Type Error: type mismatch for mapred statement, expected int for n") ;
			return new ErrorType() ;
		}
	}
  

    /* 
    index = lista di indici tra 0 e dim-1
    Collections.shuffle(index)
    int i = 0;
    while(i < n)
        if n > index[i]
           RES[index[i]] 
        i++
    }
*/
  	public String codeGeneration() {
  		String whileCont = HPCLanlib.freshLabel(); 
  		String whileEnd = HPCLanlib.freshLabel();
		String lthen = HPCLanlib.freshLabel(); 
  		String lend = HPCLanlib.freshLabel();
		Integer i = 0;

        List<Integer> range = IntStream.range(0, arrayDim - 1).boxed().collect(Collectors.toList());
		Collections.shuffle(range);

		get
		
		

		// formato AR: control_link + parameters + indirizzo di ritorno + dich_locali

		return  
		// CREAZIONE AR
				"pushr FP \n"			// carico il frame pointer; decrementa SP a causa della pushr
			
				//+ "move AL T1\n"		// risalgo la catena statica
				//+ getAR
				//+ "pushr T1 \n"			// salvo sulla pila l'access link statico: si trovera` sempre a FP-1
				+ "pushr AL"
				+ "move SP FP \n"
				+ "addi FP 2\n"				// memorizzo in FP il valore SP - parameters.size() - 1
				+ "move FP AL \n"		// memorizzo in AL l'indirizzo della catena statica che e` FP-1
				+ "subi AL 1 \n"

		//DICHIARAZIONE VARIABILE i		
				//+ "pushr RA \n"
				+ "storei A0 0 \n"  					//dichiarazione i = 0
				+ "pushr A0"
				
		//INIZIO WHILE (CONDIZIONE)	
				+ "b " + whileCont + "\n"
				+ whileCont + ":\n"
				+ "move AL T1 \n" 
				+ "subi T1 " + 1 +"\n" //metto offset sullo stack (1 perchè i è l'unica variabile salvata, e se non lo fosse è comunque la prima)
				+ "store A0 0(T1) \n"  //carico sullo stack il valore all'indirizzo ottenuto 
				+ "store T1 A0 \n" 
				+ n.codeGeneration()
				+ "bleq A0 T1 "+ whileEnd + "\n"


		//CORPO WHILE (IF)

				+ n.codeGeneration()

				
				+ left.codeGeneration()
                + "pushr A0 \n" +
                + right.codeGeneration()+
                + "popr T1 \n" +
                + "blt A0 T1 "+ ltrue +"\n"+
                + "storei A0 0\n"+
                + "b " + lend + "\n" +
                + ltrue + ":\n"+
                + "storei A0 1\n" +
                + lend + ":\n";
				
				
				+ "storei T1 1 \n"
				+ "beq A0 T1 "+ lthen + "\n"
				+ "b " + lend + "\n"
				+ lthen + ":\n"
				+ thenStmCode
				+ lend + ":\n"



				+ "addi SP " + 	declist.size() + "\n"
				+ "popr RA \n"
				+ "addi SP " + 	parlist.size() + "\n" // pop di tutti i parametri
				+ "pop \n"
				+ "store FP 0(FP) \n"
				+ "move FP AL \n"
				+ "subi AL 1 \n"
				+ "pop \n"
				//+ "rsub RA \n";
		
  		return
			
			"b " + whileCont + "\n" +
			whileCont + ":\n" +
				n.codeGeneration() +
				"bleq A0 T1 "+ whileEnd + "\n" +
				stmCode +
				"b " + whileCont + "\n" +
	        whileEnd + ":\n" ; 
  	}
    /* 

    
    RES.dim = 4
    n = 2
    index= [0, 1]
    index =  [0, 1, 2, 3]
    index.shuffle = [3, 0, 2, 1]
    
    RES[3] 


    RES.dim = 4
    n = 7
    index= [0, 1]
    index =  [0, 1, 2, 3]
    index.shuffle = [3, 0, 2, 1]
    
    while(i < n)
        if n > index[i]
           RES[index[i]] 
        i++
    
    */

  	public String toPrint(String s) {
		String stmStr = "" ;
	    if (stmList.size() != 0) {
	    		for (Node stm:stmList){
	    			stmStr = stmStr + stm.toPrint(s+"  ");
	    		}
 	    }
	    return
					s+"While\n"
							+ cond.toPrint(s+"  ")
							+ stmStr ;
	}
	  
} 