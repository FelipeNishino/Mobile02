package br.senac.noteapp.ExRetrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import br.senac.noteapp.R
import br.senac.noteapp.databinding.ActivityEditNoteBinding
import br.senac.noteapp.databinding.ActivityViaCepBinding
import br.senac.noteapp.model.Address
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class ViaCepActivity : AppCompatActivity() {
    lateinit var binding: ActivityViaCepBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViaCepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRequest.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://viacep.com.br")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(ViaCepService::class.java)

            val call = service.obter(binding.editCEP.text.toString())

            val callback = object: Callback<Address> {
                override fun onResponse(call: Call<Address>,
                                        response: Response<Address>
                ) {
                    if (response.isSuccessful) {
                        val address = response.body()
                        binding.txtBairro.setText("Bairro: " + (address?.bairro ?: "ND"))
                        binding.txtLogradouro.setText("Logradouro: " + (address?.logradouro ?: "ND"))
                        binding.txtLocalidade.setText("Localidade: " + (address?.localidade ?: "ND"))
                        binding.txtComplemento.setText("Complemento: " + (address?.complemento ?: "ND"))
                        binding.txtUF.setText("UF: " + (address?.uf ?: "ND"))
                        binding.txtIBGE.setText("IBGE: " + (address?.ibge ?: "ND"))
                        binding.txtSIAFI.setText("SIAFI: " + (address?.siafi ?: "ND"))
                        binding.txtDDD.setText("DDD: " + (address?.ddd ?: "ND"))
                        binding.txtGIA.setText("GIA: " + (address?.gia ?: "ND"))
                    }
                    else {
                        Toast.makeText(it.context, "Falha na requisição", Toast.LENGTH_SHORT).show()
                    }

                }
                override fun onFailure(call: Call<Address>, t: Throwable) {
                    Toast.makeText(it.context, "Falha na requisição", Toast.LENGTH_SHORT).show()
                }
            }
            call.enqueue(callback)
        }
    }
}