package com.example.android.strikingarts.domain.model

data class UserPreferences(
    val theme: Theme = Theme.UNSPECIFIED,
    val language: Language = Language.UNSPECIFIED,
    val preparationPeriodSeconds: Int = 5,
    val techniqueRepresentationFormat: TechniqueRepresentationFormat = TechniqueRepresentationFormat.UNSPECIFIED
)