package evaluator;

public class HPCLanlib {
	private static int labCount=0; 
	private static int funLabCount=0; 
	private static String funCode=""; 
  
	public static String freshLabel() { 
		return "label"+(labCount++);
	} 

	public static String freshFunLabel() { 
		return "function"+(funLabCount++);
	} 

	/*public static String getBoundsErrorLabel() { 
		return "boundsError";
	}*/
  
	public static void putCode(String c) { 
		funCode+="\n"+c; //aggiunge una linea vuota di separazione prima di funzione
	} 
  
	public static String getCode() { 
		return funCode;
	} 
}