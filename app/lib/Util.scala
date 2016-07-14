package lib

/**
  * Created by Zhenglai on 7/14/16.
  */
object Util {

  /*
  * Apply takes arguments and by convention will return a value related to the object's name.
  * If we take Scala's case classes as "correct" usage then the object Foo's apply will construct a Foo instance without needing to add "new". You are free of course to make apply do whatever you wish (key to value in Map, set contains value in Set, and indexing in Seq come to mind).
  *
  * Unapply, if returning an Option or Boolean can be used in match{} and pattern matching. Like apply it's just a def so can do whatever you dream up but the common usage is to extract value(s) from instances of the object's companion class.
  * */

  // Pattern matching => Powerful tool for flow control
  // Think declaratively
  // Extractor
  // val NonZeroDouble(d) = 4.4
  //    => NonZeroDouble(d).unapply(4.4) => Some(4.4) => Match
  // val NonZeroDouble(d) = 0.0 => scala.MatchError
  object NonZeroDouble {
    // returns Option[TupleN]
    def unapply(d: Double): Option[Double] = {
      if (d == 0.0)
        None // MatchError
      else
        Some(d)
    }
  }

  val nonZeroDouble(d) = 2.0
  // syntactic sugar for
  val d: Double = NonZeroDouble.unapply(d) match {
    case Some(d) ⇒ d
    case None ⇒ throw new MatchError(d)
  }

  // Client using extractors think about control flow declaratively, they need the NonZeroDouble,
  //  rather than thinking about instructing the compiler to check whether valus is zero

  def safeDivision(numerator: Double, denominator: Double, fallBack: Double) =
    denominator match {
      case NonZeroDouble(d) ⇒ numerator / d
      case _ ⇒ fallBack
    }

  // Sequence rely on unapplySeq in companion object as extractor
  // e.g. Array.unapplySeq(Array(1,2)) => Option[Seq[A]]
  // val MyVector(x, y, z) = Vector(12, 2, 3.0)
  object MyVector {
    // Avoid clashing with Vector companion object, we rename it as MyVector
    def unapplySeq(v: Vector[Double]) = Some(v)
  }

}
