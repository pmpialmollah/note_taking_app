package com.example.nnote.features.archive.domain.repository

import com.example.nnote.core.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface ArchiveRepository {
    fun getArchivedNotes(): Flow<List<Note>>
    suspend fun unArchiveNotes(notes: List<Note>): Boolean
}