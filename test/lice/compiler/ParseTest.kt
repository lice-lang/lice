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
		val root = buildNode("abc 123 (vbvbvb asdj (xaskj) (dhsaj dsaj)) ass")
		(root as StringMiddleNode).list.forEach {
			println(it.strRepr)
		}
	}

	@Test
	fun testParseComment() {
//		buildNode("abc 123 vbvbvb")
		val root = buildNode("""
; my name is Van
; I'm an artist
boyNextDoor ; this is comment
(another (element)) ; ignore me!
""")
		(root as StringMiddleNode).list.forEach {
			println(it.strRepr)
		}
	}

	@Test
	fun testParseString() {
//		buildNode("abc 123 vbvbvb")
		val root = buildNode("""
; my name is Van
; I'm an artist
boyNextDoor "; this is string"
(another ("element")) ; ignore me!
(con "what ever I put in a string token, it will be kept!" "Excited(!)")
""")
		(root as StringMiddleNode).list.forEach {
			println(it.strRepr)
		}
	}
}
