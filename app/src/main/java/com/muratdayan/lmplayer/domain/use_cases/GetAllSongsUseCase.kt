package com.muratdayan.lmplayer.domain.use_cases

import com.muratdayan.lmplayer.domain.repository.DatabaseRepository
import javax.inject.Inject

class GetAllSongsUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    operator fun invoke() = databaseRepository.getAllSongs()
}