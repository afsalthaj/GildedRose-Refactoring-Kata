package com.gildedrose

import com.gildedrose.Item._

final case class GildedRoseV2(items: List[Item]) {
  def updateQualityAndSellIn: List[Item] = items.map(_.updateQualityAndSellIn)
  def updateQualityAndSellInFor(n: NumberOfDays): Option[List[Item]] = {
    traverse(items)(_.afterNDays(n))
  }
}
