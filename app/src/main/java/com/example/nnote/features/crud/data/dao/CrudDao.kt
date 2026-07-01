package com.example.nnote.features.crud.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nnote.core.data.entity.NoteDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CrudDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteDto: NoteDto)

    @Query("SELECT * FROM note_table WHERE isArchived = 0 AND isTrashed = 0 ORDER BY id DESC")
    fun getAllNote(): Flow<List<NoteDto>>

    @Query("SELECT * FROM note_table WHERE id = :noteId")
    fun getNoteById(noteId: Int): Flow<NoteDto?>

    @Update
    suspend fun updateNote(noteDto: NoteDto): Int

    @Update
    suspend fun updateNoteList(dataNoteList: List<NoteDto>): Int

}