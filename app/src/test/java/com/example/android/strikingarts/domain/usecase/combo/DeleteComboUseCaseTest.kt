package com.example.android.strikingarts.domain.usecase.combo

import com.example.android.strikingarts.data.crossStepBackCrossLeadHook
import com.example.android.strikingarts.data.jabCrossJab
import com.example.android.strikingarts.data.repository.FakeComboRepository
import com.example.android.strikingarts.data.stepBackLeadHighKick
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteComboUseCaseTest {
    private val repository = FakeComboRepository()
    private val useCase = DeleteComboUseCase(repository)

    @Test
    fun `Given a database pre-populated with Combo objects, When an id of a combo that already exists in the database is supplied, Then it should be removed`() =
        runTest {
            val comboId = jabCrossJab.id

            useCase(comboId)

            repository.doesDatabaseContainComboWithIdOf(comboId) shouldBe false
        }

    @Test
    fun `Given a database pre-populated with Combo objects, When a list of ids of combos that already exists in the database is supplied, Then all the combos should be removed`() =
        runTest {
            val list = listOf(crossStepBackCrossLeadHook.id, stepBackLeadHighKick.id)

            useCase(list)

            list.forEach { id -> repository.doesDatabaseContainComboWithIdOf(id) shouldBe false }
        }
}