package com.gildedrose

import com.gildedrose.Tag.@@

trait Tagger[A] {
  sealed trait Marker
  type Type = A @@ Marker
  def apply(a: A): A @@ Marker = Tag[A, Marker](a)
}

object Tagger {
  implicit class TaggerOps[A, T](value: A @@ T) {
    def unwrap: A = Tag.unwrap[A, T](value)
  }
}
