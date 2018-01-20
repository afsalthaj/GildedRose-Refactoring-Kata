package com.gildedrose

import org.scalatest._

// TODO; It can more easily be represented using scala property check
// normal items
// Spec 1:
// sell by date decreases every day,
// quality decreases by 1 everyday until 0
// quality of an item is never greater than 50

// Aged Brie
// sellIn decreases every day..
// quality increases every day, and increases twice after the sellInDate is finished
// it won't be greater than 50

// sulfuras
// sellIn date never has to be sold, it doesn't decrease
// quality never decreases every day

// Backstage Passes
// sellIn date decreases everyday
// quality increases everyday by 2 if sellIn < 11, and  by 3 if < 6 and zero when concert finishes
class GildedRoseTestV2 extends FlatSpec with Matchers {

  // TODO; make it property based testing going forward
  behavior of "A normal item"

  it should "decrease in quality and sellIn date when updating the quality" in {
    val items = List[Item](new Item("foo", 1, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(0))
    list.lift(0).map(_.sellIn) should equal(Some(0))
  }

  it should "not decrease in quality if the quality is already zero" in {
    val items = List[Item](new Item("foo", 1, 0))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(0))
    list.lift(0).map(_.sellIn) should equal(Some(0))
  }

  it should "decrease in sellIn date even if sellIn date is negative" in {
    val items = List[Item](new Item("foo", -1, 0))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(0))
  }

  it should "decrease in quality even if sellIn date is negative" in {
    val items = List[Item](new Item("foo", -1, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(0))
    list.lift(0).map(_.sellIn) should equal(Some(-2))
  }

  behavior of "A aged brie"

  it should "decrease in sellIn date when updating quality" in {
    val items = List[Item](new Item("Aged Brie", 1, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.sellIn) should equal(Some(0))
  }

  it should "decrease in sellIn date when sellIn date is zero" in {
    val items = List[Item](new Item("Aged Brie", 0, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.sellIn) should equal(Some(-1))
  }

  it should "decrease in sellIn date when sellIn date is negative" in {
    val items = List[Item](new Item("Aged Brie", -1, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.sellIn) should equal(Some(-2))
  }

  it should "increase in quality when current quality is less than 50" in {
    val items = List[Item](new Item("Aged Brie", 2, 0))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(1))
  }

  it should "increase in quality by 2 when sellInDate is zero" in {
    val items = List[Item](new Item("Aged Brie", 0, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(3))
  }

  it should "stop increase in quality when current quality is equal to 50" in {
    val items = List[Item](new Item("Aged Brie", 2, 50))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(50))
  }

  it should "stop increase in quality when current quality is equal to 50 and sellInDate is zero" in {
    val items = List[Item](new Item("Aged Brie", 0, 50))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(50))
  }

  behavior of "Sulfuras"

  it should "not decrease the sellInDate" in {
    val items = List[Item](new Item("Sulfuras, Hand of Ragnaros", 10, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.sellIn) should equal(Some(10))
  }

  it should "not decrease in quality" in {
    val items = List[Item](new Item("Sulfuras, Hand of Ragnaros", 10, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(1))
  }

  it should "not increase or decrease in quality when sellInDate is already zero" in {
    val items = List[Item](new Item("Sulfuras, Hand of Ragnaros", 0, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(1))
  }

  behavior of "Backstage passes to a TAFKAL80ETC concert"

  it should "decrease in sellInDate" in {
    val items = List[Item](new Item("Backstage passes to a TAFKAL80ETC concert", 1, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.sellIn) should equal(Some(0))
  }

  it should "decrease in sellInDate when current sellInDate is zero" in {
    val items = List[Item](new Item("Backstage passes to a TAFKAL80ETC concert", 0, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.sellIn) should equal(Some(-1))
  }

  it should "increase in quality by 1 if sellInDate is above 10" in {
    val items = List[Item](new Item("Backstage passes to a TAFKAL80ETC concert", 11, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(2))
  }

  it should "increase in quality by 2 if sellInDate is less than 11" in {
    val items = List[Item](new Item("Backstage passes to a TAFKAL80ETC concert", 10, 1))
    val app = GildedRoseV2(items)

    val list = app.updateQualityAndSellIn
    list.lift(0).map(_.quality) should equal(Some(3))
  }

  it should "increase in quality by 3 if sellInDate is less than 6" in {
    val items = List[Item](new Item("Backstage passes to a TAFKAL80ETC concert", 5, 1))
    val app = GildedRoseV2(items)
    val list = app.updateQualityAndSellIn

    list.lift(0).map(_.quality) should equal(Some(4))
  }

  it should "not increase in quality if current quality is equal to 50" in {
    val items = List[Item](new Item("Backstage passes to a TAFKAL80ETC concert", 5, 50))
    val app = GildedRoseV2(items)

    val list = app.updateQualityAndSellIn
    list.lift(0).map(_.quality) should equal(Some(50))
  }
}