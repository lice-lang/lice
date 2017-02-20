package lice.compiler.parse

import lice.compiler.util.println
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by ice1000 on 2017/2/20.
 *
 * @author ice1000
 */
class NumberKtTest {
	@Test
	fun safeLower() {
		assertEquals('a', 'A'.safeLower())
		assertEquals('a', 'a'.safeLower())
		assertEquals('b', 'B'.safeLower())
		assertEquals('c', 'C'.safeLower())
		assertEquals('d', 'D'.safeLower())
		assertEquals('d', 'd'.safeLower())
	}

}