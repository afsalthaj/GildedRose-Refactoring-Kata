package com

package object gildedrose {
  def unfold[S, A](z: S)(f: S => (S, A)): Stream[A] = {
    val (s, a) = f(z)
    Stream.cons(a, unfold(s)(f))
  }

  def map2[A, B, C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    a.flatMap(aa => b.map(bb => f(aa, bb)))

  def traverse[A, B](a: List[A])(f: A => Option[B]): Option[List[B]] = {
    a.foldRight(Some(Nil): Option[List[B]])((x, y) => map2(f(x), y)(_ :: _))
  }
}
