package com.example.android.strikingarts.domain.usecase.selection

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
    fun `Given a list of Long values, When updateSelectedItems function is invoked, Then the Flow should contain the supplied list`() =
        runTest {

            flow.test { awaitItem() shouldBe emptyList() }

            useCase.updateSelectedItems(idList)

            flow.test { awaitItem() shouldBe idList }
        }

    @Test
    fun `Given a Long value, When the value of newSelectedValue is true, Then the flow should emit a list that contains the Long value`() =
        runTest {

            val newId = 7L
            useCase.onItemSelectionChange(newId, true)

            flow.test { awaitItem().contains(newId) shouldBe true }
        }

    @Test
    fun `Given a Long value, When the value of newSelectedValue is false, Then the flow should emit a list that does not contains the Long value`() =
        runTest {
            val newId = 13L

            useCase.updateSelectedItems(listOf(newId))

            useCase.onItemSelectionChange(newId, false)

            flow.test { awaitItem().contains(newId) shouldBe false }
        }

    @Test
    fun `Given a Long value, Given deselectItem is invoked, Then the flow should emit a list that does not contain any value equal to the Long Value`() =
        runTest {
            useCase.updateSelectedItems(idList)

            useCase.deselectItem(1)

            flow.test { awaitItem().contains(1) shouldBe false }
        }

    @Test
    fun `Given a Long value, Given deselectAllItems is invoked, Then the flow should emit an empty list`() =
        runTest {
            useCase.updateSelectedItems(idList)

            useCase.deselectAllItems()

            flow.test { awaitItem() shouldBe emptyList() }
        }

    @Test
    fun `Given a list of Long values, When selectAllItems function is invoked, Then the Flow should contain the supplied list`() =
        runTest {
            useCase.selectAllItems(idList)

            flow.test { awaitItem() shouldBe idList }
        }

    @Test
    fun `Given a flow that already has list of Long values, When setSelectedQuantity is invoked with a Long value and a positive integer, Then the flow should emit a new list with its last element being the supplied Long value`() =
        runTest {
            useCase.updateSelectedItems(idList)
            val newId = 14L

            useCase.setSelectedQuantity(newId, 1)

            flow.test { awaitItem().last() shouldBe newId }
        }

    @Test
    fun `Given a flow that already has list of Long values, When setSelectedQuantity is invoked with a Long value and -1 integer, Then the flow should emit a new list without the last occurrence of the supplied Long value`() =
        runTest {
            useCase.updateSelectedItems(idList)
            val toBeRemoved = 1L

            flow.test { awaitItem().last() shouldBe toBeRemoved }

            useCase.setSelectedQuantity(toBeRemoved, -1)

            flow.test { awaitItem().last() shouldNotBe toBeRemoved }
        }

    @Test
    fun `Given a flow that already has list of Long values, When setSelectedQuantity is invoked with a Long value that is not included in the list and -1 integer, Then the flow should emit the exact previous list`() =
        runTest {
            useCase.updateSelectedItems(idList)
            val toBeRemoved = 100L

            useCase.setSelectedQuantity(toBeRemoved, -1)

            flow.test { awaitItem() shouldBe idList }
        }
}