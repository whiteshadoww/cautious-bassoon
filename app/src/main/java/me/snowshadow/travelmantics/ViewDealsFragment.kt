package me.snowshadow.travelmantics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_view_deals.*


class ViewDealsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_view_deals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val deal = arguments?.getParcelable<TravelDeal>("deal")

        toolbar.title = deal?.name

        price.text = "$${deal?.price}"
        desc.text = "${deal?.desc}"

        Picasso.get()
            .load("https://firebasestorage.googleapis.com${deal?.imgUrl}?alt=media")
            .into(image)
    }


}