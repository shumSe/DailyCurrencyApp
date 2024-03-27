package ru.shumikhin.dailycurrencyapp.data.models

data class CurrencyRates(
    val updateTime: String,
    val valute: List<Valute>
)
