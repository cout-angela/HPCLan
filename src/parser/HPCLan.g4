grammar HPCLan ;

@lexer::members {
   //there is a much better way to do this, check the ANTLR guide
   public int lexicalErrors=0;
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

prog   : dec* stm* exp ';'
       ;

simpledec   : type ('const')? ID '=' exp ';'                                     #idDec
            | type ID '[' (INT | ID) ']' ';'                                     #arrayDec
            ;

dec    : simpledec                                                               #simpleDec
       | type ID '(' ( param ( ',' param)* )? ')' '{' simpledec* stm* exp '}'    #funDec
       ;

param  : type ID ;

type   : 'int'
       | 'bool'
       ;

stm    : ID '=' exp ';'
       | ID '[' exp ']' '=' exp ';'
       | 'if' '(' exp ')' '{' stm* '}' ('else' '{' stm* '}')?
       | 'while' '(' exp ')' '{' stm+ '}'
       | 'mapred' '(' ID 'upto' (INT | ID) ':' ID '[' exp ']' '=' exp ')'
       ;

exp    :  left=exp op=('*' | '/') right=exp
       | left=exp op=('+' | '-') right=exp
       | left=exp op=('==' | '>=' | '<=' | '>' | '<' | '!=') right=exp
       | left=exp op=('&&' | '||') right=exp
       | value
       ;

value  :  op=('+' | '-' | '!') value            #signedVal
       | '(' exp ')'                            #baseExp
       | 'if' cond=exp '{' thenBranch= stm* exp '}' 'else' '{' elseBranch= stm* exp '}'  #ifExp
       | ID '(' (exp (',' exp)* )? ')'          #funExp
       | ID '[' exp ']'                         #arrayExp
       | ID                                     #varExp
       | INT                                    #intVal
       | BOOL                                   #boolVal
       ;

/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

BOOL            : 'true' | 'false'          ;
INT             : '0' | [1-9][0-9]*         ;
ID              : [a-zA-Z] [a-zA-Z0-9_]*    ;

//ESCAPE SEQUENCES
WS              : (' '|'\t'|'\n'|'\r')-> skip;
LINECOMENTS    : '//' (~('\n'|'\r'))* -> skip;
BLOCKCOMENTS    : '/*'( ~('/'|'*')|'/'~'*'|'*'~'/')* '*/' -> skip;

 //VERY SIMPLISTIC ERROR CHECK FOR THE LEXING PROCESS, THE OUTPUT GOES DIRECTLY TO THE TERMINAL
 //THIS IS WRONG!!!!
ERR     : . { System.out.println("Invalid char: "+ getText()); lexicalErrors++; } -> channel(HIDDEN);
