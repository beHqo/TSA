package com.github.tsa.domain.model

data class UserPreferences(
    val dynamicColorsEnabled: Boolean = false,
    val theme: Theme = Theme.UNSPECIFIED,
    val language: Language = Language.UNSPECIFIED,
    val preparationPeriodSeconds: Int = 5,
    val techniqueRepresentationFormat: TechniqueRepresentationFormat = TechniqueRepresentationFormat.UNSPECIFIED,
    val showQuittersData: Boolean = false
)