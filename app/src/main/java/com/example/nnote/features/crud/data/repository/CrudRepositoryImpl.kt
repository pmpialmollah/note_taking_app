package com.example.nnote.features.crud.data.repository

import com.example.nnote.features.crud.data.dao.CrudDao
import com.example.nnote.core.domain.model.Note
import com.example.nnote.core.data.entity.NoteDto
import com.example.nnote.features.crud.domain.repository.CrudRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CrudRepositoryImpl @Inject constructor(
    val crudDao: CrudDao
) : CrudRepository {
    override suspend fun insertNote(note: Note) {
        val noteDto = NoteDto(
            title = note.title,
            body = note.body,
            createdAt = note.createdAt,
        )
        crudDao.insertNote(noteDto)
    }

    override fun getAllNotes(): Flow<List<NoteDto>> {
        return crudDao.getAllNote()
    }

    override fun getNoteById(noteId: Int): Flow<NoteDto?> {
        return crudDao.getNoteById(noteId)
    }


    override suspend fun updateNote(note: Note): Int {
        val noteDto = NoteDto(
            id = note.id,
            title = note.title,
            body = note.body,
            createdAt = note.createdAt,
            updatedAt = note.updatedAt,
            isTrashed = note.isTrashed,
            isArchived = note.isArchived,
        )
        return crudDao.updateNote(noteDto)
    }

    override suspend fun updateNoteList(noteList: List<Note>): Int {
        val modifiedNoteList = noteList.map { note ->
            NoteDto(
                id = note.id,
                title = note.title,
                body = note.body,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt,
                isTrashed = note.isTrashed,
                isArchived = note.isArchived,
            )
        }
        return crudDao.updateNoteList(modifiedNoteList)
    }

    override suspend fun moveNoteListToTrash(noteList: List<Note>): Int {
        val modifiedNoteList = noteList.map { note ->
            NoteDto(
                id = note.id,
                title = note.title,
                body = note.body,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt,
                isTrashed = true,
                isArchived = note.isArchived,
            )
        }
        return crudDao.updateNoteList(modifiedNoteList)
    }

    override suspend fun restoreNoteFromTrash(note: Note) {
        val noteDto = NoteDto(
            id = note.id,
            title = note.title,
            body = note.body,
            createdAt = note.createdAt,
            updatedAt = note.updatedAt,
            isTrashed = false,
            isArchived = note.isArchived,
        )
        crudDao.updateNote(noteDto)
    }

    override suspend fun restoreNoteListFromTrash(noteList: List<Note>) {
        val modifiedNoteList = noteList.map { note ->
            NoteDto(
                id = note.id,
                title = note.title,
                body = note.body,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt,
                isTrashed = false,
                isArchived = note.isArchived,
            )
        }
        crudDao.updateNoteList(modifiedNoteList)
    }

//    override suspend fun deleteNote(note: Note): Int {
//        val noteDto = NoteDto(
//            id = note.id,
//            title = note.title,
//            body = note.body,
//            createdAt = note.createdAt,
//        )
//        return crudDao.deleteNote(noteDto)
//    }
//
//    override suspend fun deleteNoteList(noteDtoList: List<Note>): Int {
//        return crudDao.deleteNoteList(noteDtoList.map {
//            NoteDto(
//                id = it.id,
//                title = it.title,
//                body = it.body,
//                createdAt = it.createdAt,
//            )
//        })
//    }

}