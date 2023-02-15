package com.darvis.data.di

import android.content.Context
import androidx.room.Room
import com.darvis.data.DataStoreManager
import com.darvis.data.db.Dao
import com.darvis.data.db.Database
import com.darvis.data.db.NotificationEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideNotificationDb(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context, Database::class.java, Database.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideNotificationDAO(database: Database): Dao<NotificationEntity> {
        return database.Dao()
    }

    @Singleton
    @Provides
    fun provideDataStoreManager(@ApplicationContext context: Context) =
        DataStoreManager(context = context)
}