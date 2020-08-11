package jasper.wagner.cryptotracking.model

import com.google.gson.annotations.SerializedName

data class Platform(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("token_address") val tokenAddress: String
)
