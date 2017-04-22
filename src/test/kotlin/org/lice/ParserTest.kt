package org.lice

import org.jetbrains.annotations.TestOnly
import org.junit.Test
import org.lice.compiler.model.MetaData
import org.lice.compiler.model.ValueNode
import org.lice.compiler.parse.parseValue
import kotlin.test.assertEquals

/**
 * Created by ice1000 on 2017/4/21.
 *
 * @author ice1000
 */

@TestOnly
class ParserTest {
	@Test
	fun test1() {
		assertEquals(ValueNode(233).eval().o, parseValue("233", MetaData.EmptyMetaData)?.eval()?.o)
	}
}
