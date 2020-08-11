package jasper.wagner.cryptotracking.adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import jasper.wagner.cryptotracking.common.Common
import jasper.wagner.cryptotracking.`interface`.ILoadMore
import jasper.wagner.cryptotracking.model.CoinModel
import jasper.wagner.cryptotracking.R
import jasper.wagner.cryptotracking.common.MathOperation
import kotlinx.android.synthetic.main.coin_layout.view.*
import java.lang.StringBuilder


class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var coinIcon = itemView.coinIcon
    var coinSymbol = itemView.coinSymbol
    var coinName = itemView.coinName
    var coinPrice = itemView.priceUsd
    var oneHourChange = itemView.oneHour
    var twentyFourChange = itemView.twentyFourHour
    var sevenDayChange = itemView.sevenDay
}

class CoinAdapter(recyclerView: RecyclerView, internal var activity: Activity, var items: List<CoinModel>) : RecyclerView.Adapter<CoinViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.coin_layout, parent, false)
        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {

        val coinModel = items[position]

        val item = holder as CoinViewHolder

        item.coinName.text = coinModel.name
        item.coinSymbol.text = coinModel.symbol
        item.coinPrice.text = MathOperation.round(coinModel.quote.uSD.price).toString()
        item.oneHourChange.text = MathOperation.round(coinModel.quote.uSD.percent_change_1h).toString() + "%"
        item.twentyFourChange.text = MathOperation.round(coinModel.quote.uSD.percent_change_24h).toString() + "%"
        item.sevenDayChange.text = MathOperation.round(coinModel.quote.uSD.percent_change_7d).toString() + "%"

        Picasso.with(activity.baseContext)
            .load(StringBuilder(Common.imageUrl)
                .append(coinModel.symbol!!.toLowerCase())
                .append(".png")
                .toString())
            .into(item.coinIcon)

        //Set color
        item.oneHourChange.setTextColor(if (coinModel.quote.uSD.percent_change_1h.toString().contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
        )

        item.twentyFourChange.setTextColor(if (coinModel.quote.uSD.percent_change_24h.toString().contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
        )

        item.sevenDayChange.setTextColor(if (coinModel.quote.uSD.percent_change_7d.toString().contains("-"))
            Color.parseColor("#FF0000")
        else
            Color.parseColor("#32CD32")
        )
    }


    internal var loadMore: ILoadMore? = null
    var isLoading: Boolean = false
    var visibleThreshold = 5
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0

    init {
        val linearLayout = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayout.itemCount
                lastVisibleItem = linearLayout.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (loadMore != null)
                        loadMore!!.onLoadMore()
                    isLoading = true
                }
            }
        })
    }

    fun setLoadMore(loadMore: ILoadMore) {
        this.loadMore = loadMore
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun  setLoaded(){
        isLoading = false
    }

    fun updateData(coinModels: List<CoinModel>)
    {
        this.items = coinModels
        notifyDataSetChanged()
    }

}