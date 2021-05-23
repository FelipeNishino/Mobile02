package br.senac.noteapp.ExRetrofit

import br.senac.noteapp.model.Address
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("/ws/{cep}/json")
    fun obter(@Path("cep") cep: String): Call<Address>
}