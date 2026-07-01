package com.example.nnote.features.archive.ui.note_list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nnote.R
import com.example.nnote.core.domain.model.Note
import com.example.nnote.databinding.ActivityArchiveBinding
import com.example.nnote.features.archive.ui.ArchiveViewModel
import com.example.nnote.features.archive.ui.note_list.adapter.ArchiveAdapter
import com.example.nnote.features.archive.ui.note_list.adapter.OnClick
import com.example.nnote.features.crud.ui.note_list.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArchiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArchiveBinding
    private var toast: Toast? = null
    private lateinit var archiveAdapter: ArchiveAdapter
    private val archiveViewModel: ArchiveViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // my code ---------------
        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // code starts here ------------------------------------------------------------------------

        archiveAdapter = ArchiveAdapter(
            object : OnClick {
                override fun onItemClick(note: Note) {
                    showToast("item clicked")
                }

                override fun onLongClick(note: Note) {
                    updateUi(true)
                }
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ArchiveActivity)
            adapter = archiveAdapter
        }

        // observers here --------------------------------------------------------------------------
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    archiveViewModel.getArchivedNotes().collect { notes ->
                        archiveAdapter.setArchivedNoteList(notes)
                    }
                }
            }
        }
        // -----------------------------------------------------------------------------------------

        // click listener --------------------------------------------------------------------------
        binding.imgBack.setOnClickListener {
            startActivity(Intent(this@ArchiveActivity, MainActivity::class.java))
            finish()
        }

        binding.imgCross.setOnClickListener {
            updateUi(false)
        }

        binding.unArchiveButton.setOnClickListener {
            val selectedNotes = archiveAdapter.getSelectedNoteList()
            val modifiedSelectedNotes = selectedNotes.map { note ->
                note.copy(
                    isSelected = false
                )
            }
            if (modifiedSelectedNotes.isNotEmpty()){
                archiveViewModel.unArchiveNotes(modifiedSelectedNotes)
            }

            updateUi(false)
        }

        // -----------------------------------------------------------------------------------------


    }   // on create bundle end here ---------------------------------------------------------------

    fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this@ArchiveActivity, message, Toast.LENGTH_SHORT)
        toast?.show()
    }


    private fun updateUi(isSelectionMode: Boolean) {
        archiveAdapter.setSelectionMode(isSelectionMode)
        if (isSelectionMode) {
            binding.topLayout.visibility = View.GONE
            binding.imgCross.visibility = View.VISIBLE
            binding.bottomLayout.visibility = View.VISIBLE
        }
        else{
            binding.topLayout.visibility = View.VISIBLE
            binding.imgCross.visibility = View.GONE
            binding.bottomLayout.visibility = View.GONE
        }
    }
}