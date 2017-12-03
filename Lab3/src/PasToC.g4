grammar PasToC;

@header {
	import java.util.ArrayList;
    import java.util.List;
}
@parser::members {
    public class Pair<L,R> {
        private L l;
        private R r;
        public Pair(L l, R r){
            this.l = l;
            this.r = r;
        }
        public L getL(){ return l; }
        public R getR(){ return r; }
        public void setL(L l){ this.l = l; }
        public void setR(R r){ this.r = r; }
    }
	List<Pair<String, Character>> memory = new ArrayList<Pair<String, Character>>();
}

main:
	program_definition	{ System.out.println($program_definition.res.replaceAll("null", "")); };

const_block returns [String res]:
	(CONST_BLOCK_START (const_declaration { $res += $const_declaration.res  + "\n"; })+)?;

var_block returns [String res]:
	(VAR_BLOCK_START	(variable_declaration { $res += $variable_declaration.res + "\n"; })+)?;

program_definition returns [String res]:
	START_PROGRAM NAME ';' const_block var_block main_block	{
	$res = ("int main() {\n" + $const_block.res  + "\n" + $var_block.res  + "\n" + $main_block.res  + "\n" ); } ;

main_block returns [String res]:
	BLOCK_BEGIN ( stmt ({ $res += $stmt.res;}))+ END_OF_PROGRAM	{ $res += "}"; } EOF;

const_val returns [String res]:
	INT	{ $res = $INT.text; };

block returns [String res]:
	BLOCK_BEGIN { $res += "{\n"; } (stmt {  $res += $stmt.res;})+ BLOCK_END { $res += "\n}"; };

stmt returns [String res]:
	math_statement	{ $res = $math_statement.res + "\n"; }
	| assignment 	{ $res = $assignment.res + "\n"; }
	| function		{ $res = $function.res + "\n"; }
	| block			{ $res = $block.res + "\n"; };

math_value returns [String res]:
	const_val		{ $res = $const_val.res; }
	| NAME			{ $res = $NAME.text; };

math returns [String res]:
    math_value            { $res = $math_value.res; }
	| math_value '+' math	{ $res = $math_value.res + "+" + $math.res; }
	| math_value '-' math	{ $res = $math_value.res + "-" + $math.res; }
	| math_value '*' math	{ $res = $math_value.res + "*" + $math.res; }
	| math_value '/' math	{ $res = $math_value.res + "/" + $math.res; };

math_statement returns [String res]:
	math ';'				{ $res = $math.res + ";"; };

const_declaration returns [String res]:
	NAME '=' INT ';'	{ $res = "const int " + $NAME.text + "=" + $INT.text + ";";};

variable_declaration returns [String res]:
	NAME { $res += $NAME.text; } (',' NAME { $res += ", " + $NAME.text; })* ':' DEF_INTEGER   { $res = "int " + $res + ";" + "\n";};

assignment returns [String res]:
	NAME ASSIGNMENT math_statement	{ $res = $NAME.text + "=" + $math_statement.res + "\n"; };

func_parameter returns [String res]:
	const_string	{  memory.add(new Pair($const_string.res, 's')); } |
	math            {  memory.add(new Pair($math.res, 'v')); } ;

function returns [String res]:
	NAME '(' (func_parameter)* (',' func_parameter)* ')' ';'	{
                String str1 = $NAME.text;
	            if (str1.contains("writeln") || str1.contains("write")) {
                            String returnValue = "printf(\"";
                            String parameterListStr = "";
                            for (Pair<String, Character> i : memory) {
                                if (i.r == 's') {
                                    String k = i.l;
                                    returnValue += i.l.substring(1, k.length() - 1);
                                } else if (i.r == 'v') {
                                    returnValue += "%d";
                                    parameterListStr += ", ";
                                    parameterListStr += i.l;
                                }
                            }
                            if (str1.contains("writeln"))
                                returnValue += "\\n\"";
                            else
                                returnValue += "\"";
                            returnValue = returnValue + parameterListStr + ");";
                            memory.clear();
                            $res = returnValue;
                        } else if (str1.contains("readln") || str1.contains("read")) {
                            String returnValue = "scanf(\"";
                            String parameterListStr = "";

                            for (Pair<String, Character> i : memory) {
                                if (i.r == 'v') {
                                    // if (current -> next)
                                    returnValue += "%d ";
                                    //  else
                                    //    returnValue += "%d";
                                    parameterListStr = parameterListStr + ", &" + i.l;
                                }
                            }
                            returnValue += "\"";
                            returnValue = returnValue + parameterListStr + ");";
                            if (str1.contains("readln"))
                                returnValue += "fflush(stdin);";

                            memory.clear();
                            $res = returnValue;
                        } else
                            $res = str1;
	        };

const_string returns [String res]:
	CONST_STRING	{ $res = $CONST_STRING.text; };

NAME: (('a'..'z')|('A'..'Z'))((('a'..'z')|('A'..'Z')|('0'..'9')))*;
INT: '0' | [1-9] ([0-9])*;
EXP: [Ee] [+\-]? INT;
CONST_STRING : '\'' (('a'..'z')|('A'..'Z')|('0'..'9')|' '|'!'|';'|':'|'+'|'/'|'-')* '\'';

START_PROGRAM: 'program ';
WS: [ \t\r\n]+ -> skip;
END_OF_PROGRAM: 'end.';

BLOCK_BEGIN: 'begin\n';
BLOCK_END: 'end;\n';
DEF_INTEGER: 'integer;';
VAR_BLOCK_START: 'var\n' ;
CONST_BLOCK_START: 'const\n';
ASSIGNMENT: ':=';
EQUAL_SIGN: '=';
PERIOD: '.';
COMMA: ',';
COLON: ':';
SEMICOLON: ';';
DOUBLE_QUOTES: '"';


