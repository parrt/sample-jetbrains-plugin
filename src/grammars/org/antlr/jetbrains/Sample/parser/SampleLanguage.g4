grammar SampleLanguage;

script
	:	statement+
	;

statement
	:   vardef
	|	'print' expr
	;

vardef : 'var' ID '=' expr ;

expr : INT ;

ID  : [a-zA-Z_] [a-zA-Z0-9_]* ;
INT : [0-9]+ ;

ERRCHAR
	:	.	-> channel(HIDDEN)
	;
