package com.gildedrose

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GildedRoseTest {

    @Test
    fun `should not mutate the item name`() {
        val items = arrayOf(Item("foo", 0, 0))

        val app = GildedRose(items)
        app.updateQuality()
        val actualName = app.items[0].name

        assertThat(actualName).isEqualTo("foo")
    }

    @Test
    fun `item quality should not be negative`() {
        val items = arrayOf(Item("Item", 1, 0))

        val app = GildedRose(items)
        app.updateQuality()
        val actualQuality = app.items[0].quality

        assertThat(0).isEqualTo(actualQuality)
    }

    @Test
    fun `"Aged Brie" item quality should increase`() {
        val item = Item("Aged Brie", 1, 0)

        item.toNew().update()

        assertThat(item.quality).isEqualTo(1)
    }

    @Test
    fun `item quality should not surpass 50`() {
        val items = arrayOf(Item("Aged Brie", 51, 0), Item("Backstage passes to a TAFKAL80ETC concert", 51, 0))

        val app = GildedRose(items)
        repeat(51) {
            app.updateQuality()
        }

        val actualQualityBrie = app.items[0].quality
        val actualQualityBackstagePass = app.items[0].quality
        assertThat(actualQualityBrie).isEqualTo(50)
        assertThat(actualQualityBackstagePass).isEqualTo(50)
    }

    @Test
    fun `"Sulfuras" item quality should not decrease`() {
        val items = arrayOf(Item("Sulfuras, Hand of Ragnaros", 1, 80))

        val app = GildedRose(items)
        app.updateQuality()
        val actualQuality = app.items[0].quality

        assertThat(actualQuality).isEqualTo(80)
    }

    @Test
    fun `when there are 10 days or less, backstage pass item quality should increase by 2`() {
        val items = arrayOf(Item("Backstage passes to a TAFKAL80ETC concert", 10, 0))

        val app = GildedRose(items)
        app.updateQuality()
        val actualQuality = app.items[0].quality

        assertThat(actualQuality).isEqualTo(2)
    }

    @Test
    fun `when there are 5 days or less, backstage pass item quality should increase by 3`() {
        val items = arrayOf(Item("Backstage passes to a TAFKAL80ETC concert", 5, 0))

        val app = GildedRose(items)
        app.updateQuality()
        val actualQuality = app.items[0].quality

        assertThat(actualQuality).isEqualTo(3)
    }

    @Test
    fun `after concert, backstage pass item quality should drop to 0`() {
        val items = arrayOf(Item("Backstage passes to a TAFKAL80ETC concert", 0, 10))

        val app = GildedRose(items)
        app.updateQuality()
        val actualQuality = app.items[0].quality

        assertThat(actualQuality).isEqualTo(0)
    }
}
