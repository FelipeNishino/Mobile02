package br.senac.noteapp.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.senac.noteapp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    lateinit var binding : ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences("Users", Context.MODE_PRIVATE)
        val username = sharedPrefs.getString("username","default") as String
        binding.etUsername.setText(username)

        binding.btnSave.setOnClickListener {

            val editor = sharedPrefs.edit()
            editor.putString("username", binding.etUsername.text.toString())

            editor.commit() //uma vez commitado ja era, fechou o arquivo, se tentar mudar
                            //toma dois tapão em forma de exception
        }
    }

}