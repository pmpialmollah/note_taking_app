package com.example.nnote.features.crud.ui.note_details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nnote.R
import com.example.nnote.core.common.Constants
import com.example.nnote.core.domain.model.Note
import com.example.nnote.databinding.ActivityNoteBinding
import com.example.nnote.features.crud.ui.MainViewModel
import com.example.nnote.features.crud.ui.note_list.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding
    private val mainViewModel: MainViewModel by viewModels()
    private var toast: Toast? = null
    private var isSavable: Boolean = false
    private var noteId: String = ""
    private var action: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // my code -----
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // my code here ----------------------------------------------------------------------------

        action = intent.getStringExtra(Constants.PARAM_ACTION) ?: Constants.PARAM_CREATE_NOTE
        noteId = intent.getStringExtra(Constants.PARAM_NOTE_ID) ?: ""

        if (action.uppercase() == Constants.PARAM_CREATE_NOTE) {   // create field ------------------------------------
            // click listener here =================================================================
            binding.saveButton.setOnClickListener {
                saveNote(true)

                navigateActivity(this@NoteActivity, MainActivity::class.java)
            }
            // =====================================================================================


        }   // -------------------------------------------------------------------------------------
        else {  // edit field ----------------------------------------------------------------------

            // click listeners =====================================================================
            binding.saveButton.setOnClickListener {
                saveNote(false)
                navigateActivity(this@NoteActivity, MainActivity::class.java)
            }
            // =====================================================================================

        }   // -------------------------------------------------------------------------------------


        binding.edTitle.doAfterTextChanged { text ->
            mainViewModel.onTitleChanged(text.toString())
        }

        binding.edBody.doAfterTextChanged { text ->
            mainViewModel.onBodyChanged(text.toString())
        }

        // observers here ======================================================================
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.isSaveState.collect { isSavable ->
                        this@NoteActivity.isSavable = isSavable
                        if (isSavable) {
                            binding.saveButton.visibility = View.VISIBLE
                        } else {
                            binding.saveButton.visibility = View.GONE
                        }
                    }
                }

                launch {
                    if (noteId.isNotEmpty()) {
                        mainViewModel.getNoteById(noteId.toInt()).collect { currentNote ->
                            if (currentNote != null) {
                                binding.edTitle.setText(currentNote.title)
                                binding.edBody.setText(currentNote.body)
                            }
                        }
                    }
                }

                launch {
                    mainViewModel.isNoteSaved.collect { isSaved ->
                        if (isSaved) {
                            showToast("Note saved.")
                        }
                    }
                }

                launch {
                    mainViewModel.isNoteUpdated.collect { isUpdated ->
                        if (isUpdated) {
                            showToast("Note updated.")
                        }
                    }
                }
                launch {
                    mainViewModel.isNoteDeleted.collect { isDeleted ->
                        if (isDeleted) {
                            showToast("Note deleted.")
                        }
                    }
                }

            }
        }
        // observers ===============================================================================


        binding.backButton.setOnClickListener {
            backFunction()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backFunction()
            }
        })
        // =========================================================================================


    }   // on create bundle ends here --------------------------------------------------------------

    fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this@NoteActivity, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun navigateActivity(currentActivity: Activity, targetActivity: Class<*>) {
        startActivity(Intent(currentActivity, targetActivity))
        currentActivity.overridePendingTransition(0, 0)
    }

    private fun backFunction() {
        if (isSavable && action.uppercase() == Constants.PARAM_CREATE_NOTE) {
            saveNote(true)
        } else if ((binding.edTitle.text.isNotEmpty() || binding.edBody.text.isNotEmpty()) && action.uppercase() == Constants.PARAM_UPDATE_NOTE) {
            saveNote(false)
        } else if ((binding.edTitle.text.isEmpty() || binding.edBody.text.isEmpty()) && action.uppercase() == Constants.PARAM_UPDATE_NOTE) {
            deleteNote(noteId.toInt())
        }
        navigateActivity(this@NoteActivity, MainActivity::class.java)
        finish()
    }

    private fun saveNote(isNewNote: Boolean) {

        val title = binding.edTitle.text.toString().trim()
        val body = binding.edBody.text.toString().trim()

        if (isNewNote) {
            val note = Note(
                title = title,
                body = body,
                createdAt = "00:00:00"
            )
            mainViewModel.insertNote(note)
        } else {
            val note = Note(
                id = noteId.toInt(),
                title = title,
                body = body,
                createdAt = "00:00:00",
                updatedAt = "11:11:11"
            )
            mainViewModel.updateNote(note)
        }
    }

    private fun deleteNote(noteId: Int) {
        val title = binding.edTitle.text.toString().trim()
        val body = binding.edBody.text.toString().trim()

        if (title.isEmpty() && body.isEmpty()) {
            val note = Note(
                id = noteId,
                title = "",
                body = "",
                createdAt = ""
            )
            mainViewModel.deleteNote(listOf(note))
        }
    }
}