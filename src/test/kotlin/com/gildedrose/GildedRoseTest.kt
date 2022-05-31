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
    fun `should not add or remove items`() {
        val items = arrayOf(Item("foo", 0, 0))

        val app = GildedRose(items)
        app.updateQuality()

        assertThat(app.items).hasSize(1)
    }
}
