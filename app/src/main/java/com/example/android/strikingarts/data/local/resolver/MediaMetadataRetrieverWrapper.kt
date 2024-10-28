package com.example.android.strikingarts.data.local.resolver

import android.media.MediaMetadataRetriever
import javax.inject.Inject

/**
 * This is required because `MediaMetadataRetriever` doesn't implement AutoCloseable in API 28 and
 * lower*/

class MediaMetadataRetrieverWrapper @Inject constructor() : MediaMetadataRetriever(),
    AutoCloseable {
    override fun close() {
        release()
    }
}