package com.example.nnote.features.crud.domain.usecases

import com.example.nnote.features.crud.domain.repository.CrudRepository
import com.example.nnote.core.domain.model.Note
import javax.inject.Inject

class UpdateUseCase @Inject constructor(
    private val repository: CrudRepository
) {
    suspend operator fun invoke(note: Note): Boolean {
        val result = repository.updateNote(note)
        return result != 0
    }

    suspend operator fun invoke(noteList: List<Note>) : Boolean{
        val result = repository.updateNoteList(noteList)
        return result != 0
    }
}