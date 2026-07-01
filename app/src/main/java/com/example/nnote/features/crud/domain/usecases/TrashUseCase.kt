package com.example.nnote.features.crud.domain.usecases

import com.example.nnote.core.domain.model.Note
import com.example.nnote.features.crud.domain.repository.CrudRepository
import javax.inject.Inject

class TrashUseCase @Inject constructor(
    private val repository: CrudRepository
) {

    suspend operator fun invoke(noteList: List<Note>) : Boolean{
        val result = repository.moveNoteListToTrash(noteList)
        return result != 0
    }


}