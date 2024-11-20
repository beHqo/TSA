package com.github.tsa.domain.technique

import com.github.tsa.data.dickSlap
import com.github.tsa.data.jab
import com.github.tsa.data.local.util.assertTechniquesAreEqual
import com.github.tsa.data.repository.FakeAudioAttributesRepo
import com.github.tsa.data.repository.FakeTechniqueRepository
import com.github.tsa.domain.audioattributes.UpsertAudioAttributesUseCase
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpsertTechniqueUseCaseTest {
    private val repository = FakeTechniqueRepository()
    private val useCase =
        UpsertTechniqueUseCase(repository, UpsertAudioAttributesUseCase(FakeAudioAttributesRepo()))

    @Test
    fun `If the provided technique is not already in the database, insert it`() = runTest {
        useCase(dickSlap)

        assertTechniquesAreEqual(repository.getLastInsertedTechnique(), dickSlap)
    }

    @Test
    fun `If the provided technique is already in the database, update it`() = runTest {
        val newName = "JabCopied"
        useCase(jab.copy(name = newName))

        repository.getTechnique(jab.id).name shouldBe newName
    }
}