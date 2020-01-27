package dk.shortify.api.gd

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GdServiceImpl {
    val isGd: GdService = Retrofit.Builder()
        .baseUrl("https://is.gd/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GdService::class.java)

    val vGd: GdService = Retrofit.Builder()
        .baseUrl("https://v.gd/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GdService::class.java)
}