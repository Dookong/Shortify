package dk.shortify.api.me2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Me2ServiceImpl {
    val service: Me2Service = Retrofit.Builder()
        .baseUrl("https://openapi.naver.com/v1/util/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Me2Service::class.java)
}