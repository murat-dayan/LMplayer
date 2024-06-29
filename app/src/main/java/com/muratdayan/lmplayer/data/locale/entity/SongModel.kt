package com.muratdayan.lmplayer.data.locale.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song_table")
data class SongModel(
    @PrimaryKey
    @ColumnInfo(name = "song_id")
    val id: Long,

    @ColumnInfo(name = "song_title")
    val title: String,

    @ColumnInfo(name = "song_artist")
    val artist: String,

    @ColumnInfo(name = "song_path")
    val path: String,

    @ColumnInfo(name = "song_duration")
    val duration: Long
)
