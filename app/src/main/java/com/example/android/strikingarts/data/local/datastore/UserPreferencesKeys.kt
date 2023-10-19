package com.example.android.strikingarts.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    private const val LANGUAGE_KEY = "language"
    private const val THEME_KEY = "theme"
    private const val TECHNIQUE_FORM_KEY = "technique_form"
    private const val PREPARATION_PERIOD_KEY = "preparation_period"
    private const val SHOW_QUITTERS_DATA_KEY = "show_quitters_data"

    val LANGUAGE = stringPreferencesKey(LANGUAGE_KEY)
    val THEME = stringPreferencesKey(THEME_KEY)
    val TECHNIQUE_FORM = stringPreferencesKey(TECHNIQUE_FORM_KEY)
    val PREPARATION_PERIOD = intPreferencesKey(PREPARATION_PERIOD_KEY)
    val SHOW_QUITTERS_DATA = booleanPreferencesKey(SHOW_QUITTERS_DATA_KEY)
}