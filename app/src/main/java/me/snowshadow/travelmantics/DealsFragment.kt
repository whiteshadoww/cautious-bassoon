package me.snowshadow.travelmantics

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.EventListener
import kotlinx.android.synthetic.main.fragment_deals.*


class DealsFragment : Fragment(), TravelDealClicked {

    private var isAdmin: Boolean = false
    private val adapter by lazy {

        val options = FirestoreRecyclerOptions.Builder<TravelDeal>()
            .setQuery(FirebaseUtil.dealsQuery(), TravelDeal::class.java)
            .build()

        TravelAdapter(options, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_deals, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        deals_rec.layoutManager = LinearLayoutManager(context)
        deals_rec.adapter = adapter
        fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_dealsFragment_to_createDealsFragment))


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout) {
            FirebaseUtil.logOut(context!!)
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    override fun onResume() {
        adapter.startListening()
        FirebaseUtil.checkAdmin(
            EventListener { sn, _ ->

                if (fab == null) return@EventListener

                if (sn != null) {

                    if (sn.size() == 1) {
                        fab.show()
                        isAdmin = true
                        return@EventListener
                    }

                }
                fab.hide()
                isAdmin = false
            })
        super.onResume()
    }

    override fun dealClicked(travelDeal: TravelDeal) {

        Navigation.findNavController(view!!).navigate(
            if (!isAdmin) R.id.action_dealsFragment_to_viewDealsFragment
            else R.id.action_dealsFragment_to_createDealsFragment,
            Bundle().apply {
                putParcelable("deal", travelDeal)
            }
        )

    }

}