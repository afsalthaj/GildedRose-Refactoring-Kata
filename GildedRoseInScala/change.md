## Changes in Version 1.1

* Add scalastyle config and scalariform
* Extend test cases for the `GildedRose` implementation.
* Add companion object in Item, specifying different types of Items. Add method `updateQualityAndSellIn` for the Type of Item. `updateQualityAndSellIn` updates the quality
depending on the name of the item.
* Add another implementation of GildedRose (GildedRoseV2), which depends on the type class `QualityAndSellInUpdate`
* Add type class `QualityAndSellInUpdate[A]`. All types of `Item` should have an instance of this behaviour
so that their quality (with their sellIn) can be updated. 
* Add unit test cases, similar to the extended test cases in `GildedRose`.
* Add Tag classes, to avoid type mismatch.