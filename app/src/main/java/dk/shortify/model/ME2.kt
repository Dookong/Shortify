package dk.shortify.model
import com.google.gson.annotations.SerializedName

data class ME2(
    @SerializedName("code")
    var code: String,
    @SerializedName("message")
    var message: String,
    @SerializedName("result")
    var result: Result
) {
    data class Result(
        @SerializedName("hash")
        var hash: String,
        @SerializedName("orgUrl")
        var orgUrl: String,
        @SerializedName("url")
        var url: String
    )
}