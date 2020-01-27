package dk.shortify.api.shrtcode

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ShrtCodeServiceImpl {
    val service: ShrtCodeService = Retrofit.Builder()
        .baseUrl("https://api.shrtco.de/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ShrtCodeService::class.java)
}