package com.example.android.strikingarts.domain.usecase.technique

import com.example.android.strikingarts.data.jab
import com.example.android.strikingarts.data.repository.FakeTechniqueRepository
import com.example.android.strikingarts.data.spearElbowNotInDB
import com.example.android.strikingarts.domain.model.Technique
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RetrieveTechniqueUseCaseTest {
    private val repository = FakeTechniqueRepository()
    private val useCase = RetrieveTechniqueUseCase(repository)

    @Test
    fun `Given a database that stores a number of Technique objects, When the supplied id points to an Technique in the database, then it should retrieve that Technique`() =
        runTest {
            useCase(jab.id) shouldBe jab
        }

    @Test
    fun `Given a database that stores a number of Technique objects, When the supplied id does not point to any Technique in the database, then it should return Technique with default values`() =
        runTest {
            useCase(spearElbowNotInDB.id) shouldBe Technique()
        }
}