package com.sevall

import com.google.protobuf.Empty
import com.servall.NoteMessage
import com.servall.Notes
import com.servall.NotesServiceGrpcKt
import com.servall.Response
import com.sevall.NotesRepository.sharedFlow
import com.sevall.NotesRepository.notes
import java.util.Random
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesService : NotesServiceGrpcKt.NotesServiceCoroutineImplBase() {
    override fun getNotes(request: Empty): Flow<Notes> {
        return sharedFlow.map {
            Notes.newBuilder()
                .addAllNotes(
                    it.map { value ->
                        NoteMessage.newBuilder()
                            .setNote(value.note)
                            .setDate(value.date)
                            .setTitle(value.title)
                            .setId(value.id)
                            .build()
                    }
                )
                .build()
        }
    }

    override suspend fun saveNote(request: NoteMessage): Response {
        val result = notes.add(
            Note(
                id = Random().nextInt().toString(),
                date = request.date,
                note = request.note,
                title = request.title
            )
        ).also {
            sharedFlow.emit(notes)
        }

        return Response.newBuilder().setResult(result).build()
    }

    override fun collaborativeModification(requests: Flow<NoteMessage>): Flow<NoteMessage> {
        return super.collaborativeModification(requests)
    }

    override suspend fun modifyNote(requests: Flow<NoteMessage>): Response {
        return super.modifyNote(requests)
    }
}