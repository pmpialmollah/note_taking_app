package com.example.nnote.features.crud.domain.usecases

import com.example.nnote.features.crud.domain.repository.CrudRepository
import com.example.nnote.core.domain.model.Note
import javax.inject.Inject

class AddUseCase @Inject constructor(
    private val repository: CrudRepository
) {
    suspend operator fun invoke(note: Note): Boolean{
        repository.insertNote(note)
        return true
    }
}