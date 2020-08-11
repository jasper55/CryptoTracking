package jasper.wagner.cryptotracking

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jasper.wagner.cryptotracking.adapter.CoinAdapter
import jasper.wagner.cryptotracking.common.Common
import jasper.wagner.cryptotracking.`interface`.ILoadMore
import jasper.wagner.cryptotracking.model.CoinModel
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity(), ILoadMore {
    //Declare variable
    internal var items: MutableList<CoinModel> = ArrayList()
    internal lateinit var adapter: CoinAdapter
    private lateinit var client: OkHttpClient
    private lateinit var request: Request


    override fun onLoadMore() {
        if (items.size <= Common.MAX_COIN_LOAD)
            loadNext10Coin(items.size)
        else
            Toast.makeText(
                this@MainActivity,
                "Data max is " + Common.MAX_COIN_LOAD,
                Toast.LENGTH_SHORT
            )
                .show()
    }


    private fun loadNext10Coin(index: Int) {
        client = OkHttpClient()
        request = Request.Builder()
            .url(String.format(Common.API_URI_2, index))
            .header("Accept", "application/json")
            .addHeader("X-CMC_PRO_API_KEY", Common.API_KEY)
            .build()


        swipe_to_refresh.isRefreshing = true // SHow refresh
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("ERROR", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body!!.string()
                    Log.d("RESULT", body)
                    val gson = Gson()
                    val newItems = gson.fromJson<List<CoinModel>>(body,
                        object : TypeToken<List<CoinModel>>() {}.type
                    )
                    runOnUiThread {
                        items.addAll(newItems)
                        adapter.setLoaded()
                        adapter.updateData(items)

                        swipe_to_refresh.isRefreshing = false
                    }
                }


            })

    }

    private fun loadFirst10Coin() {
        client = OkHttpClient()
        request = Request.Builder()
            .url(String.format(Common.API_URI_INITIAL))
            .header("Accept", "application/json")
            .addHeader("X-CMC_PRO_API_KEY", Common.API_KEY)
            .build()


        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("ERROR", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    response.body?.let {
                        val body = it.string()
                        Log.d("Debug Coin", body)
                        val jsonResponse = JSONObject(body)

                        if (jsonResponse.has("status")) {
                            val json = jsonResponse.getJSONObject("status")
                            val errorCode = json.get("error_code")
                            if (errorCode != 0) {
                                val errorMessage = json.get("error_message")
                                runOnUiThread {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Could not receive data: $errorMessage",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    swipe_to_refresh.isRefreshing = false
                                }
                            } else {
                                val coinData = jsonResponse.getJSONArray("data").toString()
                                val gson = Gson()

                                items =
                                    gson.fromJson(
                                        coinData,
                                        object : TypeToken<List<CoinModel>>() {}.type
                                    )
                                runOnUiThread {
                                    adapter.updateData(items)
                                }
                            }
                        }
                    }
                }

            })

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe_to_refresh.post { loadFirst10Coin() }

        swipe_to_refresh.setOnRefreshListener {
            items.clear() // Remove all item
            loadFirst10Coin()
            setUpAdapter()
        }

        coin_recycler_view.layoutManager = LinearLayoutManager(this)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        adapter = CoinAdapter(coin_recycler_view as RecyclerView, this@MainActivity, items)
        (coin_recycler_view as RecyclerView).adapter = adapter
        adapter.setLoadMore(this)
    }
}
