package com.example.tabatatimer.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tabatatimer.data.ToDoDatabase
import com.example.tabatatimer.data.ToDoRepository
import com.example.tabatatimer.data.ToDoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesToDoDatabase(app: Application): ToDoDatabase {
        return Room.databaseBuilder(
            app,
            ToDoDatabase::class.java,
            "toDo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesToDoRepository(db: ToDoDatabase): ToDoRepository {
        return ToDoRepositoryImpl(db.dao)
    }
}