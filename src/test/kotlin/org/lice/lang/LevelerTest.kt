package org.lice.lang

import org.junit.Test
import org.lice.Lice
import org.lice.evalTo
import org.lice.lang.NumberOperator.Leveler.plusValue
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.assertFails

/**
 * Created by ice1000 on 2017/3/1.
 *
 * @author ice1000
 */
class LevelerTest {
	@Test
	fun plusValue() {
		println(plusValue(1, 1L).javaClass)
		println(plusValue(1L, 1.0).javaClass)
		println(plusValue(1L, 1F).javaClass)
		println(plusValue(1F, 1F).javaClass)
		println(plusValue(1F, 1.0).javaClass)
		println(plusValue(1, BigInteger("1")).javaClass)
		println(plusValue(BigInteger("1"), BigInteger("1")).javaClass)
		println(plusValue(1.0, BigInteger("1")).javaClass)
		println(plusValue(1F, BigInteger("1")).javaClass)
		println(plusValue(BigInteger("1"), BigDecimal("1.0")).javaClass)
		println(plusValue(BigDecimal("1"), BigDecimal("1.0")).javaClass)
	}

	@Test
	fun plus() {
		val pluser = NumberOperator(1.toByte())
		pluser.plus(1.toByte())
		println(pluser.result.javaClass)
		pluser.plus(1.toShort())
		println(pluser.result.javaClass)
		pluser.plus(BigInteger("1"))
		println(pluser.result.javaClass)
		pluser.plus(1)
		println(pluser.result.javaClass)
		pluser.plus(1.toFloat())
		println(pluser.result.javaClass)
		pluser.plus(1.toDouble())
		println(pluser.result.javaClass)
		pluser.plus(BigDecimal("1"))
		println(pluser.result.javaClass)
	}

	@Test
	fun compare() {
		println(NumberOperator.compare(1, 2))
		println(NumberOperator.compare(2, 1))
		println(NumberOperator.compare(1, 1))
		println(NumberOperator.compare(1, 1.0))
		println(NumberOperator.compare(1F, 1.0))
		println(NumberOperator.compare(1L, 1.0))
		println(NumberOperator.compare(1L, 3.0))
		println(NumberOperator.compare(6L, 3.0))
		println(NumberOperator.compare(BigInteger("1"), BigDecimal("1.0")))
		println(NumberOperator.compare(BigInteger("2"), BigDecimal("1.0")))
		println(NumberOperator.compare(BigInteger("2"), BigDecimal("4.0")))
	}

	@Test
	fun generalTest() {
		Lice.run("(< 1 1L 1N 1.0F 1.0D 1M)")
		Lice.run("(> 1 1L 1N 1.0F 1.0D 1M)")
		Lice.run("(<= 1 1L 1N 1.0F 1.0D 1M)")
		Lice.run("(>= 1 1L 1N 1.0F 1.0D 1M)")
		Lice.run("(== 1 1L 1N 1.0F 1.0D 1M)")
		Lice.run("(== 1 1L 1.0F 1.0D 1M)")
		Lice.run("(!= 1 1L 1N 1.0F 1.0D 1M)")
		Lice.run("(!= 1 1L 1.0F 1.0D 1M)")
		Lice.run("(- 1 1L 1N 1.0F 1.0D 1M)")
		Lice.run("(- 1 1L 1.0F 1.0D 1M)")
		Lice.run("(+ 1 1L 1N 1.0F 1.0D 1M)")
		Lice.run("(+ 1 1L 1.0F 1.0D 1M)")
		Lice.run("(/ 1 1L 1N 1.0F 1.0D 1M)")
		Lice.run("(/ 1 1L 1.0F 1.0D 1M)")
		Lice.run("(* 1 1L 1N 1.0F 1.0D 1M)")
		Lice.run("(* 1 1L 1.0F 1.0D 1M)")
		Lice.run("(% 1 2L 3N 4.0F 5.0D 6M)")
		Lice.run("(< 1M 1.0D 1.0F 1N 1L 1)")
		Lice.run("(> 1M 1.0D 1.0F 1N 1L 1)")
		Lice.run("(<= 1M 1.0D 1.0F 1N 1L 1)")
		Lice.run("(>= 1M 1.0D 1.0F 1N 1L 1)")
		Lice.run("(== 1M 1.0D 1.0F 1N 1L 1)")
		Lice.run("(!= 1M 1.0D 1.0F 1N 1L 1)")
		Lice.run("(- 1M 1.0D 1.0F 1N 1L 1)")
		Lice.run("(+ 1M 1.0D 1.0F 1N 1L 1)")
		Lice.run("(/ 1M 1.0D 1.0F 1N 1L 1)")
		Lice.run("(* 1M 1.0D 1.0F 1N 1L 1)")
		Lice.run("(% 6M 5.0D 4.0F 3N 2L 1)")
	}

	@Test
	fun failureTests() {
		val anything = null
		assertFails { "(< 1 1L 1N 1.0F 1.0D 1M true)" evalTo anything }
		assertFails { "(> 1 1L 1N 1.0F 1.0D 1M true)" evalTo anything }
		assertFails { "(<= 1 1L 1N 1.0F 1.0D 1M true)" evalTo anything }
		assertFails { "(>= 1 1L 1N 1.0F 1.0D 1M true)" evalTo anything }
		assertFails { "(== 1 1L 1N 1.0F 1.0D 1M true)" evalTo anything }
		assertFails { "(!= 1 1L 1N 1.0F 1.0D 1M true)" evalTo anything }
		assertFails { "(- 1 1L 1N 1.0F 1.0D 1M true)" evalTo anything }
		assertFails { "(+ 1 1L 1N 1.0F 1.0D 1M true)" evalTo anything }
		assertFails { "(/ 1 1L 1N 1.0F 1.0D 1M true)" evalTo anything }
		assertFails { "(* 1 1L 1N 1.0F 1.0D 1M true)" evalTo anything }
		assertFails { "(% 1 2L 3N 4.0F 5.0D 6M true)" evalTo anything }
		assertFails { "(< 1M 1.0D 1.0F 1N 1L 1 true)" evalTo anything }
		assertFails { "(> 1M 1.0D 1.0F 1N 1L 1 true)" evalTo anything }
		assertFails { "(<= 1M 1.0D 1.0F 1N 1L 1 true)" evalTo anything }
		assertFails { "(>= 1M 1.0D 1.0F 1N 1L 1 true)" evalTo anything }
		assertFails { "(== 1M 1.0D 1.0F 1N 1L 1 true)" evalTo anything }
		assertFails { "(!= 1M 1.0D 1.0F 1N 1L 1 true)" evalTo anything }
		assertFails { "(- 1M 1.0D 1.0F 1N 1L 1 true)" evalTo anything }
		assertFails { "(+ 1M 1.0D 1.0F 1N 1L 1 true)" evalTo anything }
		assertFails { "(/ 1M 1.0D 1.0F 1N 1L 1 true)" evalTo anything }
		assertFails { "(* 1M 1.0D 1.0F 1N 1L 1 true)" evalTo anything }
		assertFails { "(% 6M 5.0D 4.0F 3N 2L 1 true)" evalTo anything }
	}
}