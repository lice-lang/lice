/**
 * Created by ice1000 on 2017/3/1.
 *
 * @author ice1000
 * @since 1.0.0
 */
@file:JvmMultifileClass
@file:JvmName("Lang")

package org.lice.lang

import org.lice.compiler.model.MetaData
import org.lice.compiler.model.MetaData.Factory.EmptyMetaData
import org.lice.compiler.util.InterpretException.Factory.typeMisMatch
import java.math.BigDecimal
import java.math.BigInteger

@SinceKotlin("1.1")
typealias PlusLikeFunc = (Number, Number, MetaData) -> Number
@SinceKotlin("1.1")
typealias MinusLikeFunc = (Number, Number, MetaData, Boolean) -> Number

class NumberOperator(var initial: Number) {
	var level = getLevel(initial)
	val result: Number
		get() = initial

	private inline fun plusLikeFunctionsImpl(
			o: Number,
			meta: MetaData,
			function: PlusLikeFunc): NumberOperator {
		val hisLevel = getLevel(o, meta)
		if (hisLevel <= level) {
			initial = function(o, initial, meta)
		} else {
			initial = function(initial, o, meta)
			level = getLevel(initial)
		}
		return this
	}

	private inline fun minusLikeFunctionsImpl(
			o: Number,
			meta: MetaData,
			function: MinusLikeFunc): NumberOperator {
		val hisLevel = getLevel(o, meta)
		if (hisLevel <= level) {
			initial = function(o, initial, meta, false)
		} else {
			initial = function(initial, o, meta, true)
			level = getLevel(initial)
		}
		return this
	}

	@JvmOverloads
	fun plus(o: Number, meta: MetaData = EmptyMetaData) =
			plusLikeFunctionsImpl(o, meta, { a, b, c -> plusValue(a, b, c) })

	@JvmOverloads
	fun minus(o: Number, meta: MetaData = EmptyMetaData) =
			minusLikeFunctionsImpl(o, meta, { a, b, c, d -> minusValue(a, b, c, d) })

	@JvmOverloads
	fun times(o: Number, meta: MetaData = EmptyMetaData) =
			plusLikeFunctionsImpl(o, meta, { a, b, c -> timesValue(a, b, c) })

	@JvmOverloads
	fun div(o: Number, meta: MetaData = EmptyMetaData) =
			minusLikeFunctionsImpl(o, meta, { a, b, c, d -> divValue(a, b, c, d) })

	@JvmOverloads
	fun rem(o: Number, meta: MetaData = EmptyMetaData) =
			minusLikeFunctionsImpl(o, meta, { a, b, c, d -> remValue(a, b, c, d) })

	companion object Leveler {
		fun compare(
				o1: Number,
				o2: Number,
				meta: MetaData = EmptyMetaData) = when {
			getLevel(o2, meta) <= getLevel(o1, meta) -> compareValue(o2, o1, meta)
			else -> compareValue(o1, o2, meta).negative()
		}

		internal fun Int.negative() = when {
			this > 0 -> -1
			this < 0 -> 1
			else -> 0
		}

		internal fun getLevel(o: Number, meta: MetaData = EmptyMetaData) = when (o) {
			is Byte -> 0
//			is Char -> 1
			is Short -> 2
			is Int -> 3
			is Long -> 4
			is Float -> 5
			is Double -> 6
			is BigInteger -> 7
			is BigDecimal -> 8
			else -> typeMisMatch("Numberic", o, meta)
		}

		internal fun plusValue(
				lowLevel: Number,
				highLevel: Number,
				meta: MetaData = EmptyMetaData): Number = when (highLevel) {
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
			else -> typeMisMatch("Numberic", lowLevel, meta)
		}

		internal fun timesValue(
				lowLevel: Number,
				highLevel: Number,
				meta: MetaData = EmptyMetaData): Number = when (highLevel) {
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
			else -> typeMisMatch("Numberic", lowLevel, meta)
		}

		internal fun minusValue(
				lowLevel: Number,
				highLevel: Number,
				meta: MetaData = EmptyMetaData,
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
			else -> typeMisMatch("Numberic", lowLevel, meta)
		}

		internal fun remValue(
				lowLevel: Number,
				highLevel: Number,
				meta: MetaData = EmptyMetaData,
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
			else -> typeMisMatch("Numberic", lowLevel, meta)
		}

		internal fun compareValue(
				lowLevel: Number,
				highLevel: Number,
				meta: MetaData = EmptyMetaData): Int = when (highLevel) {
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
			else -> typeMisMatch("Numberic", lowLevel, meta)
		}

		@JvmOverloads
		fun divValue(
				lowLevel: Number,
				highLevel: Number,
				meta: MetaData = EmptyMetaData,
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
			else -> typeMisMatch("Numberic", lowLevel, meta)
		}
	}

}
