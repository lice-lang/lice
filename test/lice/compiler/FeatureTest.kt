package lice.compiler

import lice.compiler.model.ExpressionNode
import lice.compiler.model.ValueNode
import lice.compiler.util.SymbolList
import lice.compiler.util.println
import org.junit.Test

/**
 * Created by ice1000 on 2017/2/21.
 *
 * @author ice1000
 */
class FeatureTest {
	@Test
	fun testHandWrittenAst() {
		val sl = SymbolList()
		val a = ValueNode(1)
		val b = ValueNode(1)
		val ast = ExpressionNode(sl, sl.getFunctionId("+")!!, listOf(a, b))
		ast
				.eval()
				.o
				.println()
	}
}