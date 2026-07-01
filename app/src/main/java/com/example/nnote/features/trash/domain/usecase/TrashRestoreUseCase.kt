package com.example.nnote.features.trash.domain.usecase

import com.example.nnote.core.domain.model.Note
import com.example.nnote.features.trash.domain.repository.TrashRepository
import javax.inject.Inject

class TrashRestoreUseCase @Inject constructor(
    private val trashRepository: TrashRepository
) {

    suspend operator fun invoke(noteList: List<Note>): Boolean {
        val result = trashRepository.restoreNoteList(noteList)
        return result != 0
    }

}