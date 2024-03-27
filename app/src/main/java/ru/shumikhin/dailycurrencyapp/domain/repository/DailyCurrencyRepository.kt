package ru.shumikhin.dailycurrencyapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.shumikhin.dailycurrencyapp.data.models.CurrencyRates
import ru.shumikhin.dailycurrencyapp.data.repository.utils.RequestResult

interface DailyCurrencyRepository {

    suspend fun getCurrency(): Flow<RequestResult<CurrencyRates>>

}