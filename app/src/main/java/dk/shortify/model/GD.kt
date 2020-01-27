package dk.shortify.model
import com.google.gson.annotations.SerializedName

data class GD(
    @SerializedName("shorturl")
    var shorturl: String
)