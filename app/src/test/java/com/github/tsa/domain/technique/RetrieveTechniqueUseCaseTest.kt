package com.github.tsa.domain.technique

import com.github.tsa.data.jab
import com.github.tsa.data.repository.FakeTechniqueRepository
import com.github.tsa.data.spearElbowNotInDB
import com.github.tsa.domain.model.Technique
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