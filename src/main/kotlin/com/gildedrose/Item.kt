package com.gildedrose

import com.gildedrose.ItemConstants.LEGENDARY_ITEM_QUALITY

open class Item(var name: String, var sellIn: Int, var quality: Int) {
    override fun toString(): String {
        return this.name + ", " + this.sellIn + ", " + this.quality
    }
}

typealias UpdateQuality = Item.() -> Unit
class SelfUpdatableItem(private val item: Item, private val updateQuality: UpdateQuality) {
    fun update() {
        this.item.updateQuality()
    }
}

fun Item.toUpdatable(): SelfUpdatableItem {
    return when {
        name == "Aged Brie" -> {
            SelfUpdatableItem(this, Item::updateAgedBrie)
        }
        name == "Sulfuras, Hand of Ragnaros" -> {
            require(quality == LEGENDARY_ITEM_QUALITY)
            SelfUpdatableItem(this, Item::updateLegendary)
        }
        name.contains("Backstage pass", true) -> {
            SelfUpdatableItem(this, Item::updateBackstagePass)
        }
        name.contains("Conjured", true) -> {
            SelfUpdatableItem(this, Item::updateConjured)
        }
        else -> SelfUpdatableItem(this, Item::updateGeneric)
    }
}

object ItemConstants {
    const val LEGENDARY_ITEM_QUALITY = 80
}

private fun Item.increaseQuality(amount: Int = 1) {
    quality = (quality + amount).coerceAtMost(50)
}

private fun Item.decreaseQuality(amount: Int = 1) {
    quality = (quality - amount).coerceAtLeast(0)
}

private fun Item.updateGeneric() {
    decreaseQuality()
    sellIn--
    if (sellIn < 0) {
        decreaseQuality()
    }
}

private fun Item.updateAgedBrie() {
    increaseQuality()
    sellIn--
    if (sellIn < 0) {
        increaseQuality()
    }
}

private fun Item.updateLegendary() {
    sellIn--
    quality = LEGENDARY_ITEM_QUALITY
}

private fun Item.updateBackstagePass() {
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

private fun Item.updateConjured() {
    decreaseQuality(2)
    sellIn--
    if (sellIn < 0) {
        decreaseQuality(2)
    }
}
