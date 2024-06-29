package com.muratdayan.lmplayer.domain.use_cases

import com.muratdayan.lmplayer.data.locale.entity.SongModel
import com.muratdayan.lmplayer.domain.repository.DatabaseRepository
import javax.inject.Inject

class InsertSongUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
){
    suspend operator fun invoke(song: SongModel) = databaseRepository.insertSong(song)
}