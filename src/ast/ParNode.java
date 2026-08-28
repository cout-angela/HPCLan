package ast;

import java.util.ArrayList;

import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

public class ParNode implements Node {

  private final String id;
  private final Type type;
  
  public ParNode (String _id, Type _type) {
   id = _id ;
   type = _type ;
  }
  
  public String getId(){
	  return id;
  }
  
  public Type getType(){
	  return type;
  }
  
  @Override
	public ArrayList<SemanticError> checkSemantics(SymbolTable ST, int _nesting) {
	  return new ArrayList<SemanticError>();
	}
  
   //non utilizzato
  public Type typeCheck () {
     return null;
  }
  
  //non utilizzato
  public String codeGeneration() {
		return "";
  }
  
  public String toPrint(String s) {
	  return s+"FPar " + id + ":" + type.toPrint(s) ;
  }
  

    
}  