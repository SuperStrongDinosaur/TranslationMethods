package visualization

import main.Parser
import main.Token
import main.Tree
import org.graphstream.graph.Edge
import org.graphstream.graph.Node
import org.graphstream.graph.implementations.SingleGraph
import org.graphstream.ui.layout.*



class visualizator(val s : String) {

    val graph = SingleGraph("tree")

    fun vis() {
        val a = Parser(s).S()

        val node = graph.addNode<Node>(a.node + cnt)
        node.setAttribute("ui.label", a.node)
        dfs(a)

        val hl = HierarchicalLayout()
      //  hl.setRoots(a.node)
        hl.shake()
        val viewer = graph.display(true)
        viewer.enableAutoLayout(hl)
    }

    var cnt = 0

    private fun dfs(s: Tree) {
        val cntPar = cnt
        for (v in s.children) {
            cnt++
            val node = graph.addNode<Node>(v.node + cnt.toString())
            node.setAttribute("ui.label", v.node)
            graph.addEdge<Edge>(v.node + cnt.toString(), s.node + cntPar.toString(), v.node + cnt.toString())
            if(v.node != Token.LETTER.toString())
            dfs(v)
        }
    }
}