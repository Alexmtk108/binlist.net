package com.example.binlist

import retrofit2.http.GET
import retrofit2.http.Path

data class BinResponse(
    val scheme: String?,
    val type: String?,
    val brand: String?,
    val prepaid: Boolean?,
    val country: Country?,
    val bank: Bank

)

data class Country(val name: String?, val latitude: Double?, val longitude: Double?)
data class Bank(val name: String?, val url: String?, val phone: String?, val city: String?)

interface BinApi {
    @GET("/{bin}")
    suspend fun getBinInfo(@Path("bin") bin: String): BinResponse
}