package me.snowshadow.travelmantics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_deal.view.*

class TravelAdapter(val options: FirestoreRecyclerOptions<TravelDeal>, val travelDealClicked: TravelDealClicked) :
    FirestoreRecyclerAdapter<TravelDeal, TravelAdapter.TravelHolder>(options) {


    override fun onBindViewHolder(holder: TravelHolder, position: Int, model: TravelDeal) {
        holder.bind(getItem(holder.adapterPosition))
    }

    override fun onCreateViewHolder(group: ViewGroup, i: Int): TravelHolder {
        // Create a new instance of the ViewHolder, in this case we are using a custom
        // layout called R.layout.message for each item
        val view = LayoutInflater.from(group.context)
            .inflate(R.layout.row_deal, group, false)

        return TravelHolder(view)
    }

    inner class TravelHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun bind(item: TravelDeal) {

            val img = "https://firebasestorage.googleapis.com${item.imgUrl}?alt=media"

            Picasso.get().load(img)
                .into(v.image)

            v.title.text = item.name
            v.price.text = "$${item.price}"
            v.desc.text = item.desc
            v.setOnClickListener { travelDealClicked.dealClicked(item) }

        }


    }
}

interface TravelDealClicked {
    fun dealClicked(travelDeal: TravelDeal)
}