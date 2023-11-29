package com.example.myfirebaseexample.api.response

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("editorial") var edit: String,
    @SerializedName("01_Nombre") var name: String,
    @SerializedName("14_autor") var autor: String,
    @SerializedName("16_Costo") var cost: Long,
)
