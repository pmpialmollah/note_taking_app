package com.example.nnote.features.crud.domain.repository

import com.example.nnote.core.domain.model.Note
import com.example.nnote.core.data.entity.NoteDto
import kotlinx.coroutines.flow.Flow

interface CrudRepository {

    suspend fun insertNote(note: Note)

    fun getAllNotes(): Flow<List<NoteDto>>

    fun getNoteById(noteId: Int): Flow<NoteDto?>

    suspend fun updateNote(note: Note): Int
    suspend fun updateNoteList(noteList: List<Note>): Int

    suspend fun moveNoteListToTrash(noteList: List<Note>): Int

    suspend fun restoreNoteFromTrash(note: Note)

    suspend fun restoreNoteListFromTrash(noteList: List<Note>)
//
//    suspend fun deleteNote(note: Note): Int
//
//    suspend fun deleteNoteList(noteDtoList: List<Note>): Int

}