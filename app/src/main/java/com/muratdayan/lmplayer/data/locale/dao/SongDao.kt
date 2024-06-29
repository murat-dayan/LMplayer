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
    suspend fun insertSong(song: SongModel) : Long // SongEntity ile güncellendi

    @Update
    suspend fun updateSong(song: SongModel) : Int // SongEntity ile güncellendi

    @Delete
    suspend fun deleteSong(song: SongModel) : Int // SongEntity ile güncellendi

    @Query("SELECT * FROM song_table")
    suspend fun getAllSongs(): List<SongModel> // SongEntity ile güncellendi
}