package me.snowshadow.travelmantics

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_create_deal.*
import kotlinx.android.synthetic.main.fragment_deals.toolbar


private val TextInputEditText.txt: String
    get() = this.text.toString()

class CreateDealsFragment : Fragment() {

    private var deal: TravelDeal? = null
    private var uri: Uri? = null
    val dialog by lazy {
        SpotsDialog.Builder()
            .setContext(context)
            .setMessage("Executing Travel Deal")
            .setCancelable(false)
            .build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_create_deal, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)


        deal = arguments?.getParcelable<TravelDeal>("deal")

        if (deal == null) {
            save.text = "Save"
            toolbar.title = "Create Travel Deal"
        } else {
            save.text = "update"
            toolbar.title = "Delete Travel Deal"
            delete.visibility = View.VISIBLE
        }


        delete.setOnClickListener {
            dialog.show()
            FirebaseUtil.delete(deal, dealSuccess, dealFailed)
        }

        save.setOnClickListener {

            if (title.txt.isEmpty()) {
                title.error = "Please enter title"
                return@setOnClickListener
            }

            if (price.txt.isEmpty()) {
                price.error = "Please enter price"
                return@setOnClickListener
            }

            if (desc.txt.isEmpty()) {
                desc.error = "Please enter description"
                return@setOnClickListener
            }

            if (uri == null && deal == null) {
                Snackbar.make(view, "Please pick image", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            dialog.show()




            if (uri != null) {
                FirebaseUtil.uploadFile(uri, uploadSuccess, uploadFailed)
            } else {

                deal?.desc = desc.txt
                deal?.name = title.txt
                deal?.price = price.txt

                FirebaseUtil.updateDeal(deal, dealSuccess, dealFailed)
            }

        }

        bindData()


    }

    private fun bindData() {

        title.setText(deal?.name)
        price.setText(deal?.price)
        desc.setText(deal?.desc)

        Picasso.get()
            .load("https://firebasestorage.googleapis.com${deal?.imgUrl}?alt=media")
            .into(image)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pick, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.pick_image) {
            pickImage()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun pickImage() {

        RxImagePicker.with(childFragmentManager)
            .requestImage(Sources.CHOOSER, "Chooser Image")
            .subscribe {
                uri = it
                Picasso.get().load(it).into(image)

            }
    }

    private val uploadSuccess = fun(path: String) {

        if (deal == null) {
            FirebaseUtil.addDeal(
                TravelDeal("", title.txt, price.txt, desc.txt, path),
                dealSuccess,
                dealFailed
            )
        } else {

            deal?.desc = desc.txt
            deal?.name = title.txt
            deal?.price = price.txt
            deal?.imgUrl = path

            FirebaseUtil.updateDeal(deal, dealSuccess, dealFailed)

        }


    }

    private val uploadFailed = fun() {
        dialog.dismiss()

        AlertDialog.Builder(context!!)
            .setMessage("Failed to upload image")
            .create()
            .show()

    }

    val dealSuccess = fun(msg: String) {

        dialog.dismiss()

        AlertDialog.Builder(context!!)
            .setMessage(msg)
            .setOnDismissListener { activity?.onBackPressed() }
            .create()
            .show()

    }

    val dealFailed = fun(msg: String) {
        dialog.dismiss()
        AlertDialog.Builder(context!!)
            .setMessage(msg)
            .create()
            .show()

    }


}