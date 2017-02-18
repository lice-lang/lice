package lice.compiler

import lice.compiler.model.StringMiddleNode
import lice.compiler.parse.buildNode
import lice.compiler.parse.createAst
import org.junit.Test
import java.io.File

/**
 * Created by ice1000 on 2017/2/15.
 *
 * @author ice1000
 */
class ParseTest {
	@Test
	fun testParse() {
		val ast = createAst(File("sample/test2.lice"))
		println(ast.root.eval().o)
	}

	@Test
	fun testBuildNode() {
//		buildNode("abc 123 vbvbvb")
		val root = buildNode("abc 123 (vbvbvb asdj (xaskj) (dhsaj dsaj))")
		(root as StringMiddleNode).list.forEach {
			println(it.strRepr)
		}
	}
}
