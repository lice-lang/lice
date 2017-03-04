package lice.compiler

import lice.compiler.model.ExpressionNode
import lice.compiler.model.StringMiddleNode
import lice.compiler.parse.buildNode
import lice.compiler.parse.createAst
import lice.compiler.parse.mapAst
import lice.compiler.util.DEBUGGING
import lice.compiler.util.VERBOSE
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
//		(ast.root as ExpressionNode)
//				.params
//				.run {
//					println("len: $size")
//					forEachIndexed { index, node ->
//						println("number: $index, value: ${node.eval().o}")
//					}
//				}
//		ast
//				.root
//				.eval()
//				.o
//				.println()
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
	fun test3() {
		VERBOSE = false
		DEBUGGING = false
		createAst(File("sample/test3.lice")).root.eval()
	}

	@Test
	fun testBuildNode2() {
//		buildNode("abc 123 vbvbvb")
		val root = buildNode("(* 2 2 2 2 (+ 11 11))")
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

	@Test
	fun testMapAst() {
		VERBOSE = false
		val root = buildNode("""
; code begins
(+ 1 1 1 (* 2 2))
""")
		val ast = mapAst(root)
//		ast.eval().o.println()
	}

	@Test
	fun testMapSimpleAst() {
//		VERBOSE = false
		val root = buildNode("""
; code begins
(+ 1 1)
""")
		val ast = mapAst(root)
		(ast as ExpressionNode).eval()
	}
}
