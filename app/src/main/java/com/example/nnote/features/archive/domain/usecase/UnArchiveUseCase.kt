package com.example.nnote.features.archive.domain.usecase

import com.example.nnote.core.domain.model.Note
import com.example.nnote.features.archive.domain.repository.ArchiveRepository
import javax.inject.Inject

class UnArchiveUseCase @Inject constructor(
    private val repository: ArchiveRepository
) {

    suspend operator fun invoke(notes: List<Note>): Boolean{
        return repository.unArchiveNotes(notes)
    }

}