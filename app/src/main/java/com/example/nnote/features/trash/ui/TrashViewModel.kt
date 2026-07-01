package com.example.nnote.features.trash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nnote.core.domain.model.Note
import com.example.nnote.features.crud.domain.usecases.TrashUseCase
import com.example.nnote.features.trash.domain.usecase.GetTrashUseCase
import com.example.nnote.features.trash.domain.usecase.TrashDeleteUseCase
import com.example.nnote.features.trash.domain.usecase.TrashRestoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class TrashViewModel @Inject constructor(
    private val getTrashUseCase: GetTrashUseCase,
    private val trashRestoreUseCase: TrashRestoreUseCase,
    private val trashDeleteUseCase: TrashDeleteUseCase,
) : ViewModel() {

    fun getTrashedNotes(): StateFlow<List<Note>> {
        return getTrashUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000.milliseconds),
            initialValue = emptyList()
        )
    }

    fun restoreTrashedNotes(noteList: List<Note>) {
        viewModelScope.launch {
            val result = trashRestoreUseCase(noteList)
        }
    }

    fun deleteTrashedNotes(noteList: List<Note>) {
        viewModelScope.launch {
            val result = trashDeleteUseCase(noteList)
        }
    }

}