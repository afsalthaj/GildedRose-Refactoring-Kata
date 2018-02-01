package com.gildedrose

import Tagger._

import Item._

// https://medium.com/@anler/type-safe-scala-phantom-type-parameters-a638f07164e6
trait ProcessingBehaviour[A <: Item.Type] {
  def update(sellIn: SellIn, quality: Quality): (SellIn, Quality)
}

object ProcessingBehaviour {
  def apply[A <: Item.Type: ProcessingBehaviour](a: SellIn, q: Quality): (SellIn, Quality) =
    implicitly[ProcessingBehaviour[A]].update(a, q)

  implicit object DefaultItem extends ProcessingBehaviour[Item.Type.Default] {
    def update(sellIn: SellIn, quality: Quality): (SellIn, Quality) =
      (SellIn(sellIn.unwrap - 1),
        if (quality.unwrap == 0) Quality(quality.unwrap) else Quality(quality.unwrap - 1))
  }

  implicit object AgedBrie extends ProcessingBehaviour[Item.Type.AgedBrie] {
    def update(sellIn: SellIn, quality: Quality): (SellIn, Quality) =
      (SellIn(sellIn.unwrap - 1), Quality((sellIn.unwrap, quality.unwrap) match {
        case (ss, qq) if qq < 50 && ss <= 0 => qq + 2
        case (ss, qq) if qq < 50 && ss > 0 => qq + 1
        case (_, qq) => qq
      }))
  }

  implicit object Sulfuras extends ProcessingBehaviour[Item.Type.Sulfuras] {
    def update(sellIn: SellIn, quality: Quality): (SellIn, Quality) = (sellIn, quality)
  }

  implicit object BackStage extends ProcessingBehaviour[Item.Type.Backstage] {
    def update(s: SellIn, q: Quality): (SellIn, Quality) =
      (SellIn(s.unwrap - 1), Quality((s.unwrap, q.unwrap) match {
        case (ss, qq) if ss < 6 && qq < 50 => qq + 3
        case (ss, qq) if ss < 11 && qq < 50 => qq + 2
        case (_, qq) if qq < 50 => qq + 1
        case (_, qq) => qq
      }))
  }
}

