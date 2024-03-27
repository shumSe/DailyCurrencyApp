package ru.shumikhin.dailycurrencyapp.data.api.models

import com.google.gson.annotations.SerializedName

data class CurrencyRatesDto(
    @SerializedName("Date") val date: String,
    @SerializedName("PreviousDate") val previousDate: String,
    @SerializedName("PreviousURL") val previousURL: String,
    @SerializedName("Timestamp") val timestamp: String,
    @SerializedName("Valute") val valute: Map<String, ValuteDto>
) {
}

