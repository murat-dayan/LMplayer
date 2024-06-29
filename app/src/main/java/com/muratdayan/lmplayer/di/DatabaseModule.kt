package com.muratdayan.lmplayer.di

import android.content.Context
import androidx.room.Room
import com.muratdayan.lmplayer.core.common.Constants
import com.muratdayan.lmplayer.data.locale.dao.SongDao
import com.muratdayan.lmplayer.data.locale.database.LMPlayerDatabase
import com.muratdayan.lmplayer.data.locale.repository.RepositoryImpl
import com.muratdayan.lmplayer.domain.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : LMPlayerDatabase{
        return Room.databaseBuilder(
            context,
            LMPlayerDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideSongDao(database: LMPlayerDatabase): SongDao{
        return database.getLMPlayerDatabase()
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(songDao: SongDao): DatabaseRepository {
        return RepositoryImpl(songDao)
    }


}