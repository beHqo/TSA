package com.thestrikingarts.ui.mapper

import com.thestrikingarts.domain.model.Combo
import com.thestrikingarts.domain.model.TechniqueRepresentationFormat

fun Combo.getTechniqueRepresentation(form: TechniqueRepresentationFormat): String =
    this.techniqueList.let { techniqueList ->
        if (form == TechniqueRepresentationFormat.NUM) techniqueList.joinToString { it.num.ifEmpty { it.name } }
        else techniqueList.joinToString { it.name }
    }

