package com.example.nnote.features.trash.data.repository

import com.example.nnote.core.data.entity.NoteDto
import com.example.nnote.core.data.entity.toNote
import com.example.nnote.core.domain.model.Note
import com.example.nnote.features.trash.data.dao.TrashDao
import com.example.nnote.features.trash.domain.repository.TrashRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrashRepositoryImpl @Inject constructor(
    private val trashDao: TrashDao
) : TrashRepository {

    override fun getTrashedNotes(): Flow<List<Note>> {
        return trashDao.getTrashedNotes().map { list ->
            list.map { it.toNote() }
        }
    }

    override suspend fun deleteNoteList(noteList: List<Note>): Int {
        val modifiedNoteList = noteList.map { note ->
            NoteDto(
                id = note.id,
                title = note.title,
                body = note.body,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt,
                isArchived = note.isArchived,
                isTrashed = note.isTrashed,
            )
        }
        return trashDao.deleteNoteList(modifiedNoteList)
    }

    override suspend fun restoreNoteList(noteList: List<Note>): Int {
        val modifiedNoteList = noteList.map { note ->
            NoteDto(
                id = note.id,
                title = note.title,
                body = note.body,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt,
                isArchived = note.isArchived,
                isTrashed = false,
            )
        }
        return trashDao.restoreNoteList(modifiedNoteList)
    }
}