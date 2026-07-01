package com.example.nnote.features.crud.domain.usecases

import com.example.nnote.features.crud.domain.repository.CrudRepository
import com.example.nnote.core.domain.model.Note
import com.example.nnote.core.data.entity.toNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUseCase @Inject constructor(
    private val repository: CrudRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes().map { noteList ->
            noteList.map { item ->
                item.toNote()
            }
        }
    }

    operator fun invoke(noteId: Int): Flow<Note?> {
        return repository.getNoteById(noteId).map { item ->
            item?.toNote()
        }
    }
}