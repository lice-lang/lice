package org.lice.parse

import org.lice.model.MetaData

class Token constructor(
		val type: TokenType,
		val strValue: String,
		val metaData: MetaData) {

	constructor(
			type: TokenType,
			strValue: String,
			beginLineNumber: Int,
			endLineNumber: Int,
			beginIndex: Int,
			endIndex: Int)
			: this(type, strValue, MetaData(beginLineNumber, endLineNumber, beginIndex, endIndex))

	enum class TokenType {
		BinNumber,
		OctNumber,
		DecNumber,
		HexNumber,
		LongInteger,
		BigInt,
		BigDec,
		FloatNumber,
		DoubleNumber,
		StringLiteral,
		Identifier,
		LispKwd,
		EOI
	}
}

val Token.TokenType.isIntegral
	get() = this == Token.TokenType.BinNumber
			|| this == Token.TokenType.OctNumber
			|| this == Token.TokenType.DecNumber
			|| this == Token.TokenType.HexNumber
			|| this == Token.TokenType.LongInteger

val Token.TokenType.isDecimal
	get() = this == Token.TokenType.DecNumber
			|| this == Token.TokenType.FloatNumber
			|| this == Token.TokenType.DoubleNumber

val Token.TokenType.isFloating
	get() = this == Token.TokenType.FloatNumber || this == Token.TokenType.DoubleNumber
