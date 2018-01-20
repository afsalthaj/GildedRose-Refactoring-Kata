package com.gildedrose

import com.gildedrose.QualityAndSellInUpdate._

import Tagger._

// https://medium.com/@anler/type-safe-scala-phantom-type-parameters-a638f07164e6
trait QualityAndSellInUpdate[A] {
  def update(s: SellIn, q: Quality): (SellIn, Quality)
}

object QualityAndSellInUpdate {

  type SellIn = SellIn.Type
  object SellIn extends Tagger[Int]

  type Quality = Quality.Type
  object Quality extends Tagger[Int]

  def apply[A: QualityAndSellInUpdate](a: SellIn, q: Quality): (SellIn, Quality) =
    implicitly[QualityAndSellInUpdate[A]].update(a, q)

  implicit object DefaultItem extends QualityAndSellInUpdate[Item.Type.Default] {
    def update(s: SellIn, q: Quality): (SellIn, Quality) =
      (SellIn(s.unwrap - 1), if (q.unwrap == 0) Quality(q.unwrap) else Quality(q.unwrap - 1))

  }

  implicit object AgedBrie extends QualityAndSellInUpdate[Item.Type.AgedBrie] {
    def update(s: SellIn, q: Quality): (SellIn, Quality) =
      (SellIn(s.unwrap - 1), Quality((s.unwrap, q.unwrap) match {
        case (ss, qq) if qq < 50 && ss <= 0 => qq + 2
        case (ss, qq) if qq < 50 && ss > 0 => qq + 1
        case (_, qq) => qq
      }))
  }

  implicit object Sulfuras extends QualityAndSellInUpdate[Item.Type.Sulfuras] {
    def update(s: SellIn, q: Quality): (SellIn, Quality) = (s, q)
  }

  implicit object BackStage extends QualityAndSellInUpdate[Item.Type.Backstage] {
    def update(s: SellIn, q: Quality): (SellIn, Quality) =
      (SellIn(s.unwrap - 1), Quality((s.unwrap, q.unwrap) match {
        case (ss, qq) if ss < 6 && qq < 50 => qq + 3
        case (ss, qq) if ss < 11 && qq < 50 => qq + 2
        case (_, qq) if qq < 50 => qq + 1
        case (_, qq) => qq
      }))
  }
}

