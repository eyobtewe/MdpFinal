package com.mdp.mdpfinal.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.mdp.mdpfinal.data.local.database.AppDatabase
import com.mdp.mdpfinal.data.local.datastore.UserPreferencesRepository
import com.mdp.mdpfinal.data.remote.api.JokeApiService
import com.mdp.mdpfinal.worker.JokeWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "inventory_db").build()
    }

    @Provides
    @Singleton
    fun provideItemDao(database: AppDatabase) = database.itemDao()

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(@ApplicationContext context: Context): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }

    @Provides
    @Singleton
    fun provideJokeApiService(): JokeApiService {
        return Retrofit.Builder()
            .baseUrl("https://official-joke-api.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JokeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
    