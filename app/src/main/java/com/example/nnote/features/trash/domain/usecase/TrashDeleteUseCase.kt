package com.example.nnote.features.trash.domain.usecase

import com.example.nnote.core.domain.model.Note
import com.example.nnote.features.trash.data.repository.TrashRepositoryImpl
import javax.inject.Inject

class TrashDeleteUseCase @Inject constructor(
    private val repository: TrashRepositoryImpl
) {

    suspend operator fun invoke(noteList: List<Note>): Boolean {
        val result = repository.deleteNoteList(noteList)

        return result != 0
    }

}