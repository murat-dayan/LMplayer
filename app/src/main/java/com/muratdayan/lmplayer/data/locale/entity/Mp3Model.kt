package com.muratdayan.lmplayer.data.locale.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Mp3File(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val path: String
)
