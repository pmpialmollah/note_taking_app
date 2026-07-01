package com.example.nnote.core.di

import android.content.Context
import androidx.room.Room
import com.example.nnote.features.crud.data.dao.CrudDao
import com.example.nnote.core.data.room_database.RoomDatabase
import com.example.nnote.features.archive.data.dao.ArchiveDao
import com.example.nnote.features.archive.data.repository.ArchiveRepositoryImpl
import com.example.nnote.features.archive.domain.repository.ArchiveRepository
import com.example.nnote.features.crud.data.repository.CrudRepositoryImpl
import com.example.nnote.features.crud.domain.repository.CrudRepository
import com.example.nnote.features.trash.data.dao.TrashDao
import com.example.nnote.features.trash.data.repository.TrashRepositoryImpl
import com.example.nnote.features.trash.domain.repository.TrashRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RoomDatabase {
        return Room.databaseBuilder(
            context,
            RoomDatabase::class.java,
            "my_databse"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(
        roomDatabase: RoomDatabase
    ): CrudDao {
        return roomDatabase.roomDao()
    }


    @Provides
    @Singleton
    fun provideCrudRepository(
        crudDao: CrudDao
    ): CrudRepository {
        return CrudRepositoryImpl(crudDao)
    }

    @Provides
    @Singleton
    fun provideArchiveDao(
        roomDatabase: RoomDatabase
    ): ArchiveDao {
        return roomDatabase.archiveDao()
    }

    @Provides
    @Singleton
    fun provideArchiveRepository(
        archiveDao: ArchiveDao
    ): ArchiveRepository {
        return ArchiveRepositoryImpl(archiveDao)
    }

    @Provides
    @Singleton
    fun provideTrashDao(
        roomDatabase: RoomDatabase
    ): TrashDao {
        return roomDatabase.trashDao()
    }

    @Provides
    @Singleton
    fun provideTrashRepository(
        trashDao: TrashDao
    ): TrashRepository {
        return TrashRepositoryImpl(trashDao)
    }

}