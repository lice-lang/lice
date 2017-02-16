package lice.compiler

import lice.compiler.ast.createAst
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
		println(ast.root.eval())
	}
}
