package com.github.tsa.domain.selection

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SelectionUseCaseTest {
    private val useCase = SelectionUseCase()
    private val flow = useCase.selectedItemsIdList
    private val idList = listOf<Long>(1, 2, 3, 4, 7, 7, 7, 1)

    @Test
    fun `Flow should emit the provided values`() = runTest {
        flow.test { awaitItem() shouldBe emptyList() }

        useCase.updateSelectedItems(idList)

        flow.test { awaitItem() shouldBe idList }
    }

    @Test
    fun `Select the new value`() = runTest {
        val newId = 7L

        useCase.onItemSelectionChange(newId, true)

        flow.test { awaitItem().contains(newId) shouldBe true }
    }

    @Test
    fun `Deselect the last occurrence of this given value, if possible`() = runTest {
        val newId = 13L

        useCase.updateSelectedItems(listOf(newId))

        useCase.onItemSelectionChange(newId, false)

        flow.test { awaitItem().contains(newId) shouldBe false }
    }

    @Test
    fun `Deselect any values that match the given value`() = runTest {
        useCase.updateSelectedItems(idList)

        useCase.deselectItem(1)

        flow.test { awaitItem().contains(1) shouldBe false }
    }

    @Test
    fun `Deselect everything`() = runTest {
        useCase.updateSelectedItems(idList)

        useCase.deselectAllItems()

        flow.test { awaitItem() shouldBe emptyList() }
    }

    @Test
    fun `Select all`() = runTest {
        useCase.selectAllItems(idList)

        flow.test { awaitItem() shouldBe idList }
    }

    @Test
    fun `Select {$newQuantity} number of {$newId} values and append them to the end`() = runTest {
        useCase.updateSelectedItems(idList)
        val newId = 14L

        useCase.setSelectedQuantity(newId, 1)

        flow.test { awaitItem().last() shouldBe newId }
    }

    @Test
    fun `Deselect the last occurrence of the given value, if possible`() = runTest {
        useCase.updateSelectedItems(idList)
        val toBeRemoved = 1L

        flow.test { awaitItem().last() shouldBe toBeRemoved }

        useCase.setSelectedQuantity(toBeRemoved, -1)

        flow.test { awaitItem().last() shouldNotBe toBeRemoved }
    }

    @Test
    fun `When trying to deselect an item that is not selected, do nothing`() = runTest {
        useCase.updateSelectedItems(idList)
        val toBeRemoved = 100L

        useCase.setSelectedQuantity(toBeRemoved, -1)

        flow.test { awaitItem() shouldBe idList }
    }
}