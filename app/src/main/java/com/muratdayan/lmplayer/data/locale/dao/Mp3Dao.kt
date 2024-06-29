package com.muratdayan.lmplayer.data.locale.dao

import androidx.room.Dao
import androidx.room.Query
import com.muratdayan.lmplayer.data.locale.entity.Mp3File

@Dao
interface Mp3Dao {

    @Query("SELECT * FROM Mp3File")
    suspend fun getAllMp3Files(): List<Mp3File>
}