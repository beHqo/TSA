package com.github.tsa.domain.audioattributes

import com.github.tsa.data.dickPunchResourceAudioAttributes
import com.github.tsa.data.jabAudioAttributes
import com.github.tsa.data.repository.FakeAudioAttributesRepo
import com.github.tsa.domain.model.SilenceAudioAttributes
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpsertAudioAttributesUseCaseTest {
    private val repository = FakeAudioAttributesRepo()
    private val upsertAudioAttributesUseCase = UpsertAudioAttributesUseCase(repository)

    @Test
    fun `Do not insert SilenceAudioAttributes`() = runTest {
        val id = upsertAudioAttributesUseCase(SilenceAudioAttributes)

        id shouldBe null
    }

    @Test
    fun `Insert ResourceAudioAttributes and return its id`() = runTest {
        val id = upsertAudioAttributesUseCase(dickPunchResourceAudioAttributes)

        id shouldBe dickPunchResourceAudioAttributes.id
    }

    @Test
    fun `Insert UriAudioAttributes and return its id`() = runTest {
        val id = upsertAudioAttributesUseCase(dickPunchResourceAudioAttributes)

        id shouldNotBe null
        id shouldNotBe -1L
    }

    @Test
    fun `When trying to insert an AudioAttributes object that is already in the database, expect its Id`() =
        runTest {
            val id = upsertAudioAttributesUseCase(jabAudioAttributes)

            id shouldBe jabAudioAttributes.id
        }
}