package com.example.android.strikingarts.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.android.strikingarts.R
import com.example.android.strikingarts.database.entity.Technique
import com.example.android.strikingarts.mediaplayer.TechniquePlayer

private fun getTechniqueList(): List<Technique> {
    val jab = Technique(name = "Jab",
        num = "1",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Punch")
    val cross = Technique(name = "Cross",
        num = "2",
        sound = R.raw.yummy_pie,
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Punch")
    val leadHook = Technique(name = "Lead Hook",
        num = "3",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Punch")
    val rearHook = Technique(name = "Rear Hook",
        num = "4",
        canBeFaint = true,
        canBeBodyshot = true,
        techniqueType = "Punch")
    val leadUppercut = Technique(name = "Lead Uppercut",
        num = "5",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Punch")
    val rearUppercut = Technique(name = "Rear Uppercut",
        num = "6",
        canBeFaint = true,
        canBeBodyshot = false,
        techniqueType = "Punch")
    val idk = Technique(name = "Idk")

    return listOf(
        jab,
        cross,
        leadHook,
        rearHook,
        leadUppercut,
//        rearUppercut,
        idk
    )
}


@Composable
fun PlayAll(techniqueList: List<Technique> = getTechniqueList()) {
    val context = LocalContext.current
    val techniquePlayer = TechniquePlayer(context, techniqueList)

    Row {
        IconButton(onClick = {
            techniquePlayer.play()
        }) { Icon(Icons.Sharp.Cancel, null) }
        IconButton(onClick = {
            techniquePlayer.pause()
        }) { Icon(Icons.Sharp.Cancel, null) }
    }

}