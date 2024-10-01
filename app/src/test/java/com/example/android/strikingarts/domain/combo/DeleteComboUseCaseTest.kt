package com.example.android.strikingarts.domain.combo

import com.example.android.strikingarts.data.crossStepBackCrossLeadHook
import com.example.android.strikingarts.data.jabCrossJab
import com.example.android.strikingarts.data.longComboNotInDB
import com.example.android.strikingarts.data.rearHighKickStepForwardSlashingElbowNotInDB
import com.example.android.strikingarts.data.repository.FakeComboRepository
import com.example.android.strikingarts.data.stepBackLeadHighKick
import com.example.android.strikingarts.data.stepForwardSpearElbowNotInDB
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteComboUseCaseTest {
    private val repository = FakeComboRepository()
    private val useCase = DeleteComboUseCase(repository)

    @Test
    fun `A combo that already exists in the database should be removed`() = runTest {
        val comboId = jabCrossJab.id

        val affectedRows = useCase(comboId)
        affectedRows shouldBe 1L

        repository.doesDatabaseContainComboWithIdOf(comboId) shouldBe false
    }

    @Test
    fun `A list of combos that already exist in the database should be removed`() = runTest {
        val list = listOf(crossStepBackCrossLeadHook.id, stepBackLeadHighKick.id)

        val affectedRows = useCase(list)
        affectedRows shouldBe list.size.toLong()

        list.forEach { id -> repository.doesDatabaseContainComboWithIdOf(id) shouldBe false }
    }

    @Test
    fun `Nothing should happen when the provided id for deletion does not refer to any objects in the database`() =
        runTest {
            val toBeDeleted = longComboNotInDB

            val affectedRows = useCase(toBeDeleted.id)
            affectedRows shouldBe 0L
        }

    @Test
    fun `Nothing should happen when the provided list of ids for deletion does not refer to any objects in the database`() =
        runTest {
            val list = listOf(
                stepForwardSpearElbowNotInDB.id, rearHighKickStepForwardSlashingElbowNotInDB.id
            )

            val affectedRows = useCase(list)
            affectedRows shouldBe 0L
        }
}