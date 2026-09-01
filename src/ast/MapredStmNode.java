package ast;
import evaluator.HPCLanlib;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import semanticanalysis.STentry;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class MapredStmNode implements Node {
	private final String index ;
	private final Node n;
    private final Node arrayStm;
    private final String arrayId;
  
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
    index = lista di indici tra 0 e n-1
    Collection.shuffle(index)
    int i = 0;
    while(i < n){
        RES[index[i]] = exp;
        i += 1
    }
*/
  	public String codeGeneration() {
  		String whileCont = HPCLanlib.freshLabel(); 
  		String whileEnd = HPCLanlib.freshLabel();

        //List<Integer> range = IntStream.range(1, 501).boxed().collect(Collectors.toList());


		String stmCode = "" ;
	    if (stmList.size() != 0) {
	    	for (Node stm:stmList){
	    		stmCode = stmCode + stm.codeGeneration();
			}
 	    }
  		return 
			"b " + whileCont + "\n" +
			whileCont + ":\n" +
				cond.codeGeneration() +
				"storei T1 0 \n" +
				"beq A0 T1 "+ whileEnd + "\n" +
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