package com.example.nnote.features.archive.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nnote.core.domain.model.Note
import com.example.nnote.features.archive.domain.usecase.GetArchivedUseCase
import com.example.nnote.features.archive.domain.usecase.UnArchiveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val archivedUseCase: GetArchivedUseCase,
    private val unArchivedUseCase: UnArchiveUseCase
): ViewModel() {


    fun getArchivedNotes(): StateFlow<List<Note>>{
        return archivedUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000.milliseconds),
            initialValue = emptyList()
        )
    }

    fun unArchiveNotes(notes: List<Note>){
        viewModelScope.launch {
            val result = unArchivedUseCase(notes)
        }
    }
    
}