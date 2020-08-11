package jasper.wagner.cryptotracking.model

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("status") val status: Status,
    @SerializedName("data") val coinModel: CoinModel
)
