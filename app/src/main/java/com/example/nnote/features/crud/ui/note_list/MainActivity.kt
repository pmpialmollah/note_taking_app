package com.example.nnote.features.crud.ui.note_list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nnote.R
import com.example.nnote.core.common.Constants
import com.example.nnote.core.domain.model.Note
import com.example.nnote.databinding.ActivityMainBinding
import com.example.nnote.features.archive.ui.note_list.ArchiveActivity
import com.example.nnote.features.crud.ui.note_list.adapter.MainAdapter
import com.example.nnote.features.crud.ui.MainViewModel
import com.example.nnote.features.crud.ui.note_details.NoteActivity
import com.example.nnote.features.crud.ui.note_list.adapter.OnClick
import com.example.nnote.features.trash.ui.TrashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mainAdapter: MainAdapter
    private var toast: Toast? = null
    private var isSelectionMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // my code here ----------------------------------------------------------------------------

        // adapter code here =======================================================================
        mainAdapter = MainAdapter(object : OnClick {

            override fun longClick(note: Note) {
                isSelectionMode = true
                updateUi(isSelectionMode)
            }

            override fun onItemClick(noteId: Int) {
                val myIntent = Intent(this@MainActivity, NoteActivity::class.java).apply {
                    putExtra(Constants.PARAM_NOTE_ID, noteId.toString())
                    putExtra(Constants.PARAM_ACTION, Constants.PARAM_UPDATE_NOTE)
                }
                startActivity(myIntent)
                overridePendingTransition(0, 0)
            }
        })
        binding.mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mainAdapter
        }
        // =========================================================================================

        // click handle here =======================================================================
        binding.addNoteButton.setOnClickListener {
            navigateActivity(
                this@MainActivity,
                NoteActivity::class.java,
                Constants.PARAM_CREATE_NOTE
            )
        }


        binding.archiveButton.setOnClickListener {
            val selectedNoteList = mainAdapter.getSelectedNoteList()

            val modifiedNoteList = selectedNoteList.map { note ->
                note.copy(
                    isArchived = true,
                    isSelected = false,
                )
            }

            if (modifiedNoteList.isNotEmpty()) {
                mainViewModel.updateNoteList(modifiedNoteList)
            }

            updateUi(false)
        }


        binding.pinButton.setOnClickListener {
            showToast("Pin button clicked.")
        }

        binding.deleteButton.setOnClickListener {
            val selectedNoteList = mainAdapter.getSelectedNoteList()

            val modifiedNoteList = selectedNoteList.map { note ->
                note.copy(
                    isSelected = false
                )
            }

            if (modifiedNoteList.isNotEmpty()) {
                mainViewModel.moveNoteListToTrash(modifiedNoteList)
            }
            isSelectionMode = false
            updateUi(isSelectionMode)
        }
        binding.imgCross.setOnClickListener {
            isSelectionMode = false
            updateUi(isSelectionMode)
        }

        binding.imgMenu.setOnClickListener {
            binding.main.openDrawer(GravityCompat.START)
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigationNotes -> binding.main.closeDrawer(GravityCompat.START)
                R.id.navigationArchive -> navigateActivity(
                    this@MainActivity,
                    ArchiveActivity::class.java,
                    ""
                )

                R.id.navigationTrash -> navigateActivity(
                    this@MainActivity,
                    TrashActivity::class.java,
                    ""
                )
            }

            binding.main.closeDrawer(GravityCompat.START)
            true
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.main.isDrawerOpen(GravityCompat.START)) {
                    binding.main.closeDrawer(GravityCompat.START)
                } else if (isSelectionMode) {
                    isSelectionMode = false
                    updateUi(isSelectionMode)
                } else {
                    finish()
                }
            }
        })

        // =========================================================================================

        // observers ===============================================================================

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.getAllNote.collect { allNotes ->
                        mainAdapter.setNoteList(allNotes)
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

        // =========================================================================================

    } // on create end here ------------------------------------------------------------------------

    fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun navigateActivity(currentActivity: Activity, targetActivity: Class<*>, action: String?) {
        val myIntent = Intent(currentActivity, targetActivity)
        myIntent.putExtra("ACTION", action?.uppercase())
        startActivity(myIntent)
        overridePendingTransition(0, 0)
    }

    fun updateUi(isSelectionMode: Boolean) {

        mainAdapter.setSelectionMode(isSelectionMode)

        if (isSelectionMode) {
            binding.imgCross.visibility = View.VISIBLE
            binding.addNoteButton.visibility = View.GONE
            binding.bottomLayout.visibility = View.VISIBLE
            binding.imgMenu.visibility = View.GONE
        } else {
            binding.imgCross.visibility = View.GONE
            binding.addNoteButton.visibility = View.VISIBLE
            binding.bottomLayout.visibility = View.GONE
            binding.imgMenu.visibility = View.VISIBLE
        }
    }
}