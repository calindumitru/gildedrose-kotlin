package com.gildedrose

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class ItemTest {

    @ParameterizedTest
    @MethodSource("com.gildedrose.ItemTestKt#itemsWhichDecreaseInQuality")
    fun `item quality should not be negative`(itemName: String) {
        val item = Item(itemName, 1, 0)

        item.toUpdatable().update()

        Assertions.assertThat(item.quality).isEqualTo(0)
    }

    @ParameterizedTest
    @MethodSource("com.gildedrose.ItemTestKt#itemsWhichIncreaseInQuality")
    fun `item quality should not surpass 50`(itemName: String) {
        val item = Item(itemName, 100, 0)

        val updatableItem = item.toUpdatable()
        repeat(50) {
            updatableItem.update()
        }

        Assertions.assertThat(item.quality).isLessThanOrEqualTo(50)
    }

    @Test
    fun `Aged Brie item quality should increase`() {
        val item = Item("Aged Brie", 1, 0)

        item.toUpdatable().update()

        Assertions.assertThat(item.quality).isEqualTo(1)
    }

    @ParameterizedTest
    @MethodSource("com.gildedrose.ItemTestKt#items")
    fun `when updating, sell-in should decrease by 1`(itemName: String) {
        val item = Item(itemName, 1, 80)

        item.toUpdatable().update()

        Assertions.assertThat(item.sellIn).isEqualTo(0)
    }

    @Nested
    inner class GenericItemTests {

        @Test
        fun `before surpassing the sell by date, generic item quality should decrease by 1`() {
            val item = Item("generic item", 1, 5)

            item.toUpdatable().update()

            Assertions.assertThat(item.quality).isEqualTo(4)
        }

        @Test
        fun `once the sell by date has passed, generic item quality should decrease by 2`() {
            val item = Item("generic item", 0, 5)

            item.toUpdatable().update()

            Assertions.assertThat(item.quality).isEqualTo(3)
        }
    }

    @Nested
    inner class LegendaryItemTests {

        @Test
        fun `Sulfuras item quality should not change`() {
            val item = Item("Sulfuras, Hand of Ragnaros", 1, 80)

            item.toUpdatable().update()

            Assertions.assertThat(item.quality).isEqualTo(80)
        }

        @Test
        fun `creating Sulfuras with item quality other than 80 should throw an error`() {
            assertThrows<IllegalArgumentException> { Item("Sulfuras, Hand of Ragnaros", 1, 0).toUpdatable() }
        }
    }

    @Nested
    inner class ConjuredItemTests {

        @Test
        fun `before surpassing the sell by date, generic item quality should decrease by 2`() {
            val item = Item("conjured item", 1, 5)

            item.toUpdatable().update()

            Assertions.assertThat(item.quality).isEqualTo(3)
        }

        @Test
        fun `once the sell by date has passed, generic item quality should decrease by 4`() {
            val item = Item("conjured item", 0, 5)

            item.toUpdatable().update()

            Assertions.assertThat(item.quality).isEqualTo(1)
        }
    }

    @Nested
    inner class BackStagePassTests {

        @Test
        fun `when there are more than 10 days before concert, backstage pass item quality should increase by 1`() {
            val item = Item("Backstage passes to a TAFKAL80ETC concert", 11, 0)

            item.toUpdatable().update()

            Assertions.assertThat(item.quality).isEqualTo(1)
        }

        @Test
        fun `when there are 10 days or less before concert, backstage pass item quality should increase by 2`() {
            val item = Item("Backstage passes to a TAFKAL80ETC concert", 10, 0)

            item.toUpdatable().update()

            Assertions.assertThat(item.quality).isEqualTo(2)
        }

        @Test
        fun `when there are 5 days or less before concert, backstage pass item quality should increase by 3`() {
            val item = Item("Backstage passes to a TAFKAL80ETC concert", 5, 0)

            item.toUpdatable().update()

            Assertions.assertThat(item.quality).isEqualTo(3)
        }

        @Test
        fun `after concert, backstage pass item quality should drop to 0`() {
            val item = Item("Backstage passes to a TAFKAL80ETC concert", 0, 10)

            item.toUpdatable().update()

            Assertions.assertThat(item.quality).isEqualTo(0)
        }
    }
}

fun itemsWhichIncreaseInQuality(): Stream<Arguments> {
    return Stream.of(
        Arguments.of("Aged Brie"),
        Arguments.of("Backstage passes to a TAFKAL80ETC concert"),
        Arguments.of("generic backstage pass")
    )
}

fun itemsWhichDecreaseInQuality(): Stream<Arguments> {
    return Stream.of(
        Arguments.of("some generic item"),
        Arguments.of("conjured item")
    )
}

fun items(): Stream<Arguments> {
    return Stream.of(
        Arguments.of("+5 Dexterity Vest"),
        Arguments.of("Aged Brie"),
        Arguments.of("Elixir of the Mongoose"),
        Arguments.of("Sulfuras, Hand of Ragnaros"),
        Arguments.of("Backstage passes to a TAFKAL80ETC concert"),
        Arguments.of("Conjured Mana Cake")
    )
}
