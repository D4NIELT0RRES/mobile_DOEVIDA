import com.google.gson.annotations.SerializedName

data class RecuperarSenhaResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)
