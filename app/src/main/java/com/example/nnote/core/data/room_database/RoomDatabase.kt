package com.example.nnote.core.data.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nnote.features.crud.data.dao.CrudDao
import com.example.nnote.core.data.entity.NoteDto
import com.example.nnote.features.archive.data.dao.ArchiveDao
import com.example.nnote.features.trash.data.dao.TrashDao

@Database(
    entities = [NoteDto::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun roomDao(): CrudDao
    abstract fun archiveDao(): ArchiveDao
    abstract fun trashDao(): TrashDao
}