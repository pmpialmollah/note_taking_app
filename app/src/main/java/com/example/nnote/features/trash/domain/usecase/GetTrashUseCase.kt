package com.example.nnote.features.trash.domain.usecase

import com.example.nnote.core.domain.model.Note
import com.example.nnote.features.trash.domain.repository.TrashRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrashUseCase @Inject constructor(
    private val trashRepository: TrashRepository
) {

    operator fun invoke(): Flow<List<Note>> {
        return trashRepository.getTrashedNotes()
    }

}