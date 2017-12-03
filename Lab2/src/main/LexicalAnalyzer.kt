package main

import java.io.IOException
import java.io.InputStream
import java.text.ParseException

class LexicalAnalyzer @Throws(ParseException::class) constructor(private val s: InputStream) {
    var curChar: Int = 0
    var nextChar: Int = 0
    private var curPos = 0
    var curToken: Token? = null

    init {
        nextChar()
    }

    @Throws(ParseException::class)
    private fun nextChar() {
        curPos++
        try {
            nextChar = s.read()
        } catch (e: IOException) {
            throw ParseException(e.message, curPos)
        }
    }

    @Throws(ParseException::class)
    fun nextToken(): Token? {
        skip()
        //System.out.println((char)curChar);
        curChar = nextChar
        when (curChar) {
            ','.toInt() -> curToken = Token.COMMA
            ':'.toInt() -> curToken = Token.COLON
            ';'.toInt() -> curToken = Token.SEMICOLON
            ')'.toInt() -> curToken = Token.RIGHT_BRACKET
            '('.toInt() -> curToken = Token.LEFT_BRACKET
            -1 -> {
                curToken = Token.END
                return curToken
            }
            'p'.toInt() -> curToken = Token.P
            'r'.toInt() -> curToken = Token.R
            'o'.toInt() -> curToken = Token.O
            'c'.toInt() -> curToken = Token.C
            'd'.toInt() -> curToken = Token.D
            'u'.toInt() -> curToken = Token.U
            'e'.toInt() -> curToken = Token.E
            'f'.toInt() -> curToken = Token.F
            'n'.toInt() -> curToken = Token.N
            't'.toInt() -> curToken = Token.T
            'i'.toInt() -> curToken = Token.I
            else -> if (Character.isLetter(curChar)) {
                this.curToken = Token.LETTER
            } else {
                throw ParseException("Illegal character " + curChar, curPos)
            }
        }
        nextChar()
        return curToken
    }

    @Throws(ParseException::class)
    private fun skip() {
        while (Character.isWhitespace(nextChar)) {
            nextChar()
        }
    }
}