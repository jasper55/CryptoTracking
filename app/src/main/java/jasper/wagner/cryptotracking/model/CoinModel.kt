package jasper.wagner.cryptotracking.model

import com.google.gson.annotations.SerializedName


data class CoinModel(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("num_market_pairs") val num_market_pairs: Int,
    @SerializedName("date_added") val date_added: String,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("max_supply") val max_supply: Double?,
    @SerializedName("circulating_supply") val circulating_supply: Double,
    @SerializedName("total_supply") val total_supply: Double,
    @SerializedName("platform") val platform: Platform?,
    @SerializedName("cmc_rank") val cmc_rank: Int,
    @SerializedName("last_updated") val last_updated: String,
    @SerializedName("quote") val quote: Quote
)
