package dk.shortify.model
import com.google.gson.annotations.SerializedName

data class ShrtCode(
    @SerializedName("ok")
    var ok: Boolean,
    @SerializedName("result")
    var result: Result
) {
    data class Result(
        @SerializedName("code")
        var code: String,
        @SerializedName("full_share_link")
        var fullShareLink: String,
        @SerializedName("full_short_link")
        var fullShortLink: String,
        @SerializedName("full_short_link2")
        var fullShortLink2: String,
        @SerializedName("original_link")
        var originalLink: String,
        @SerializedName("share_link")
        var shareLink: String,
        @SerializedName("short_link")
        var shortLink: String,
        @SerializedName("short_link2")
        var shortLink2: String
    )
}