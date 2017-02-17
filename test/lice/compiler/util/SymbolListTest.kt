package lice.compiler.util

import lice.compiler.model.Value
import org.junit.Test

/**
 * Created by ice1000 on 2017/2/18.
 *
 * @author ice1000
 */
class SymbolListTest {
	@Test
	fun testGetVariable() {
		val l = SymbolList()
		l.initialize()
		l.addVariable("fuck", Value("233"))
		val id = l.getVariableId("fuck") ?: throw ParseException("")
		println("id = $id")
		val s = l.getVariable(id)
		s.println()
		s.o.println()
		s.type.println()
		l.setVariable(id, Value("Fuck"))
		val s1 = l.getVariable(id)
		println("")
		s1.println()
		s1.o.println()
		s1.type.println()
	}
}