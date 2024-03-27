package ru.shumikhin.dailycurrencyapp.data.models

data class Valute(
    val id: String,
    val charCode: String,
    val nominal: Int,
    val name: String,
    val value: Double,
    val previous: Double
)
