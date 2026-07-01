package com.example.nnote.features.archive.data.repository

import com.example.nnote.core.data.entity.NoteDto
import com.example.nnote.core.data.entity.toNote
import com.example.nnote.core.domain.model.Note
import com.example.nnote.features.archive.data.dao.ArchiveDao
import com.example.nnote.features.archive.domain.repository.ArchiveRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArchiveRepositoryImpl @Inject constructor(
    private val archiveDao: ArchiveDao
) : ArchiveRepository {

    override fun getArchivedNotes(): Flow<List<Note>> {
        return archiveDao.getArchivedNotes().map {
            it.map { noteDto ->
                noteDto.toNote()
            }
        }
    }

    override suspend fun unArchiveNotes(notes: List<Note>): Boolean {
        val result = archiveDao.unArchiveNotes(notes.map { note ->
            NoteDto(
                id = note.id,
                title = note.title,
                body = note.body,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt,
                isArchived = false,
                isTrashed = note.isTrashed,
            )
        })

        return result != 0

    }
}