package com.example.nnote.features.archive.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.nnote.core.data.entity.NoteDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ArchiveDao {

    @Query("SELECT * FROM note_table WHERE isArchived = 1 AND isTrashed = 0")
    fun getArchivedNotes(): Flow<List<NoteDto>>

    @Update
    suspend fun unArchiveNotes(notes: List<NoteDto>): Int


}