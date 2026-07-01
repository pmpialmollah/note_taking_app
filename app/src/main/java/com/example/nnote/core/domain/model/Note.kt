package com.example.nnote.core.domain.model

data class Note(
    val id: Int = 0,
    var title: String,
    var body: String,
    var createdAt: String,
    var updatedAt: String = "00:00:00",
    var isArchived: Boolean = false,
    var isTrashed: Boolean = false,
    var isSelected: Boolean = false,
)