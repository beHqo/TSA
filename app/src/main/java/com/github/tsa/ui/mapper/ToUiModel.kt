package com.github.tsa.ui.mapper

import com.github.tsa.domain.model.Combo
import com.github.tsa.domain.model.TechniqueRepresentationFormat

fun Combo.getTechniqueRepresentation(form: TechniqueRepresentationFormat): String =
    this.techniqueList.let { techniqueList ->
        if (form == TechniqueRepresentationFormat.NUM) techniqueList.joinToString { it.num.ifEmpty { it.name } }
        else techniqueList.joinToString { it.name }
    }

