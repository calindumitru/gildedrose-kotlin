# gildedrose-kotlin

## Introduction

* Although I believe that the 'academical' approach to solve this exercise would be to use inheritance, I wanted to solve the assignment by choosing a different approach highlighting some of Kotlin's unique features
* Kotlin's extension function mechanism allow us to extend the functionality of the Item class, without 'touching it'.

## Implementation details

* `FNItem` acts as a wrapper for the Item class, allowing us to specify the algorithm used to calculate the items' quality.
* I have defined the following functions which deal with the calculation of the quality for each item 'type' 
  * updateGeneric
  * updateAgedBrie
  * updateLegendaryItem
  * updateBackstagePass
  * updateConjuredItem
* Adding support for a new item with a different behaviour is as simple as creating a new `updateSomeKindOfItem` function and adding a new rule to the Factory.
* I kept and fixed the existing test, ensuring that the item name does not change on update.
  * If we could alter the Item class, I would prefer to change `var name` to `val name` and remove this test.
* Implemented a restriction ensuring that Sulfuras, as a legendary item, can only exist with Quality of 80, which never alters.
* Implemented the following tests, matching the rules described in the specification requirements
  
```
"Aged Brie" item quality should increase
"Sulfuras" item quality should not change
item quality should not be negative
  [1] some generic item
  [2] conjured item
item quality should not surpass 50
  [1] Aged Brie
  [2] Backstage passes to a TAFKAL80ETC concert
  [3] generic backstage pass
when updating, sell-in should decrease by 1
  [1] +5 Dexterity Vest
  [2] Aged Brie
  [3] Elixir of the Mongoose
  [4] Sulfuras, Hand of Ragnaros
  [5] Backstage passes to a TAFKAL80ETC concert
  [6] Conjured Mana Cake
GenericItemTests
  before surpassing the sell by date, generic item quality should decrease by 1
  once the sell by date has passed, generic item quality should decrease by 2
LegendaryItemTests
  Sulfuras item quality should not change
  creating Sulfuras with item quality other than 80 should throw an error
BackStagePassTests
  when there are 10 days or less before concert, backstage pass item quality should increase by 2
  when there are more than 10 days before concert, backstage pass item quality should increase by 1
  when there are 5 days or less before concert, backstage pass item quality should increase by 3
  after concert, backstage pass item quality should drop to 0
ConjuredItemTests
  before surpassing the sell by date, generic item quality should decrease by 2
  once the sell by date has passed, generic item quality should decrease by 4
```

## Future optimizations

* The item detection algorithm is quite costly at the moment. A possible improvement could be to keep a 'catalogue' of all items and their associated update function, which would reduce the computational complexity of the `toNew()` method from O(n) to O(1).
  * the obvious drawback of this alternative is that we would need to keep the catalogue updated with every new item which is unfeasible, especially in the case of 'Backstage passes'
```kotlin
// instead of:
fun Item.toNew(): FNItem {
    return when {
        name == "Aged Brie" -> {
            FNItem(this, Item::updateAgedBrie)
        }
        //[...]
        name.contains("Conjured") -> {
            FNItem(this, Item::updateConjuredItem)
        }
        else -> FNItem(this, Item::updateGeneric)
    }
}

// alternative:
class FNItem(private val item: Item, private val updateQuality: UpdateQuality) {
    // [...]

    companion object {
        val catalogue = mapOf<String, UpdateQuality>(
            "Aged Brie" to Item::updateAgedBrie,
            "Conjured Mana Cake" to Item::updateConjuredItem,
            // [...]
        )
    }
}

fun Item.toNew(): FNItem {
    return FNItem(this, updateQuality = FNItem.catalogue[this.name] ?: Item::updateGeneric)
}
```
* Upon the introduction of new 'aged' items and assuming their quality would increase over time in the same manner as an 'Aged Brie', `updateAgedBrie` could be renamed to `updateAged`
* Upon the introduction of new 'legendary' items with unique names, it might make sense to keep these names in a Set.