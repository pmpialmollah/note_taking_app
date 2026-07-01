package com.example.nnote.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nnote.core.domain.model.Note

@Entity(tableName = "note_table")
data class NoteDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val body: String,
    val createdAt: String,
    val updatedAt: String = "00:00:00",
    val isArchived: Boolean = false,
    val isTrashed: Boolean = false
)

fun NoteDto.toNote(): Note{
    return Note(
        id = id,
        title = title,
        body = body,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isArchived = isArchived,
        isTrashed = isTrashed,
    )
}