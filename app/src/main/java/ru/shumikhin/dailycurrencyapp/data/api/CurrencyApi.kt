package ru.shumikhin.dailycurrencyapp.data.api

import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import ru.shumikhin.dailycurrencyapp.data.api.models.CurrencyRatesDto

interface CurrencyApi {

    @GET("/daily_json.js")
    suspend fun getData(): Result<CurrencyRatesDto>
}

fun CurrencyApi(
): CurrencyApi{
    return retrofit().create(CurrencyApi::class.java)
}

private fun retrofit(
): Retrofit{
    return Retrofit.Builder()
        .baseUrl("https://www.cbr-xml-daily.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .build()
}
