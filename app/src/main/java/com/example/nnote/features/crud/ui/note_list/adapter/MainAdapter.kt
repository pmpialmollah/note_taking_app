package com.example.nnote.features.crud.ui.note_list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nnote.R
import com.example.nnote.core.domain.model.Note

class MainAdapter(
    val onClick: OnClick
) : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {
    private var isSelectionMode: Boolean = false
    private var noteList: List<Note>? = emptyList()

    fun setNoteList(noteList: List<Note>) {
        this.noteList = noteList
        notifyDataSetChanged()
    }

    fun setSelectionMode(isSelectionMode: Boolean) {
        this.isSelectionMode = isSelectionMode
        notifyDataSetChanged()
    }

    fun getSelectedNoteList(): List<Note> {
        val selectedNoteList = mutableListOf<Note>()
        noteList?.forEach { note ->
            if (note.isSelected) {
                selectedNoteList.add(note)
            }
        }

        return selectedNoteList
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle = itemView.findViewById<TextView>(R.id.noteTitle)
        val noteBody = itemView.findViewById<TextView>(R.id.noteBody)
        val noteTime = itemView.findViewById<TextView>(R.id.noteTime)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        val checkBoxLayout = itemView.findViewById<LinearLayout>(R.id.checkBoxLayout)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val myView =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item_layout, parent, false)
        return MyViewHolder(myView)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val note = noteList?.get(position)

        if (isSelectionMode) {
            holder.checkBoxLayout.visibility = View.VISIBLE
        } else {
            holder.checkBoxLayout.visibility = View.GONE
            note?.isSelected = false
        }

        holder.noteTitle.text = note?.title
        holder.noteBody.text = note?.body
        holder.noteTime.text = note?.createdAt

        holder.checkBox.isChecked = note?.isSelected ?: false

        holder.itemView.setOnClickListener {
            if (!isSelectionMode) {
                onClick.onItemClick(note?.id ?: -10)
            } else {
                note?.let {
                    it.isSelected = !it.isSelected
                    holder.checkBox.isChecked = it.isSelected
                }
            }
        }

        holder.checkBox.setOnClickListener {
            note?.let {
                it.isSelected = holder.checkBox.isChecked
            }
        }

        holder.itemView.setOnLongClickListener {
            note?.let { note ->
                onClick.longClick(note)
                note.isSelected = true
                holder.checkBox.isChecked = note.isSelected
            }
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return noteList?.size ?: 0
    }

}

interface OnClick {
    fun longClick(note: Note)
    fun onItemClick(noteId: Int)
}