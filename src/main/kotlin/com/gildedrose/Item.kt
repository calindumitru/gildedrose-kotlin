package com.gildedrose

fun Item.updateGeneric() {
    decreaseQuality()
    sellIn--
    if (sellIn < 0) {
        decreaseQuality()
    }
}

fun Item.updateAgedBrie() {
    increaseQuality()
    sellIn--
    if (sellIn < 0) {
        increaseQuality()
    }
}

fun Item.updateLegendaryItem() {
    sellIn--
    quality = 80
}

fun Item.increaseQuality(amount: Int = 1) {
    quality = (quality + amount).coerceAtMost(50)
}

fun Item.decreaseQuality(amount: Int = 1) {
    quality = (quality - amount).coerceAtLeast(0)
}

fun Item.updateBackstagePass() {
    increaseQuality()
    if (sellIn <= 10) {
        increaseQuality()
    }
    if (sellIn <= 5) {
        increaseQuality()
    }
    sellIn--
    if (sellIn < 0) {
        quality = 0
    }
}

fun Item.updateConjuredItem() {
    decreaseQuality(2)
    sellIn--
    if (sellIn < 0) {
        decreaseQuality(2)
    }
}

fun Item.toNew(): FNItem {
    return when {
        name == "Aged Brie" -> {
            FNItem(this, Item::updateAgedBrie)
        }
        name == "Sulfuras, Hand of Ragnaros" -> {
            require(quality == 80)
            FNItem(this, Item::updateLegendaryItem)
        }
        name.contains("Backstage pass") -> {
            FNItem(this, Item::updateBackstagePass)
        }
        name.contains("Conjured") -> {
            FNItem(this, Item::updateConjuredItem)
        }
        else -> FNItem(this, Item::updateGeneric)
    }
}

open class Item(var name: String, var sellIn: Int, var quality: Int) {
    override fun toString(): String {
        return this.name + ", " + this.sellIn + ", " + this.quality
    }
}

typealias UpdateQuality = Item.() -> Unit
class FNItem(private val item: Item, private val updateQuality: UpdateQuality) {
    fun update() {
        this.item.updateQuality()
    }
}
