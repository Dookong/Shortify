package dk.shortify.api.me2

import dk.shortify.model.ME2
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Me2Service {
    @Headers(
        "X-Naver-Client-Id: ${Key.clientId}",
        "X-Naver-Client-Secret: ${Key.clientSecret}"
    )
    @GET("shorturl.json")
    fun getShorten(
        @Query("url") url: String
    ): Call<ME2>
}