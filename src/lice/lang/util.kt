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

class NumberOperator
@JvmOverloads
constructor(var initial: Number = 0) {
	var level = getLevel(initial)
	val result: Number
		get() = initial

	fun plusLikeFunctionsImpl(
			o: Number,
			lineNumber: Int,
			function: (Number, Number, Int) -> Number): NumberOperator {
		val hisLevel = getLevel(o, lineNumber)
		if (hisLevel <= level) {
			initial = function(o, initial, lineNumber)
		} else {
			initial = function(initial, o, lineNumber)
			level = getLevel(initial)
		}
		return this
	}

	fun minusLikeFunctionsImpl(
			o: Number,
			lineNumber: Int,
			function: (Number, Number, Int, Boolean) -> Number): NumberOperator {
		val hisLevel = getLevel(o, lineNumber)
		if (hisLevel <= level) {
			initial = function(o, initial, lineNumber, false)
		} else {
			initial = function(initial, o, lineNumber, true)
			level = getLevel(initial)
		}
		return this
	}

	@JvmOverloads
	fun plus(o: Number, lineNumber: Int = -1) =
			plusLikeFunctionsImpl(o, lineNumber, { a, b, c -> plusValue(a, b, c) })

	@JvmOverloads
	fun minus(o: Number, lineNumber: Int = -1) =
			minusLikeFunctionsImpl(o, lineNumber, { a, b, c, d -> minusValue(a, b, c, d) })

	@JvmOverloads
	fun times(o: Number, lineNumber: Int = -1) =
			plusLikeFunctionsImpl(o, lineNumber, { a, b, c -> timesValue(a, b, c) })

	@JvmOverloads
	fun div(o: Number, lineNumber: Int = -1) =
			minusLikeFunctionsImpl(o, lineNumber, { a, b, c, d -> divValue(a, b, c, d) })

	@JvmOverloads
	fun rem(o: Number, lineNumber: Int = -1) =
			minusLikeFunctionsImpl(o, lineNumber, { a, b, c, d -> remValue(a, b, c, d) })

companion object Leveler {
	fun compare(
			o1: Number,
			o2: Number,
			lineNumber: Int = -1): Int {
		if (getLevel(o2, lineNumber) <= getLevel(o1, lineNumber))
			return compareValue(o2, o1, lineNumber)
		else
			return compareValue(o1, o2, lineNumber).negative()
	}

	internal fun Int.negative() = when {
		this > 0 -> -1
		this < 0 -> 1
		else -> 0
	}

	internal fun getLevel(o: Number, lineNumber: Int = -1) = when (o) {
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

	internal fun plusValue(
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

	internal fun timesValue(
			lowLevel: Number,
			highLevel: Number,
			lineNumber: Int = -1): Number = when (highLevel) {
		is Byte -> highLevel * lowLevel.toByte()
		is Short -> highLevel * lowLevel.toShort()
		is Int -> highLevel * lowLevel.toInt()
		is Long -> highLevel * lowLevel.toLong()
		is Float -> highLevel * lowLevel.toFloat()
		is Double -> highLevel * lowLevel.toDouble()
		is BigInteger -> {
			val l = getLevel(lowLevel)
			when {
				l <= 4 ->
					// int
					highLevel * BigInteger(lowLevel.toString())
				l == 7 ->
					// both are big integer
					highLevel * lowLevel as BigInteger
				else ->
					// low is Float/Double, and high is BigInteger
					// should return a big decimal
					BigDecimal(highLevel) * BigDecimal(lowLevel.toString())
			}
		}
		is BigDecimal ->
			highLevel * BigDecimal(lowLevel.toString())
		else -> typeMisMatch("Numberic", lowLevel, lineNumber)
	}

	internal fun minusValue(
			lowLevel: Number,
			highLevel: Number,
			lineNumber: Int = -1,
			reverse: Boolean = false): Number = when (highLevel) {
		is Byte ->
			if (!reverse) (highLevel - lowLevel.toByte())
			else (lowLevel.toByte() - highLevel)
		is Short ->
			if (!reverse) (highLevel - lowLevel.toShort())
			else (lowLevel.toShort() - highLevel)
		is Int ->
			if (!reverse) (highLevel - lowLevel.toInt())
			else (lowLevel.toInt() - highLevel)
		is Long ->
			if (!reverse) (highLevel - lowLevel.toLong())
			else (lowLevel.toLong() - highLevel)
		is Float ->
			if (!reverse) (highLevel - lowLevel.toFloat())
			else (lowLevel.toFloat() - highLevel)
		is Double ->
			if (!reverse) (highLevel - lowLevel.toDouble())
			else (lowLevel.toDouble() - highLevel)
		is BigInteger -> {
			val l = getLevel(lowLevel)
			when {
				l <= 4 ->
					// int
					if (!reverse) highLevel - BigInteger(lowLevel.toString())
					else BigInteger(lowLevel.toString()) - highLevel
				l == 7 ->
					// both are big integer
					if (!reverse) highLevel - lowLevel as BigInteger
					else lowLevel as BigInteger - highLevel
				else ->
					// low is Float/Double, and high is BigInteger
					// should return a big decimal
					if (!reverse) BigDecimal(highLevel) - BigDecimal(lowLevel.toString())
					else BigDecimal(lowLevel.toString()) - BigDecimal(highLevel)
			}
		}
		is BigDecimal ->
			highLevel - BigDecimal(lowLevel.toString())
		else -> typeMisMatch("Numberic", lowLevel, lineNumber)
	}

	internal fun remValue(
			lowLevel: Number,
			highLevel: Number,
			lineNumber: Int = -1,
			reverse: Boolean = false): Number = when (highLevel) {
		is Byte ->
			if (!reverse) (highLevel % lowLevel.toByte())
			else (lowLevel.toByte() % highLevel)
		is Short ->
			if (!reverse) (highLevel % lowLevel.toShort())
			else (lowLevel.toShort() % highLevel)
		is Int ->
			if (!reverse) (highLevel % lowLevel.toInt())
			else (lowLevel.toInt() % highLevel)
		is Long ->
			if (!reverse) (highLevel % lowLevel.toLong())
			else (lowLevel.toLong() % highLevel)
		is Float ->
			if (!reverse) (highLevel % lowLevel.toFloat())
			else (lowLevel.toFloat() % highLevel)
		is Double ->
			if (!reverse) (highLevel % lowLevel.toDouble())
			else (lowLevel.toDouble() % highLevel)
		is BigInteger -> {
			val l = getLevel(lowLevel)
			when {
				l <= 4 ->
					// int
					if (!reverse) highLevel % BigInteger(lowLevel.toString())
					else BigInteger(lowLevel.toString()) % highLevel
				l == 7 ->
					// both are big integer
					if (!reverse) highLevel % lowLevel as BigInteger
					else lowLevel as BigInteger % highLevel
				else ->
					// low is Float/Double, and high is BigInteger
					// should return a big decimal
					if (!reverse) BigDecimal(highLevel) % BigDecimal(lowLevel.toString())
					else BigDecimal(lowLevel.toString()) % BigDecimal(highLevel)
			}
		}
		is BigDecimal ->
			highLevel % BigDecimal(lowLevel.toString())
		else -> typeMisMatch("Numberic", lowLevel, lineNumber)
	}

	internal fun compareValue(
			lowLevel: Number,
			highLevel: Number,
			lineNumber: Int = -1): Int = when (highLevel) {
		is Byte -> highLevel.compareTo(lowLevel.toByte())
		is Short -> highLevel.compareTo(lowLevel.toShort())
		is Int -> highLevel.compareTo(lowLevel.toInt())
		is Long -> highLevel.compareTo(lowLevel.toLong())
		is Float -> highLevel.compareTo(lowLevel.toFloat())
		is Double -> highLevel.compareTo(lowLevel.toDouble())
		is BigInteger -> {
			val l = getLevel(lowLevel)
			when {
				l <= 4 ->
					// int
					highLevel.compareTo(BigInteger(lowLevel.toString()))
				l == 7 ->
					// both are big integer
					highLevel.compareTo(lowLevel as BigInteger)
				else ->
					// low is Float/Double, and high is BigInteger
					// should return a big decimal
					BigDecimal(highLevel).compareTo(BigDecimal(lowLevel.toString()))
			}
		}
		is BigDecimal ->
			highLevel.compareTo(BigDecimal(lowLevel.toString()))
		else -> typeMisMatch("Numberic", lowLevel, lineNumber)
	}

	@JvmOverloads
	fun divValue(
			lowLevel: Number,
			highLevel: Number,
			lineNumber: Int = -1,
			reverse: Boolean = false): Number = when (highLevel) {
		is Byte ->
			if (!reverse) (highLevel / lowLevel.toByte())
			else (lowLevel.toByte() / highLevel)
		is Short ->
			if (!reverse) (highLevel / lowLevel.toShort())
			else (lowLevel.toShort() / highLevel)
		is Int ->
			if (!reverse) (highLevel / lowLevel.toInt())
			else (lowLevel.toInt() / highLevel)
		is Long ->
			if (!reverse) (highLevel / lowLevel.toLong())
			else (lowLevel.toLong() / highLevel)
		is Float ->
			if (!reverse) (highLevel / lowLevel.toFloat())
			else (lowLevel.toFloat() / highLevel)
		is Double ->
			if (!reverse) (highLevel / lowLevel.toDouble())
			else (lowLevel.toDouble() / highLevel)
		is BigInteger -> {
			val l = getLevel(lowLevel)
			when {
				l <= 4 ->
					// int
					if (!reverse) highLevel / BigInteger(lowLevel.toString())
					else BigInteger(lowLevel.toString()) / highLevel
				l == 7 ->
					// both are big integer
					if (!reverse) highLevel / lowLevel as BigInteger
					else lowLevel as BigInteger / highLevel
				else ->
					// low is Float/Double, and high is BigInteger
					// should return a big decimal
					if (!reverse) BigDecimal(highLevel) / BigDecimal(lowLevel.toString())
					else BigDecimal(lowLevel.toString()) / BigDecimal(highLevel)
			}
		}
		is BigDecimal ->
			highLevel / BigDecimal(lowLevel.toString())
		else -> typeMisMatch("Numberic", lowLevel, lineNumber)
	}
}

}
