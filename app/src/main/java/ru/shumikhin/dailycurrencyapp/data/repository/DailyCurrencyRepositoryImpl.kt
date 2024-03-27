package ru.shumikhin.dailycurrencyapp.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import ru.shumikhin.dailycurrencyapp.data.api.CurrencyApi
import ru.shumikhin.dailycurrencyapp.data.api.models.CurrencyRatesDto
import ru.shumikhin.dailycurrencyapp.data.models.CurrencyRates
import ru.shumikhin.dailycurrencyapp.data.models.Valute
import ru.shumikhin.dailycurrencyapp.data.repository.utils.RequestResult
import ru.shumikhin.dailycurrencyapp.data.repository.utils.map
import ru.shumikhin.dailycurrencyapp.data.repository.utils.toRequestResult
import ru.shumikhin.dailycurrencyapp.domain.repository.DailyCurrencyRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

class DailyCurrencyRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi
) : DailyCurrencyRepository {
    override suspend fun getCurrency(): Flow<RequestResult<CurrencyRates>> {
        val start = flowOf<RequestResult<CurrencyRatesDto>>(RequestResult.Loading)
        val currencyRates = flow { emit(currencyApi.getData()) }
            .map {
                it.toRequestResult()
            }
        return merge(start, currencyRates).map { requestResult ->
            requestResult.map {
                it.toCurrencyRates()
            }
        }.flowOn(Dispatchers.IO)
    }
}

private fun CurrencyRatesDto.toCurrencyRates(): CurrencyRates {
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
