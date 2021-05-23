package br.senac.noteapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.room.Room
import br.senac.noteapp.R
import br.senac.noteapp.databinding.ActivityEditNoteBinding
import br.senac.noteapp.databinding.ActivityListNotesBinding
import br.senac.noteapp.db.AppDatabase
import br.senac.noteapp.model.Note
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener

class EditNoteActivity : AppCompatActivity(), ColorPickerDialogListener {
    lateinit var binding: ActivityEditNoteBinding
    var selectedColor : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val note = intent.getSerializableExtra("note") as Note

        binding.editTitle.setText(note.title)
        binding.editDesc.setText(note.desc)
        binding.editUser.setText(note.user)
        binding.btnEditNoteColor.setBackgroundColor(note.color)
        selectedColor = note.color

        binding.btnSaveNote.setOnClickListener { saveNote(note) }
        binding.btnCancel.setOnClickListener { finish() }
        binding.btnEditNoteColor.setOnClickListener { ColorPickerDialog.newBuilder().setDialogId(321).show(this); }

    }

    override fun onDialogDismissed(dialogId: Int) {
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        when (dialogId) {
            321-> {
                binding.btnEditNoteColor.setBackgroundColor(color)
                selectedColor = color
            }
        }
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
            note.color = selectedColor

            db.noteDao().edit(note)
            finish()
        }.start()
    }
}