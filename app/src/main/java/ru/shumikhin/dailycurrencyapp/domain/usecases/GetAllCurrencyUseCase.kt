package ru.shumikhin.dailycurrencyapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import ru.shumikhin.dailycurrencyapp.data.models.CurrencyRates
import ru.shumikhin.dailycurrencyapp.data.repository.utils.RequestResult
import ru.shumikhin.dailycurrencyapp.domain.repository.DailyCurrencyRepository
import javax.inject.Inject

class GetAllCurrencyUseCase @Inject constructor(
    private val repository: DailyCurrencyRepository,
) {

    suspend operator fun invoke(): Flow<RequestResult<CurrencyRates>> {
        return repository.getCurrency()
    }

}