package dk.shortify.api.shrtcode

import dk.shortify.model.ShrtCode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ShrtCodeService {
    @GET("shorten")
    fun getShorten(
        @Query("url") url: String
    ): Call<ShrtCode>
}