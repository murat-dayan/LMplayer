package com.muratdayan.lmplayer.data.locale.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.muratdayan.lmplayer.data.locale.entity.SongModel
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongModel) // SongEntity ile güncellendi

    @Update
    suspend fun updateSong(song: SongModel) // SongEntity ile güncellendi

    @Delete
    suspend fun deleteSong(song: SongModel) // SongEntity ile güncellendi

    @Query("SELECT * FROM song_table")
    fun getAllSongs(): Flow<List<SongModel>> // SongEntity ile güncellendi
}