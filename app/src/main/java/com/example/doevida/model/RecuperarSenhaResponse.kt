import com.google.gson.annotations.SerializedName

data class RecuperarSenhaResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("status_code") val status_code: Int,
    @SerializedName("message") val message: String
)
