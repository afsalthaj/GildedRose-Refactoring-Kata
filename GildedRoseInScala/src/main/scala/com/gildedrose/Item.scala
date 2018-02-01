package com.gildedrose

import Tagger._
import com.gildedrose.Item.Type._

class Item(val name: String, var sellIn: Int, var quality: Int) {

}

object Item {

  type SellIn = SellIn.Type
  object SellIn extends Tagger[Int]

  type Quality = Quality.Type
  object Quality extends Tagger[Int]

  type NumberOfDays = NumberOfDays.Type
  object NumberOfDays extends Tagger[Int]

  sealed trait Type

  object Type {
    case class Default() extends Type
    case class AgedBrie() extends Type
    case class Sulfuras() extends Type
    case class Backstage() extends Type

    def from(s: String): Type = s match {
      case "Aged Brie" => AgedBrie()
      case "Sulfuras, Hand of Ragnaros" => Sulfuras()
      case "Backstage passes to a TAFKAL80ETC concert" => Backstage()
      case _ => Default()
    }
  }

  implicit class ItemOps(item: Item) {
    def updateQualityAndSellIn: Item = {
      def update[T <: Type: ProcessingBehaviour](item: Item): Item = {
        val (newSellIn, newQuality) =
          ProcessingBehaviour[T](SellIn(item.sellIn), Quality(item.quality))

        new Item(item.name, newSellIn, newQuality)
      }

      Type.from(item.name) match {
        case AgedBrie() => update[AgedBrie](item)
        case Sulfuras() => update[Sulfuras](item)
        case Backstage() => update[Backstage](item)
        case _ => update[Default](item)
      }
    }

    def afterNDays(n: NumberOfDays): Option[Item] =
      unfold(item) { t =>
        val item = t.updateQualityAndSellIn
        (item, item)
      }.take(n.unwrap).lift(n.unwrap)
  }
}
