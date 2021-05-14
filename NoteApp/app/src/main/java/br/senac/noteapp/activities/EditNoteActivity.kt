package br.senac.noteapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import br.senac.noteapp.databinding.ActivityEditNoteBinding
import br.senac.noteapp.databinding.ActivityListNotesBinding
import br.senac.noteapp.db.AppDatabase
import br.senac.noteapp.model.Note

class EditNoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val note = intent.getSerializableExtra("note") as Note

        binding.editTitle.setText(note.title)
        binding.editDesc.setText(note.desc)
        binding.editUser.setText(note.user)

        binding.btnSaveNote.setOnClickListener { saveNote(note) }
        binding.btnCancel.setOnClickListener { finish() }
    }

    fun saveNote(note: Note) {
        Thread {
            val db = Room.databaseBuilder(this, AppDatabase::class.java, "db").build()
            if (binding.editTitle.text.isNotBlank()) {
                note.title = binding.editTitle.text.toString()
            }

            if (binding.editDesc.text.isNotBlank()) {
                note.desc = binding.editDesc.text.toString()
            }

            if (binding.editUser.text.isNotBlank()) {
                note.user = binding.editUser.text.toString()
            }

            db.noteDao().edit(note)
            finish()
        }.start()
    }
}