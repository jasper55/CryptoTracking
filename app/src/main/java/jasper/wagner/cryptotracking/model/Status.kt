package jasper.wagner.cryptotracking.model

import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("timestamp") val timeStamp: String,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: String?,
    @SerializedName("elapsed") val elapsed: Long,
    @SerializedName("credit_count") val creditCount: Int,
    @SerializedName("notice") val notive: String
)
