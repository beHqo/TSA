package com.example.android.strikingarts.domain.usecase.audioattributes

import com.example.android.strikingarts.data.assetAudioAttributes
import com.example.android.strikingarts.data.jabAudioAttributes
import com.example.android.strikingarts.data.repository.FakeAudioAttributesRepo
import com.example.android.strikingarts.domain.model.SilenceAudioAttributes
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpsertAudioAttributesUseCaseTest {
    private val repository = FakeAudioAttributesRepo()
    private val upsertAudioAttributesUseCase = UpsertAudioAttributesUseCase(repository)

    @Test
    fun `Given SilenceAudioAttributes, when operation is invoked, then expect null`() = runTest {
        val id = upsertAudioAttributesUseCase(SilenceAudioAttributes)

        id shouldBe null
    }

    @Test
    fun `Given AssetAudioAttributes, when operation is invoked, then expect its id`() = runTest {
        val id = upsertAudioAttributesUseCase(assetAudioAttributes)

        id shouldBe assetAudioAttributes.id
    }

    @Test
    fun `Given UriAudioAttributes, when it's not already in the database, then expect its id`() =
        runTest {
            val id = upsertAudioAttributesUseCase(assetAudioAttributes)

            id shouldNotBe null
            id shouldNotBe -1L
        }

    @Test
    fun `Given UriAudioAttributes, when it's already in the database, then expect its id`() =
        runTest {
            val id = upsertAudioAttributesUseCase(jabAudioAttributes)

            id shouldBe jabAudioAttributes.id
        }
}