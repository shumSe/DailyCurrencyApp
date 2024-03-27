package ru.shumikhin.dailycurrencyapp.data.repository.utils

import ru.shumikhin.dailycurrencyapp.data.api.models.CurrencyRatesDto
import ru.shumikhin.dailycurrencyapp.data.models.CurrencyRates
import ru.shumikhin.dailycurrencyapp.data.models.Valute
import java.text.SimpleDateFormat
import java.util.Calendar

fun CurrencyRatesDto.toCurrencyRates(): CurrencyRates {
    return CurrencyRates(
        updateTime = getCurrentTime(),
        valute = valute.values.map { valuteDto ->
            Valute(
                id = valuteDto.id,
                charCode = valuteDto.charCode,
                nominal = valuteDto.nominal,
                name = valuteDto.name,
                value = String.format("%.2f", valuteDto.value).toDouble(),
                previous = valuteDto.previous,
            )
        }
    )
}

private fun getCurrentTime(): String{
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    return sdf.format(Calendar.getInstance().time)
}