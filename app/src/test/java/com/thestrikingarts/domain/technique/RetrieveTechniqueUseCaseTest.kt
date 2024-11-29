package com.thestrikingarts.domain.technique

import com.thestrikingarts.data.jab
import com.thestrikingarts.data.repository.FakeTechniqueRepository
import com.thestrikingarts.data.spearElbowNotInDB
import com.thestrikingarts.domain.model.Technique
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveTechniqueUseCaseTest {
    private val repository = FakeTechniqueRepository()
    private val useCase = RetrieveTechniqueUseCase(repository)

    @Test
    fun `Retrieve technique by id`() = runTest {
        useCase(jab.id) shouldBe jab
    }

    @Test
    fun `Return the a Technique object with default value when the provided id does not refer to any techniques in the database`() =
        runTest {
            useCase(spearElbowNotInDB.id) shouldBe Technique()
        }
}