package ast;
import evaluator.HPCLanlib;
import java.util.ArrayList;
import java.util.HashMap;

import semanticanalysis.STentry;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class MapredStmNode implements Node {
	private final String i ;
	private Node iNode ;

	private final Node n;
    private final Node RESstm;
	private Integer nesting;
	private STentry iEntry;
  
	public MapredStmNode (String _i, Node _n, Node _RESstm) {
    	i = _i ;
    	n = _n;
        RESstm = _RESstm;
	}
  
	@Override
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
		ArrayList<SemanticError> errors = new ArrayList<SemanticError>();

        if (ST.lookup(i) != null)
			errors.add(new SemanticError("Identifier " + i + " already declared"));
		else {
			HashMap<String,STentry> HM = new HashMap<String,STentry>() ;
			nesting = _nesting+1;
			ST.add(HM);
			ST.insert(i, new IntType(), "", 0, null, _nesting + 1) ;

			iEntry = ST.lookup(i) ;
			
			errors.addAll(n.checkSemantics(ST, _nesting + 1 ));
			
			errors.addAll(RESstm.checkSemantics(ST, _nesting + 1));
			
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


	/* 
	
		int i = n/2
		while (i >= 0) {   // i < 0
			RES[j] = e
			i = i - 1
		}

		i = n/2 + 1
		while (i < n) {   // n <= i
			RES[j] = e
			i = i + 1
		}
	*/

	
  	public String codeGeneration() {
  		String whileCont = HPCLanlib.freshLabel(); 
  		String whileEnd = HPCLanlib.freshLabel();
		String while2Cont = HPCLanlib.freshLabel(); 
  		String while2End = HPCLanlib.freshLabel();
		String getAR = "move AL T1 \n";
		
		for (int i = 0; i < nesting - iEntry.getnesting(); i++)
			getAR += "store T1 0(T1) \n";
			
		getAR += "subi T1 " + iEntry.getoffset() +"\n";


		// formato AR: control_link + parameters + indirizzo di ritorno + dich_locali

		return  

		// CREAZIONE AR
				"pushr FP \n"			// carico il frame pointer; decrementa SP a causa della pushr
			
				//+ "move AL T1\n"		// risalgo la catena statica
				//+ getAR
				//+ "pushr T1 \n"			// salvo sulla pila l'access link statico: si trovera` sempre a FP-1
				+ "pushr AL \n"
				+ "move SP FP \n"
				+ "addi FP 2\n"				// memorizzo in FP il valore SP - parameters.size() - 1
				+ "move FP AL \n"		// memorizzo in AL l'indirizzo della catena statica che e` FP-1
				+ "subi AL 1 \n"

		//INIZIALIZZIONE VARIABILE i		
				//+ "pushr RA \n"
				// mettere dentro i.offset n/2
				+ n.codeGeneration()
				+ "divi A0 2 \n"
				+ "pushr A0 \n"
				
				
		// WHILE 1	
				+ "b " + whileCont + "\n"
				+ whileCont + ":\n"
				+ "storei T1 0 \n"
				+ "blt A0 T1 "+ whileEnd + "\n"
				+ RESstm.codeGeneration()
				+ getAR
				+ "store A0 0(T1) \n"
				+ "subi A0 1 \n" 
				+ getAR
				+ "load A0 0(T1)\n"
				+ "b " + whileCont + "\n"
				
				+ whileEnd + ":\n"


		//WHILE 2
				//INIZIALIZZIONE VARIABILE i		
				//+ "pushr RA \n"
				// mettere dentro i.offset n/2
				+ n.codeGeneration()
				+ "divi A0 2 \n"
				+ "addi A0 1 \n"
				+ getAR
				+ "load A0 0(T1) \n"

				+ "b " + while2Cont + "\n"
				+ while2Cont + ":\n"
				+ n.codeGeneration()
				+ "store T1 0(T1) \n"
				+ "bleq A0 T1 "+ while2End + "\n"
				+ RESstm.codeGeneration()
				+ getAR
				+ "store A0 0(T1) \n"
				+ "addi A0 1 \n"
				+ getAR
				+ "load A0 0(T1) \n"
				+ "b " + while2Cont + "\n"
				
				+ while2End + ":\n"


		//RIMOZIONE AR
				+ "addi SP 1 \n" // rimuove la viariabile i
				+ "pop \n" //rimozione AL
				+ "store FP 0(FP) \n"
				+ "move FP AL \n"
				+ "subi AL 1 \n"
				+ "pop \n"; // rimozione control link
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
 	    
	    return s+"Mapred:\n"
			+ s + "    " + i
			+ " upto " + n.toPrint("")
			+ "\n" + RESstm.toPrint(s+"    ");
	}
	  
} 