package com.muratdayan.lmplayer.presentation.songs

import com.muratdayan.lmplayer.data.locale.entity.SongModel

data class SongsState(
    val songs: List<SongModel>? = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = "",
)
