package dk.shortify.api.gd

import dk.shortify.model.GD
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GdService {
    @GET("create.php")
    fun getShorten(
        @Query("format") format: String, @Query("url") url: String
    ): Call<GD>
}