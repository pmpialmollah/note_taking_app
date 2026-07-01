package com.example.nnote.features.archive.domain.usecase

import com.example.nnote.features.crud.domain.repository.CrudRepository
import com.example.nnote.core.domain.model.Note
import com.example.nnote.features.archive.domain.repository.ArchiveRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetArchivedUseCase @Inject constructor(
    val repository: ArchiveRepository
) {


    operator fun invoke(): Flow<List<Note>>{
        return repository.getArchivedNotes()
    }


}