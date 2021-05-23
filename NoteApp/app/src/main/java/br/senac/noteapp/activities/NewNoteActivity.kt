package br.senac.noteapp.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import androidx.room.Room
import br.senac.noteapp.R
import br.senac.noteapp.databinding.ActivityNewNoteBinding
import br.senac.noteapp.db.AppDatabase
import br.senac.noteapp.model.Note
import br.senac.noteapp.model.Notes
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import kotlin.concurrent.thread

class NewNoteActivity : AppCompatActivity(), ColorPickerDialogListener {
    lateinit var binding: ActivityNewNoteBinding
    var selectedColor : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedColor = PreferenceManager.getDefaultSharedPreferences(this).getInt("noteColor", R.color.noteDefaultColor)

        binding.btnAdd.setOnClickListener {
            val sharedPrefs = getSharedPreferences("Users", Context.MODE_PRIVATE)
            val username = sharedPrefs.getString("username","default") as String

            val note = Note(title = binding.etTitle.text.toString(), desc = binding.etDesc.text.toString(), user = username, color = selectedColor)

            //Notes.noteList.add(note)
            Thread {
                saveNote(note)
                finish()
            }.start()

            //finish()
        }
        binding.btnNoteColor.setBackgroundColor(selectedColor)
        binding.btnNoteColor.setOnClickListener { ColorPickerDialog.newBuilder().setDialogId(123).show(this);}

        // listener update note
    }

    override fun onDialogDismissed(dialogId: Int) {
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        when (dialogId) {
            123-> {
                binding.btnNoteColor.setBackgroundColor(color)
                selectedColor = color
            }
        }
    }

    fun saveNote(note : Note) {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "db").build()

        db.noteDao().save(note)
    }
}