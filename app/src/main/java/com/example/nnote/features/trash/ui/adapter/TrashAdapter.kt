package com.example.nnote.features.trash.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nnote.R
import com.example.nnote.core.domain.model.Note
import org.w3c.dom.Text

class TrashAdapter(
    private val onClick: OnClick
) : RecyclerView.Adapter<TrashAdapter.MyViewHolder>() {

    private var trashededNoteList: List<Note> = emptyList()
    private var isSelectionMode: Boolean = false

    fun setTrashededNoteList(notes: List<Note>) {
        trashededNoteList = notes
        this.notifyDataSetChanged()
    }

    fun setSelectionMode(isSelectionMode: Boolean) {
        this@TrashAdapter.isSelectionMode = isSelectionMode
        notifyDataSetChanged()
    }

    fun getSelectedNoteList(): List<Note> {
        val selectedNoteList = mutableListOf<Note>()
        trashededNoteList.forEach { note ->
            if (note.isSelected) {
                selectedNoteList.add(note)
            }
        }

        return selectedNoteList as List<Note>
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle = itemView.findViewById<TextView>(R.id.noteTitle)
        val noteBody = itemView.findViewById<TextView>(R.id.noteBody)
        val noteTime = itemView.findViewById<TextView>(R.id.noteTime)
        val checkLayout = itemView.findViewById<LinearLayout>(R.id.checkBoxLayout)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val noteView =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item_layout, parent, false)
        return MyViewHolder(noteView)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val note = trashededNoteList[position]

        holder.noteTitle.text = note.title
        holder.noteBody.text = note.body
        holder.noteTime.text = note.createdAt

        if (isSelectionMode) {
            holder.checkLayout.visibility = View.VISIBLE
        } else {
            holder.checkLayout.visibility = View.GONE
            note.isSelected = false
        }

        holder.checkBox.isChecked = note.isSelected


        holder.itemView.setOnClickListener {
            if (isSelectionMode) {
                note.let {
                    it.isSelected = !it.isSelected
                    holder.checkBox.isChecked = it.isSelected
                }
            }
        }

        holder.checkBox.setOnClickListener {
            note.isSelected = holder.checkBox.isChecked
        }


        holder.itemView.setOnLongClickListener {
            onClick.onLongClick(note)
            note.isSelected = true
            holder.checkBox.isChecked = note.isSelected
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return trashededNoteList.size
    }
}

interface OnClick {
    fun onLongClick(note: Note)
}