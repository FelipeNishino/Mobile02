package br.senac.noteapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.room.Room
import br.senac.noteapp.R
import br.senac.noteapp.databinding.ActivityListNotesBinding
import br.senac.noteapp.databinding.NoteCardBinding
import br.senac.noteapp.db.AppDatabase
import br.senac.noteapp.model.Note
import br.senac.noteapp.model.Notes
import java.math.RoundingMode

class ListNotesActivity : AppCompatActivity() {
    lateinit var binding: ActivityListNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            val i = Intent(this, NewNoteActivity::class.java)
            startActivity(i)
        }
    }

    fun updateUI(notes : List<Note>) {
        binding.noteContainer.removeAllViews()

        val prefManager = PreferenceManager.getDefaultSharedPreferences(this)
        val textColor = prefManager.getInt("textColor", R.color.textDefaultColor)

        val titleFontSize = prefManager.getString("title_font_size", "14")
        val contentFontSize = prefManager.getString("content_font_size", "10")

        //Notes.noteList.forEach {
        notes.forEach { note ->
            val cardBinding = NoteCardBinding.inflate(layoutInflater)

            cardBinding.txtTitle.text = note.title
            cardBinding.txtTitle.setTextColor(textColor)
            cardBinding.txtTitle.textSize = titleFontSize?.toFloat() ?: 14F
            cardBinding.txtDesc.text = note.desc
            cardBinding.txtDesc.setTextColor(textColor)
            cardBinding.txtDesc.textSize = contentFontSize?.toFloat() ?: 10F
            cardBinding.txtUser.text = note.user
            cardBinding.txtUser.setTextColor(textColor)

            cardBinding.btnDelete.setOnClickListener {
                Thread {
                    val db = Room.databaseBuilder(this, AppDatabase::class.java, "db").build()
                    db.noteDao().delete(note)
                    refreshNotes()
                }.start()
            }
            cardBinding.btnEdit.setOnClickListener {
                val intent = Intent(this,  EditNoteActivity::class.java)
                intent.putExtra("note", note)
                startActivityForResult(intent,0)
            }

            cardBinding.root.setCardBackgroundColor(note.color)

            binding.noteContainer.addView(cardBinding.root)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 0) { refreshNotes() }
    }

    fun refreshNotes() {
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "db").build()
        Thread {
            val notes = db.noteDao().getAll()

            runOnUiThread {
                updateUI(notes)
            }

        }.start()
    }

    override fun onResume() {
        super.onResume()
        refreshNotes()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.user -> {
                val i = Intent(this, UserActivity::class.java)
                startActivity(i)
            }
            R.id.config -> {
                val i = Intent(this, SettingsActivity::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}