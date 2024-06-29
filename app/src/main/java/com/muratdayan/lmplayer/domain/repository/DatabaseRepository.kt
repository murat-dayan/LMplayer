package com.muratdayan.lmplayer.domain.repository

import com.muratdayan.lmplayer.core.common.Resource
import com.muratdayan.lmplayer.data.locale.entity.SongModel
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    suspend fun insertSong(song: SongModel): Long
    suspend fun updateSong(song: SongModel): Int
    suspend fun deleteSong(song: SongModel): Int
    fun getAllSongs(): Flow<Resource<List<SongModel>>>

}