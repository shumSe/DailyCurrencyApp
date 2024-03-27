package ru.shumikhin.dailycurrencyapp.presentation.allCurrencyScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.shumikhin.dailycurrencyapp.data.models.CurrencyRates
import ru.shumikhin.dailycurrencyapp.data.repository.utils.RequestResult
import ru.shumikhin.dailycurrencyapp.domain.usecases.GetAllCurrencyUseCase
import javax.inject.Inject

@HiltViewModel
class AllCurrencyViewModel @Inject constructor(
    private val getAllCurrencyUseCase: GetAllCurrencyUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Default)
    val state = _state.asStateFlow()

    private var isUpdating = true
    private val backgroundScope = CoroutineScope(context = Dispatchers.IO)
    init {
        viewModelScope.launch {
            getAllCurrencyUseCase().collect {
                _state.value = it.toState()
            }
        }
    }

    fun reloadValute() {
        isUpdating = true
        backgroundScope.coroutineContext.cancelChildren()
        viewModelScope.launch {
            autoUpdateCurrency()
        }
    }

    fun stopUpdating() {
        isUpdating = false
    }

    fun startUpdating() {
        isUpdating = true
    }

    suspend fun autoUpdateCurrency() {
        backgroundScope.launch {
            while (isUpdating) {
                getAllCurrencyUseCase().collect {
                    _state.value = it.toState()
                }
                delay(30000)
            }
        }
    }

}


sealed class State {
    data object Default : State()
    class Error(val error: Throwable) : State()
    class Success(val currencyRates: CurrencyRates) : State()
    data object Loading : State()
}

private fun RequestResult<CurrencyRates>.toState(): State {
    return when (this) {
        is RequestResult.Error -> State.Error(this.error)
        is RequestResult.Loading -> State.Loading
        is RequestResult.Success -> State.Success(data)
    }
}
