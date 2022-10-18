package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.data.source.local.room.VideoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Created by 김현국 2022/10/17
 * @Time 6:59 PM
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule{

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context) : VideoDatabase {
        return Room.databaseBuilder(
            context,
            VideoDatabase::class.java,
            "VideoRoomDatabase"
        ).build()
    }
}
