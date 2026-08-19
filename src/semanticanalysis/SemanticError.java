package semanticanalysis;

public class SemanticError {
	private final String msg;
	
	public SemanticError(String _msg) {
		msg = _msg;
	}
	
	public String toString() {		
		return msg;
	}
}
