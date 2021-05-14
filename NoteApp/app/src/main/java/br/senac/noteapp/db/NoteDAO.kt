package br.senac.noteapp.db

import androidx.room.*
import br.senac.noteapp.model.Note

@Dao
interface NoteDAO {
    @Insert
    fun save(note : Note)

    @Query(value = "Select * from Note")
    fun getAll() : List<Note>

    @Query(value = "Select * from Note where id = :id")
    fun getWithId(id: Int) : List<Note>

    @Update
    fun edit(note: Note)

    @Delete
    fun delete(note: Note)
}