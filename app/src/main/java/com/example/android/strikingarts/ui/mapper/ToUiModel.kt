package com.example.android.strikingarts.ui.mapper

import com.example.android.strikingarts.domain.model.Combo
import com.example.android.strikingarts.domain.model.TechniqueRepresentationFormat

fun Combo.getTechniqueRepresentation(form: TechniqueRepresentationFormat): String =
    this.techniqueList.let { techniqueList ->
        if (form == TechniqueRepresentationFormat.NUM) techniqueList.joinToString { it.num.ifEmpty { it.name } }
        else techniqueList.joinToString { it.name }
    }

