package lice.misc

import lice.compiler.util.ParseException

/**
	* 不是很懂你们Scala
	*
	* @author ice1000
	*/
trait Parsers[ParseError, Parser[+ _]] {
	self =>

	def run[A](p: Parser[A])(input: String): Either[ParseError, A]

	def char(c: Char): Parser[Char]

	def or[A](s1: Parser[A], s2: Parser[A]): Parser[A]

	implicit def string(s: String): Parser[String]

	implicit def operators[A](p: Parser[A]): ParserOps[A] = ParserOps[A](p)

	implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] =
		ParserOps(f(a))

	case class ParserOps[A](p: Parser[A]) {
		def |[B >: A](p2: Parser[B]): Parser[B] = self.or(p, p2)

		def or[B >: A](p2: => Parser[B]): Parser[B] = self.or(p, p2)
	}

}

/**
	* my parser
	*
	* @author ice1000
	* @tparam Parser parser
	*/
class LiceParser[Parser[+ _]] extends Parsers[ParseException, Parser] {
	override def run[A](p: Parser[A])(input: String): Either[ParseException, A] = {
//		new Right()
		???
	}

	override def char(c: Char): Parser[Char] = {
		???
	}

	override def or[A](s1: Parser[A], s2: Parser[A]): Parser[A] = {
		???
	}

	override implicit def string(s: String): Parser[String] = {
		???
	}
}

object Main {
	def main(args: Array[String]): Unit = {
		//
	}
}
