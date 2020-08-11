package jasper.wagner.cryptotracking.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.loading_layout.view.*

class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val progressBar = itemView.progress_bar
}