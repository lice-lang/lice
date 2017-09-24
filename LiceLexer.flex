/* JFlex example: partial Java language lexer specification */
import org.lice.internal.parser;
import org.lice.runtime.MetaData;

%%

%class Lexer
%public
%unicode
%cup
%line
%column

%function nextToken
%type Token

%{
    StringBuilder string = new StringBuilder();

    private Symbol identifier(String image) {
        return new Token(Token.Type.Identifier, yyline, yycolumn);
    }
    private Symbol string(String image) {
        return new Token(Token.Type.Identifier);
    }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

DecIntegerLiteral = 0 | [1-9][0-9]*

Identifier = [^ \t\f\r\n]+ 

%state STRING

%%

/* keywords */
<YYINITIAL> "abstract"           { return symbol(sym.ABSTRACT); }
<YYINITIAL> "boolean"            { return symbol(sym.BOOLEAN); }
<YYINITIAL> "break"              { return symbol(sym.BREAK); }

<YYINITIAL> {
    /* identifiers */ 
    {Identifier}                   { return symbol(sym.IDENTIFIER); }
     
    /* literals */
    {DecIntegerLiteral}            { return symbol(sym.INTEGER_LITERAL); }
    \"                             { string.setLength(0); yybegin(STRING); }

    /* whitespace */
    {WhiteSpace}                   { /* ignore */ }
}

<STRING> {
    \"                             { yybegin(YYINITIAL); 
                                       return symbol(sym.STRING_LITERAL, 
                                       string.toString()); }
    [^\n\r\"\\]+                   { string.append( yytext() ); }
    \\t                            { string.append("\\t"); }
    \\n                            { string.append("\\n"); }
    \\b                            { string.append("\\b"); }
    \\r                            { string.append("\\r"); }
    \\r                            { string.append("\\r"); }
    \\f                            { string.append("\\f"); }
    \\\"                           { string.append("\\\""); }
    \\                             { string.append("\\'"); }
}

    /* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                        yytext()+">"); }
