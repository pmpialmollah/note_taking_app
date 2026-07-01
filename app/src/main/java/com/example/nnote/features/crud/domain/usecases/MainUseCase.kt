package com.example.nnote.features.crud.domain.usecases

import com.example.nnote.features.crud.domain.repository.CrudRepository
import javax.inject.Inject

class MainUseCase @Inject constructor(
    val repository: CrudRepository
) {

    suspend fun checkIsInsertable(title: String, body: String): Boolean {
        return title.isNotEmpty() || body.isNotEmpty()
    }

}