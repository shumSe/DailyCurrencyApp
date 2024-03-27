package ru.shumikhin.dailycurrencyapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.shumikhin.dailycurrencyapp.data.api.CurrencyApi
import ru.shumikhin.dailycurrencyapp.data.repository.DailyCurrencyRepositoryImpl
import ru.shumikhin.dailycurrencyapp.domain.repository.DailyCurrencyRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideDailyCurrencyRepositoryImpl(currencyApi: CurrencyApi): DailyCurrencyRepository {
        return DailyCurrencyRepositoryImpl(currencyApi = currencyApi)
    }
}