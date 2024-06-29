package com.muratdayan.lmplayer.data.locale.repository

import com.muratdayan.lmplayer.data.locale.dao.SongDao
import com.muratdayan.lmplayer.domain.repository.DatabaseRepository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val songDao: SongDao
): DatabaseRepository {
}