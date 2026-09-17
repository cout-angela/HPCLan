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
	private final String i ;
	private Node iNode ;

	private final Node n;
    private final Node RESstm;
    private final String RESid;
	private Integer RESdim;

	private STentry indexArray;
  
	public MapredStmNode (String _i, Node _n, Node _RESstm, String _RESid) {
    	i = _i ;
    	n = _n;
        RESstm = _RESstm;
        RESid = _RESid;
	}
  
	@Override
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();

        if (ST.top_lookup(i))
			errors.add(new SemanticError("Identifier " + i + " already declared"));
		else {
			HashMap<String,STentry> HM = new HashMap<String,STentry>() ;

			ST.add(HM);
			ST.insert(i, new IntType(), "", 0, null, _nesting) ;

			iNode = new IdNode(i) ;
			iNode.checkSemantics(ST, _nesting) ; // check semantics for the index variable
			
			ST.insert("mapred", new IntType(), "", RESdim, null, _nesting) ;
			indexArray = ST.lookup("mapred");
			
			//id = ST.top_lookup(index);
			
			errors.addAll(n.checkSemantics(ST, _nesting));
			
			if(ST.lookup(RESid) != null){
				RESdim = ST.lookup(RESid).getdim();
			}

			errors.addAll(RESstm.checkSemantics(ST, _nesting));
			
			ST.remove();
		}


		return errors;
	}
  
	public Type typeCheck() {
		if (n.typeCheck() instanceof IntType) {
		  	return RESstm.typeCheck();
		} else {
			System.out.println("Type Error: type mismatch for mapred statement, expected int for n") ;
			return new ErrorType() ;
		}
	}
  

    /* 
    index = lista di indici tra 0 e dim-1  			--> dichiarazione + assegnamento array
    Collections.shuffle(index) 
    int i = index.pop; 								--> dichiazione + assegnamento  variabile i
    while(i < n)   						   			--> accesso a variabili i e n
        if n > index[i]								--> accesso array di supporto index 
           RES[j] = EXP  							--> assegnamento array RES 
        i = index.pop								--> assegnamento variabile i [3, 0, 2, 1] = 2
    }
	*/

	
  	public String codeGeneration() {
  		String whileCont = HPCLanlib.freshLabel(); 
  		String whileEnd = HPCLanlib.freshLabel();
		String lthen = HPCLanlib.freshLabel(); 
  		String lend = HPCLanlib.freshLabel();
		Integer i = 0;

        List<Integer> range = IntStream.range(0, RESdim - 1).boxed().collect(Collectors.toList());
		Collections.shuffle(range);

		String storeIndexArray = "";
	
		for(int h=0; h < range.size(); h++){
			storeIndexArray += "storei A0 " + range.get(h) + "\n" 
								+ "load A0 " + (indexArray.getoffset()+h) + "(FP) \n" ;
		}

		String getAR="";
		for (int i=0; i < st.getnesting() - nesting; i++) 
			getAR += "store T1 0(T1) \n";
		

		// formato AR: control_link + parameters + indirizzo di ritorno + dich_locali

		return  
		//INIZIALIZZIONE VARIABILE i		
				//+ "pushr RA \n"
				// mettere dentro i.offset n/2
				n.codeGeneration()
				+ "move AL T1 \n" 
				+ getAR  //risalgo la catena statica
				+ "subi T1 " + iNode.getoffset() +"\n"  //
				+ "load A0 " + iNode.getoffset() + "(T1) \n"
				
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