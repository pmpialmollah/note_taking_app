package com.example.nnote.features.trash.domain.repository

import com.example.nnote.core.domain.model.Note
import kotlinx.coroutines.flow.Flow


interface TrashRepository{

    fun getTrashedNotes(): Flow<List<Note>>


    suspend fun deleteNoteList(noteList: List<Note>): Int


    suspend fun restoreNoteList(noteList: List<Note>): Int

}