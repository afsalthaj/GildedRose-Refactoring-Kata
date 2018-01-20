package com.gildedrose

import com.gildedrose.QualityAndSellInUpdate.{ Quality, SellIn }
import Tagger._

class Item(val name: String, var sellIn: Int, var quality: Int) {

}

object Item {

  type NumberOfDays = NumberOfDays.Type
  object NumberOfDays extends Tagger[Int]

  object Type {

    sealed trait Default
    sealed trait AgedBrie
    sealed trait Sulfuras
    sealed trait Backstage

    def updateQualityAndSellIn(i: Item): Item = {
      i.name match {
        case "Aged Brie" =>
          Item.from(i.name, QualityAndSellInUpdate[AgedBrie](SellIn(i.sellIn), Quality(i.quality)))
        case "Sulfuras, Hand of Ragnaros" =>
          Item.from(i.name, QualityAndSellInUpdate[Sulfuras](SellIn(i.sellIn), Quality(i.quality)))
        case "Backstage passes to a TAFKAL80ETC concert" =>
          Item.from(i.name, QualityAndSellInUpdate[Backstage](SellIn(i.sellIn), Quality(i.quality)))
        case _ =>
          Item.from(i.name, QualityAndSellInUpdate[Default](SellIn(i.sellIn), Quality(i.quality)))
      }
    }
  }

  def from(name: String, sq: (SellIn, Quality)): Item =
    new Item(name, sq._1.unwrap, sq._2.unwrap)

  implicit class ItemOps(a: Item) {
    def updateQualityAndSellIn: Item = Type.updateQualityAndSellIn(a)
    def afterNDays(n: NumberOfDays): Option[Item] =
      unfold(a) { t =>
        val item = t.updateQualityAndSellIn
        (item, item)
      }.take(n.unwrap).lift(n.unwrap)
  }
}
