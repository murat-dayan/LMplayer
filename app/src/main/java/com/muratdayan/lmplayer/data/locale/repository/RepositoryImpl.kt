package com.muratdayan.lmplayer.data.locale.repository

import com.muratdayan.lmplayer.core.common.Resource
import com.muratdayan.lmplayer.data.locale.dao.SongDao
import com.muratdayan.lmplayer.data.locale.entity.SongModel
import com.muratdayan.lmplayer.domain.repository.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val songDao: SongDao
): DatabaseRepository {
    override suspend fun insertSong(song: SongModel): Long {
        return songDao.insertSong(song)
    }

    override suspend fun updateSong(song: SongModel): Int {
        return songDao.updateSong(song)
    }

    override suspend fun deleteSong(song: SongModel): Int {
        return songDao.deleteSong(song)
    }

    override fun getAllSongs(): Flow<Resource<List<SongModel>>> = flow {
        emit(Resource.Loading())

        val songs = songDao.getAllSongs()
        emit(Resource.Success(songs))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(Resource.Error(it.message.toString()))
        }
}