package com.muratdayan.lmplayer.data.locale.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muratdayan.lmplayer.data.locale.dao.SongDao
import com.muratdayan.lmplayer.data.locale.entity.SongModel

@Database(entities = [SongModel::class], version = 1, exportSchema = false)
abstract class LMPlayerDatabase : RoomDatabase() {

    abstract fun getLMPlayerDatabase(): SongDao
}