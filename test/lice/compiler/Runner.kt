package lice.compiler

import lice.compiler.parse.createAst
import lice.compiler.util.DEBUGGING
import lice.compiler.util.VERBOSE
import org.junit.Test
import java.io.File

/**
 * Created by ice1000 on 2017/2/21.
 *
 * @author ice1000
 */
class Runner {
	@Test
	fun testHandWrittenAst() {
		DEBUGGING = false
		VERBOSE = false
		createAst(File("sample/test4.lice")).root.eval()
	}
}