package org.lice

import org.jetbrains.annotations.TestOnly
import org.junit.Test
import org.lice.ast.MetaData
import org.lice.compiler.model.StringLeafNode
import org.lice.ast.ValueNode
import org.lice.compiler.parse.parseValue
import org.lice.compiler.parse.wrapValue
import org.lice.core.SymbolList
import org.lice.core.bindings
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Created by ice1000 on 2017/4/21.
 *
 * @author ice1000
 */

@TestOnly
class ParserTest {
	@Test(timeout = 1000)
	fun test1() {
		assertEquals(
				ValueNode(233).eval().o,
				parseValue("233", MetaData.EmptyMetaData)?.eval()?.o
		)
	}

	@Test(timeout = 1000)
	fun test2() {
		assertNull(parseValue("null", MetaData.EmptyMetaData))
		assertNull(parseValue("true", MetaData.EmptyMetaData))
	}

	@Test(timeout = 1000)
	fun test3() {
		val sl = bindings()
		val md = MetaData.EmptyMetaData
		assertTrue(true == wrapValue(StringLeafNode(md, "true"), sl).eval().o)
		assertNull(wrapValue(StringLeafNode(md, "null"), sl).eval().o)
	}

	@Test(timeout = 1000)
	fun test4() {
		val md = MetaData.EmptyMetaData
		assertEquals(233, parseValue("233", md)?.eval()?.o)
		assertEquals(0x233, parseValue("0x233", md)?.eval()?.o)
		assertEquals(0b10101, parseValue("0b10101", md)?.eval()?.o)
	}

	@Test(timeout = 1000)
	fun test5() {
		assertEquals(233F, parseValue("233.0", MetaData.EmptyMetaData)?.eval()?.o)
	}
}
