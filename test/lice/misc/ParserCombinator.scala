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


sealed trait ParserTrait[T] {
	def matches(t: T): T
}

/**
	* my parser
	*
	* @author ice1000
	*/
class LiceParsers extends Parsers[ParseException, ParserTrait] {
	override def run[A](p: ParserTrait[A])(input: String): Either[ParseException, A] = {
		//		new Right()
		???
	}

	override def char(c: Char): ParserTrait[Char] = {
		new OneParser(c)
	}

	override def or[A](s1: ParserTrait[A], s2: ParserTrait[A]): ParserTrait[A] = {
		new TwoParser[A](s1, s2)
	}

	override implicit def string(s: String): ParserTrait[String] = {
		new OneParser(s)
	}

	class OneParser[T](value: T) extends ParserTrait[T] {
		override def matches(t: T): T = {
			if (t == this.value) t
			else throw new ParseException("")
		}
	}

	class TwoParser[T](p1: ParserTrait[T], p2: ParserTrait[T]) extends ParserTrait[T] {
		override def matches(t: T): T = try {
			p1.matches(t)
		} catch {
			case ParseException => p2.matches(t)
		}
	}

	object Main {
		def main(args: Array[String]): Unit = {
		}
	}

}
