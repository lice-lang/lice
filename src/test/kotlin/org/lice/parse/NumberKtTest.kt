package org.lice.parse

import org.junit.Assert.*
import org.junit.Test
import org.lice.parse.*
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Created by ice1000 on 2017/2/20.
 *
 * @author ice1000
 */
class NumberKtTest {
	@Test(timeout = 1000)
	fun safeLower() {
		assertEquals('a', 'A'.safeLower())
		assertEquals('a', 'a'.safeLower())
		assertEquals('b', 'B'.safeLower())
		assertEquals('c', 'C'.safeLower())
		assertEquals('d', 'D'.safeLower())
		assertEquals('d', 'd'.safeLower())
	}

	@Test(timeout = 1000)
	fun isHexInt() {
		assertTrue("0x2333".isHexInt())
		assertTrue("0xA1D2".isHexInt())
		assertTrue("0x0000".isHexInt())
		assertTrue("0x9999".isHexInt())
		assertTrue("-0x9999".isHexInt())
		assertTrue("0xFFFF".isHexInt())
		assertTrue("0xABCD".isHexInt())
		assertTrue("0xffff".isHexInt())
		assertTrue("0XFFFF".isHexInt())
		assertFalse("0aFFFF".isHexInt())
		assertFalse("FFFF".isHexInt())
		assertFalse("0xABCR".isHexInt())
		assertFalse("-0xABCX".isHexInt())
	}

	@Test(timeout = 1000)
	fun isBinInt() {
		assertTrue("0b1011001".isBinInt())
		assertTrue("0B110010".isBinInt())
		assertTrue("-0B110010".isBinInt())
		assertFalse("0x01010".isBinInt())
		assertFalse("0b102001".isBinInt())
		assertFalse("0100101".isBinInt())
		assertFalse("FFFF".isBinInt())
		assertFalse("0xABCR".isBinInt())
		assertFalse("-0xABCX".isBinInt())
	}

	@Test(timeout = 1000)
	fun isOctInt() {
		assertTrue("0o1011001".isOctInt())
		assertTrue("-0o1011001".isOctInt())
		assertTrue("0o2738687001".isOctInt())
		assertTrue("-0o2738687001".isOctInt())
		assertFalse("0o9a010".isOctInt())
		assertFalse("0o91010".isOctInt())
		assertFalse("-0o91010".isOctInt())
		assertFalse("2738687001".isOctInt())
	}

	@Test(timeout = 1000)
	fun toHexInt() {
//		0x1.println()
//		Integer.toHexString("0x1".toHexInt()).println()
		assertEquals(0x2333, "0x2333".toHexInt())
		assertEquals(0xABCD, "0xABCD".toHexInt())
		assertEquals(-0x2333, "-0x2333".toHexInt())
	}

	@Test(timeout = 1000)
	fun toBinInt() {
//		0x1.println()
//		Integer.toHexString("0x1".toHexInt()).println()
		assertEquals(0b001010, "0b001010".toBinInt())
		assertEquals(0B01100010, "0B01100010".toBinInt())
	}

	@Test
	fun isBigInt() {
		assertTrue("1273983189279n".isBigInt())
		assertTrue("1273983189279N".isBigInt())
		assertTrue("-1273983189279N".isBigInt())
		assertFalse("-12739831dsf9N".isBigInt())
	}

	@Test(timeout = 1000)
	fun toBigInt() {
		assertEquals(BigInteger("12903829108"), "12903829108N".toBigInt())
		assertEquals(BigInteger("-21893219"), "-21893219N".toBigInt())
		assertEquals(BigInteger("-21893219"), "-21893219n".toBigInt())
	}

	@Test
	fun isBigDec() {
		assertTrue("1273983189279m".isBigDec())
		assertTrue("12739.83189279m".isBigDec())
		assertTrue("1273983189279M".isBigDec())
		assertTrue("127398.3189279M".isBigDec())
		assertTrue("-1273983189279M".isBigDec())
		assertTrue("-127398.3189279M".isBigDec())
		assertFalse("-12739831dsf9M".isBigDec())
		assertFalse("-127398.31dsf9M".isBigDec())
	}

	@Test
	fun toBigDec() {
		assertEquals(BigDecimal("1273983189279"), "1273983189279m".toBigDec())
		assertEquals(BigDecimal("12739.83189279"), "12739.83189279m".toBigDec())
		assertEquals(BigDecimal("1273983189279"), "1273983189279M".toBigDec())
		assertEquals(BigDecimal("127398.3189279"), "127398.3189279M".toBigDec())
		assertEquals(BigDecimal("-1273983189279"), "-1273983189279M".toBigDec())
		assertEquals(BigDecimal("-127398.3189279"), "-127398.3189279M".toBigDec())
	}
}