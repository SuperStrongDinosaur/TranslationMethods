package main

import java.io.ByteArrayInputStream
import java.text.ParseException

class Parser @Throws(ParseException::class) constructor(s: String) {

    private val lex: LexicalAnalyzer = LexicalAnalyzer(ByteArrayInputStream(s.toByteArray()))

    init {
        lex.nextToken()
    }

    @Throws(ParseException::class)
    fun S(): Tree {
        //System.out.println("Parser.S " + lex.getCurToken());
        when (lex.curToken) {
            Token.F, Token.P -> {
                val v = V()
                val name = WORD()
                lex.nextToken()
                val varGroups = VARGROUPS()
                lex.nextToken()
                val typeoffunc = WORD()
                return Tree("S", v, name, LEFT_BRACKET_TREE, varGroups, typeoffunc)
            }
            else -> throw AssertionError()
        }
    }

    @Throws(ParseException::class)
    private fun SkipWord(s : String) {
        for (i in s) {
            assert(Token.valueOf(i.toString().toUpperCase()) != lex.nextToken())
        }
        lex.nextToken()
    }

    @Throws(ParseException::class)
    private fun V(): Tree {
        //System.out.println("Parser.V " + lex.getCurToken());
        return when (lex.curToken) {
            Token.F -> {
                SkipWord("unction")
                Tree("V", Tree("function"))
            }
            Token.P -> {
                SkipWord("rocedure")
                Tree("V", Tree("procedure"))
            }
            else -> throw AssertionError()
        }
    }

    @Throws(ParseException::class)
    private fun VARGROUPS(): Tree {
        //System.out.println("Parser.VARGROUPS " + lex.getCurToken());
        return when (lex.curToken) {
            Token.RIGHT_BRACKET -> {
                lex.nextToken()
                Tree("VARGROUPS", RIGHT_BRACKET_TREE)
            }
            Token.SEMICOLON -> {
                lex.nextToken()
                val next = VARGROUPS()
                Tree("VARGROUPS", SEMICOLON_TREE, next)
            }
            Token.F, Token.U, Token.N, Token.C, Token.R, Token.T, Token.I, Token.O, Token.P, Token.E, Token.D, Token.LETTER -> {
                val varlist = VARLIST()
                assert(lex.curToken === Token.COLON)
                lex.nextToken()
                val type = WORD()
                val vargroups = VARGROUPS()
                Tree("VARGROUPS", varlist, COLON_TREE, type, vargroups)
            }
            else -> throw AssertionError()
        }
    }

    /*@Throws(ParseException::class)
    private fun VARNEXT(): Tree {
        return when (lex.curToken) {
            Token.RIGHT_BRACKET -> {
                lex.nextToken()
                Tree("VARNEXT")
            }
            Token.SEMICOLON -> {
                lex.nextToken()
                val next = VARGROUPS()
                Tree("VARNEXT", SEMICOLON_TREE, next)
            }
            else -> throw AssertionError()
        }
    }*/

    @Throws(ParseException::class)
    private fun VARLIST(): Tree {
        //System.out.println("Parser.VARLIST " + lex.getCurToken());
        return when (lex.curToken) {
            Token.F, Token.U, Token.N, Token.C, Token.R, Token.T, Token.I, Token.O, Token.P, Token.E, Token.D, Token.LETTER -> {
                val word = WORD()
                val varlistprime = VARLISTPRIME()
                Tree("VARLIST", word, varlistprime)
            }
            else -> throw AssertionError()
        }
    }

    @Throws(ParseException::class)
    private fun VARLISTPRIME(): Tree {
        //System.out.println("Parser.VARLISTPRIME " + lex.getCurToken());
        return when (lex.curToken) {
            Token.COMMA -> {
                lex.nextToken()
                val word = WORD()
                val varlistprime = VARLISTPRIME()
                Tree("VARLISTPRIME", COMMA_TREE, word, varlistprime)
            }
            else -> Tree("VARLISTPRIME")
        }
    }

    @Throws(ParseException::class)
    private fun WORD(): Tree {
        //System.out.println("Parser.WORD " + lex.getCurToken());
        return when (lex.curToken) {
            Token.F, Token.U, Token.N, Token.C, Token.R, Token.T, Token.I, Token.O, Token.P, Token.E, Token.D, Token.LETTER -> {
              /*  val letterTree = arrayListOf<Tree>()
                do {
                    letterTree.add(Tree(lex.curChar.toChar().toString()))
                } while (isLetter(lex.nextToken()))
                Tree("WORD", *letterTree.toTypedArray())*/

                val letterTree = arrayListOf<Char>()
                var k = String()
                do {
                    letterTree.add(lex.curChar.toChar())
                        k = k.plus(lex.curChar.toChar())
                } while (isLetter(lex.nextToken()))
                Tree("WORD", Tree(k))
            }
            else -> throw AssertionError()
        }
    }

    private fun isLetter(token: Token?): Boolean {
        return token === Token.LETTER || token === Token.F || token === Token.U || token === Token.N ||
                token === Token.C || token === Token.R || token === Token.T || token === Token.I ||
                token === Token.O || token === Token.P || token === Token.E || token === Token.D
    }

    companion object {
        private val COLON_TREE = Tree(":")
        private val SEMICOLON_TREE = Tree(";")
        private val LEFT_BRACKET_TREE = Tree("(")
        private val RIGHT_BRACKET_TREE = Tree(")")
        private val COMMA_TREE = Tree(",")
    }
}