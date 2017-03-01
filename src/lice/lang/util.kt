/**
 * Created by ice1000 on 2017/3/1.
 *
 * @author ice1000
 */
@file:JvmMultifileClass
@file:JvmName("Lang")

package lice.lang

import lice.compiler.util.InterpretException.Factory.typeMisMatch
import java.math.BigDecimal
import java.math.BigInteger

class NumberOperator(var initial: Number = 0) {
	var level = 0
	val result: Number
		get() = initial

	@JvmOverloads
	fun plus(o: Number, lineNumber: Int = -1) {
		val hisLevel = getLevel(o, lineNumber)
		if (hisLevel <= level) {
			initial = plusValue(o, initial, lineNumber)
		} else {
			initial = plusValue(initial, o, lineNumber)
			level = getLevel(initial)
		}
	}

	companion object Leveler {
		@JvmOverloads
		fun getLevel(o: Number, lineNumber: Int = -1) = when (o) {
			is Byte -> 0
//			is Char -> 1
			is Short -> 2
			is Int -> 3
			is Long -> 4
			is Float -> 5
			is Double -> 6
			is BigInteger -> 7
			is BigDecimal -> 8
			else -> typeMisMatch("Numberic", o, lineNumber)
		}

		@JvmOverloads
		fun plusValue(
				lowLevel: Number,
				highLevel: Number,
				lineNumber: Int = -1): Number = when (highLevel) {
			is Byte -> highLevel + lowLevel.toByte()
			is Short -> highLevel + lowLevel.toShort()
			is Int -> highLevel + lowLevel.toInt()
			is Long -> highLevel + lowLevel.toLong()
			is Float -> highLevel + lowLevel.toFloat()
			is Double -> highLevel + lowLevel.toDouble()
			is BigInteger -> {
				val l = getLevel(lowLevel)
				when {
					l <= 4 ->
						// int
						highLevel + BigInteger(lowLevel.toString())
					l == 7 ->
						// both are big integer
						highLevel + lowLevel as BigInteger
					else ->
						// low is Float/Double, and high is BigInteger
						// should return a big decimal
						BigDecimal(highLevel) + BigDecimal(lowLevel.toString())
				}
			}
			is BigDecimal ->
				highLevel + BigDecimal(lowLevel.toString())
			else -> typeMisMatch("Numberic", lowLevel, lineNumber)
		}

		@JvmOverloads
		fun minusValue(
				lowLevel: Number,
				highLevel: Number,
				lineNumber: Int = -1): Number = when (highLevel) {
			is Byte -> highLevel - lowLevel.toByte()
			is Short -> highLevel - lowLevel.toShort()
			is Int -> highLevel - lowLevel.toInt()
			is Long -> highLevel - lowLevel.toLong()
			is Float -> highLevel - lowLevel.toFloat()
			is Double -> highLevel - lowLevel.toDouble()
			is BigInteger -> {
				val l = getLevel(lowLevel)
				when {
					l <= 4 ->
						// int
						highLevel - BigInteger(lowLevel.toString())
					l == 7 ->
						// both are big integer
						highLevel - lowLevel as BigInteger
					else ->
						// low is Float/Double, and high is BigInteger
						// should return a big decimal
						BigDecimal(highLevel) - BigDecimal(lowLevel.toString())
				}
			}
			is BigDecimal ->
				highLevel - BigDecimal(lowLevel.toString())
			else -> typeMisMatch("Numberic", lowLevel, lineNumber)
		}
	}
}
