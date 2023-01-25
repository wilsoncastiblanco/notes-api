package com.sevall

import kotlinx.coroutines.flow.MutableSharedFlow

object NotesRepository {
    val notes = mutableListOf<Note>()
    val sharedFlow = MutableSharedFlow<List<Note>>()
}