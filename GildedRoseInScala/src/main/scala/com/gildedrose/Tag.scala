package com.gildedrose

object Tag {

  // scalastyle:off structural.type
  type Tagged[T] = { type Tag = T }
  type @@[T, Tag] = T with Tagged[Tag]

  @inline def apply[A, T](value: A): A @@ T =
    value.asInstanceOf[A @@ T]

  @inline def unwrap[A, T](value: A @@ T): A =
    value.asInstanceOf[A]
}
