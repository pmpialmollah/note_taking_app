package com.example.nnote.features.crud.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nnote.core.domain.model.Note
import com.example.nnote.features.crud.domain.usecases.AddUseCase
import com.example.nnote.features.crud.domain.usecases.GetUseCase
import com.example.nnote.features.crud.domain.usecases.MainUseCase
import com.example.nnote.features.crud.domain.usecases.TrashUseCase
import com.example.nnote.features.crud.domain.usecases.UpdateUseCase
import com.example.nnote.features.trash.domain.usecase.TrashDeleteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUseCase: GetUseCase,
    private val addUseCase: AddUseCase,
    private val updateUseCase: UpdateUseCase,
    private val deleteUseCase: TrashDeleteUseCase,
    private val trashUseCase: TrashUseCase,
    private val mainUseCase: MainUseCase,
) : ViewModel() {

    private val _titleStateFlow = MutableStateFlow<String>("")
    private val _bodyStateFlow = MutableStateFlow<String>("")

    private val _isNoteSavedSharedFlow = MutableSharedFlow<Boolean>()
    val isNoteSaved = _isNoteSavedSharedFlow.asSharedFlow()

    private val _isNoteUpdatedSharedFlow = MutableSharedFlow<Boolean>()
    val isNoteUpdated = _isNoteUpdatedSharedFlow.asSharedFlow()

    private val _isNoteDeletedSharedFlow = MutableSharedFlow<Boolean>()
    val isNoteDeleted = _isNoteDeletedSharedFlow.asSharedFlow()


    val isSaveState: SharedFlow<Boolean> = combine(_titleStateFlow, _bodyStateFlow) { title, body ->
        mainUseCase.checkIsInsertable(title, body)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000.milliseconds),
        initialValue = false
    )

    fun onTitleChanged(title: String) {
        _titleStateFlow.value = title
    }

    fun onBodyChanged(body: String) {
        _bodyStateFlow.value = body
    }

    // note insertion ------------------------------------------------------------------------------
    fun insertNote(note: Note) {
        viewModelScope.launch {
            val isSuccess = addUseCase(note)

            _isNoteSavedSharedFlow.emit(isSuccess)

        }
    }

    // note get ------------------------------------------------------------------------------------
    val getAllNote: StateFlow<List<Note>> = getUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000.milliseconds),
            initialValue = emptyList()
        )

    fun getNoteById(noteId: Int) = getUseCase(noteId)

    // note deletion -------------------------------------------------------------------------------


    fun moveNoteListToTrash(noteList: List<Note>) {
        viewModelScope.launch {
            val isTrashed = trashUseCase(noteList)
        }
    }

    fun deleteNote(noteList: List<Note>) {
        viewModelScope.launch {
            // delete functionality
            val result = deleteUseCase(noteList)
        }
    }


    // note update ---------------------------------------------------------------------------------
    fun updateNote(note: Note) {
        viewModelScope.launch {
            val isUpdated = updateUseCase(note)

            _isNoteUpdatedSharedFlow.emit(isUpdated)
        }
    }

    fun updateNoteList(noteList: List<Note>) {
        viewModelScope.launch {
            val isUpdated = updateUseCase(noteList)

            _isNoteUpdatedSharedFlow.emit(isUpdated)
        }
    }

}