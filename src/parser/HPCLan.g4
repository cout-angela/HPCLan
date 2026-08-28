grammar HPCLan;

@lexer::members {
   //there is a much better way to do this, check the ANTLR guide
   public int lexicalErrors=0;
}

/*------------------------------------------------------------------
 * PARSER RULES
 ------------------------------------------------------------------
 */

prog: dec* stm* exp ';'; //-> ProgNode

simpledec:
	type c = 'const'? ID '=' exp ';'	# idDec //-> DecNode
	| type ID '[' (INT | ID) ']' ';'	# arrayDec;

dec:
	simpledec																# simpleDec
	| type ID '(' (param ( ',' param)*)? ')' '{' simpledec* stm* exp '}'	# funDec; //-> FunNode

param:
	type ID; //->ParNode (? usati per passaggio di parametri fun - per getType e getId)

type:
	'int' //-> IntType
	| 'bool'; //-> BoolType

stm:
	ID '=' exp ';' 														# asgStm
	| ID '[' exp ']' '=' exp ';' 										# arrayStm
	| 'if' '(' exp ')' '{' stm* '}' ('else' '{' stm* '}')?  			# ifStm//-> IfStmNode // ? = 0 o 1 
	| 'while' '(' exp ')' '{' stm+ '}' 									# whileStm
	| 'mapred' '(' ID 'upto' (INT | ID) ':' ID '[' exp ']' '=' exp ')'  # mapredStm
	; 					

exp:
	left = exp op = ('*' | '/') right = exp //-> DivNode, MultNode
	| left = exp op = ('+' | '-') right = exp //-> MinusNode, PlusNode
	| left = exp op = ('==' | '>=' | '<=' | '>' | '<' | '!=') right = exp
	//EqualNode, GeqNode, GtNode, LeqNode, LtNode, UnEqualNode
	| left = exp op = ('&&' | '||') right = exp //->AndNode, OrNode
	| value;

value:
	op = ('+' | '-' | '!') value															# signedVal //-> NotNode, UMinusNode, NotNode
	| '(' exp ')'																			# baseExp
	| 'if' cond = exp '{' thenBranch = stm* exp '}' 'else' '{' elseBranch = stm* exp '}'	# ifExp
	//-> IfExpNode
	| ID '(' (exp (',' exp)*)? ')'	# funExp //-> CallNode
	| ID '[' exp ']'				# arrayExp
	| ID							# varExp //->IdNode
	| INT							# intVal //->IntNode
	| BOOL							# boolVal; //->BoolNode

/*------------------------------------------------------------------
 * LEXER RULES
 ------------------------------------------------------------------
 */

BOOL: 'true' | 'false';
INT: '0' | [1-9][0-9]*;
ID: [a-zA-Z] [a-zA-Z0-9_]*;

//ESCAPE SEQUENCES
WS: (' ' | '\t' | '\n' | '\r') -> skip;
LINECOMENTS: '//' (~('\n' | '\r'))* -> skip;
BLOCKCOMENTS:
	'/*' (~('/' | '*') | '/' ~'*' | '*' ~'/')* '*/' -> skip;

//VERY SIMPLISTIC ERROR CHECK FOR THE LEXING PROCESS, THE OUTPUT GOES DIRECTLY TO THE TERMINAL THIS
// IS WRONG!!!!
ERR:
	. { System.out.println("Invalid char: "+ getText()); lexicalErrors++; } -> channel(HIDDEN);