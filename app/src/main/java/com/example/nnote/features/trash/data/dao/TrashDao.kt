package com.example.nnote.features.trash.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.example.nnote.core.data.entity.NoteDto
import kotlinx.coroutines.flow.Flow

@Dao
interface TrashDao {

    @Query("SELECT * FROM note_table WHERE isTrashed = 1")
    fun getTrashedNotes(): Flow<List<NoteDto>>

    @Delete
    suspend fun deleteNoteList(noteDtoList: List<NoteDto>): Int

    @Update
    suspend fun restoreNoteList(noteDtoList: List<NoteDto>): Int

}