package org.lice

import org.jetbrains.annotations.TestOnly
import org.junit.Test
import org.lice.core.SymbolList
import org.lice.model.*
import org.lice.parse.parseValue
import org.lice.parse.wrapValue
import kotlin.test.*

@TestOnly
class ParserTest {
	@Test(timeout = 1000)
	fun test1() {
		assertEquals(
				ValueNode(233).eval(),
				parseValue("233", MetaData.EmptyMetaData)?.eval()
		)
	}

	@Test(timeout = 1000)
	fun test2() {
		assertNull(parseValue("null", MetaData.EmptyMetaData))
		assertNull(parseValue("true", MetaData.EmptyMetaData))
	}

	@Test(timeout = 1000)
	fun test3() {
		val sl = SymbolList()
		val md = MetaData.EmptyMetaData
		assertNull(wrapValue(StringLeafNode(md, "null"), sl).eval())
		assertTrue(true == wrapValue(StringLeafNode(md, "true"), sl).eval())
	}

	@Test(timeout = 1000)
	fun test4() {
		val md = MetaData.EmptyMetaData
		assertEquals(233, parseValue("233", md)?.eval())
		assertEquals(0x233, parseValue("0x233", md)?.eval())
		assertEquals(0b10101, parseValue("0b10101", md)?.eval())
	}

	@Test(timeout = 1000)
	fun test5() {
		assertEquals(233F, parseValue("233.0", MetaData.EmptyMetaData)?.eval())
	}
}
