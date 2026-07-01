package com.example.nnote.features.trash.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nnote.R
import com.example.nnote.core.domain.model.Note
import com.example.nnote.databinding.ActivityTrashBinding
import com.example.nnote.features.crud.ui.note_list.MainActivity
import com.example.nnote.features.trash.ui.adapter.OnClick
import com.example.nnote.features.trash.ui.adapter.TrashAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.jvm.java

@AndroidEntryPoint
class TrashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrashBinding
    private var toast: Toast? = null
    private lateinit var trashAdapter: TrashAdapter
    private val trashViewModel: TrashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // my code ---------------
        binding = ActivityTrashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // code starts here ------------------------------------------------------------------------

        trashAdapter = TrashAdapter(
            object : OnClick {
                override fun onLongClick(note: Note) {
                    updateUi(true)
                }
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TrashActivity)
            adapter = trashAdapter
        }

        // observers here --------------------------------------------------------------------------
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    trashViewModel.getTrashedNotes().collect { notes ->
                        trashAdapter.setTrashededNoteList(notes)
                    }
                }
            }
        }
        // -----------------------------------------------------------------------------------------

        // click listener --------------------------------------------------------------------------
        binding.imgBack.setOnClickListener {
            startActivity(Intent(this@TrashActivity, MainActivity::class.java))
            finish()
        }

        binding.imgCross.setOnClickListener {
            updateUi(false)
        }

        binding.restoreButton.setOnClickListener {
            val selectedNotes = trashAdapter.getSelectedNoteList().map { note ->
                note.copy(
                    isSelected = false
                )
            }

            if (selectedNotes.isNotEmpty()) {
                trashViewModel.restoreTrashedNotes(selectedNotes)
            }
            updateUi(false)
        }

        binding.deleteButton.setOnClickListener {
            val selectedNotes = trashAdapter.getSelectedNoteList().map { note ->
                note.copy(
                    isSelected = false
                )
            }

            if (selectedNotes.isNotEmpty()) {
                AlertDialog.Builder(this@TrashActivity)
                    .setTitle("Confirm delete")
                    .setMessage("Do you really want to permanently delete notes?")
                    .setPositiveButton("yes") { dialog, _ ->
                        trashViewModel.deleteTrashedNotes(selectedNotes)
                        dialog.dismiss()
                    }
                    .setNegativeButton("no") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()

            }
            updateUi(false)
        }

        // -----------------------------------------------------------------------------------------


    }   // on create bundle end here ---------------------------------------------------------------

    fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this@TrashActivity, message, Toast.LENGTH_SHORT)
        toast?.show()
    }


    private fun updateUi(isSelectionMode: Boolean) {
        trashAdapter.setSelectionMode(isSelectionMode)
        if (isSelectionMode) {
            binding.topLayout.visibility = View.GONE
            binding.imgCross.visibility = View.VISIBLE
            binding.bottomLayout.visibility = View.VISIBLE
        } else {
            binding.topLayout.visibility = View.VISIBLE
            binding.imgCross.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
        }
    }
}