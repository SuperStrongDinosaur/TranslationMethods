package test

import visualization.visualizator

fun main(args : Array<String>) {
    //val b = visualizator("function f(a, b, c, d,e,f,g: integer; g : double; Straustrup: String):int;")
    val b = visualizator("function Mbzfvw(vYZitQnp:POT):K")
   // b.vis()
    val a = ParserTester()
    a.singleVarSingleType()
    a.linearComplexityTest()
    a.manyVarsManyType()
    a.manyVarsSingleType()
    a.singleVarsManyType()
}