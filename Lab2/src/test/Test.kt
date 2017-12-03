package test

import org.apache.commons.lang.RandomStringUtils
import org.junit.Test
import java.text.ParseException
import java.util.Random

import main.Parser

class ParserTester {

    internal var random = Random(0)

    private fun generateWord(): String {
        return RandomStringUtils.randomAlphabetic(random.nextInt(10) + 1)
    }

    @Test
    @Throws(ParseException::class)
    fun singleVarSingleType() {
        baseTest(1, 1, 1, 1)
    }

    @Test
    @Throws(ParseException::class)
    fun manyVarsSingleType() {
        baseTest(1, 1, 1, 10)
    }

    @Test
    @Throws(ParseException::class)
    fun singleVarsManyType() {
        baseTest(1, 10, 1, 1)
    }

    @Test
    @Throws(ParseException::class)
    fun manyVarsManyType() {
        baseTest(1, 10, 1, 10)
    }

    @Test
    @Throws(ParseException::class)
    fun linearComplexityTest() {
        baseTest(600, 800, 600, 800)
    }

    private fun generateTypeGroups(typesAmount: Int, wordsMin: Int, wordsMax: Int): StringBuilder {
        val builder = StringBuilder()
        for (j in 0 until typesAmount) {
            builder.append(typeGroup(random.nextInt(wordsMax - wordsMin + 1) + wordsMin))
            if (j != typesAmount - 1)
                builder.append(';')
        }
        return builder
    }

    private fun declarationWrapper(stringBuilder: StringBuilder): String {
        return "function " + generateWord() + "(" + stringBuilder.toString() + "):" + generateWord()
    }

    private fun typeGroup(varsAmount: Int): StringBuilder {
        return varGroup(varsAmount).append(generateWord())
    }

    private fun varGroup(varsAmount: Int): StringBuilder {
        var varsAmount = varsAmount
        val result = StringBuilder()
        while (varsAmount-- > 0) {
            result.append(generateWord())
                    .append(if (varsAmount == 0) ':' else ',')
        }
        return result
    }

    @Throws(ParseException::class)
    private fun baseTest(tMin: Int, tMax: Int, wMin: Int, wMax: Int) {
        for (i in 0..9) {
            val typesAmount = random.nextInt(tMax - tMin + 1) + tMin
            val test = declarationWrapper(generateTypeGroups(typesAmount, wMin, wMax))
            System.out.println(test)
            println(test.length)
            Parser(test).S()
            println("\tdone")
        }
    }
}