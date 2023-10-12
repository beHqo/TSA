package com.example.android.strikingarts.data.local.datastore

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    private const val LANGUAGE_KEY = "language"
    private const val THEME_KEY = "theme"
    private const val TECHNIQUE_FORM_KEY = "technique_form"
    private const val PREPARATION_PERIOD_KEY = "preparation_period"

    val LANGUAGE = stringPreferencesKey(LANGUAGE_KEY)
    val THEME = stringPreferencesKey(THEME_KEY)
    val TECHNIQUE_FORM = stringPreferencesKey(TECHNIQUE_FORM_KEY)
    val PREPARATION_PERIOD = intPreferencesKey(PREPARATION_PERIOD_KEY)
}