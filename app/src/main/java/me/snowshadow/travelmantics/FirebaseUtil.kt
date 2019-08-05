package me.snowshadow.travelmantics

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import java.util.*


object FirebaseUtil {

    private val RC_SIGN_IN = 325
    val TAG = FirebaseUtil::class.java.name


    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mStorageRef = FirebaseStorage.getInstance().reference

    fun loginUser(activity: Activity) {
        activity.startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )
                )
                .build(),
            RC_SIGN_IN
        )
    }


    val user: FirebaseUser?
        get() = mAuth.currentUser


    fun addDeal(deal: TravelDeal, dealAddSuccess: (msg: String) -> Unit, dealAddFailed: (msg: String) -> Unit) {

        val doc = FirebaseFirestore.getInstance()
            .collection("deals")
            .document()

        deal.id = doc.id

        doc.set(deal)
            .addOnSuccessListener {
                dealAddSuccess("Deal added success")
            }.addOnFailureListener { dealAddFailed("Deal added failed") }

    }

    fun dealsQuery(): Query {

        return FirebaseFirestore.getInstance()
            .collection("deals")


    }

    fun uploadFile(uri: Uri?, uploadSuccess: (i: String) -> Unit, uploadFailed: () -> Unit) {

        val riversRef = mStorageRef.child("${UUID.randomUUID()}")


        riversRef.putFile(uri!!).addOnSuccessListener { taskSnapshot ->

            taskSnapshot.storage.downloadUrl
                .addOnSuccessListener {

                    uploadSuccess(it.path.toString())

                }.addOnFailureListener {
                    uploadFailed()

                }
        }.addOnFailureListener {

        }
    }


    fun listenAuthentication(activity: Activity) {

        mAuth.addAuthStateListener {

            if (it.currentUser == null) {
                loginUser(activity)
            } else {

            }

        }
    }


    fun checkAdmin(listner: EventListener<QuerySnapshot>) {

        FirebaseFirestore.getInstance()
            .collection("admin")
            .whereEqualTo("uid", user?.uid)
            .addSnapshotListener(listner)
    }


    fun unlistenAuthentication() {


    }

    fun logOut(ctx: Context) {

        AuthUI.getInstance()
            .signOut(ctx)
    }

    fun updateDeal(deal: TravelDeal?, dealAddSuccess: (msg: String) -> Unit, dealAddFailed: (msg: String) -> Unit) {

        val doc = FirebaseFirestore.getInstance()
            .collection("deals")
            .document(deal?.id!!)

        doc.set(deal)
            .addOnSuccessListener { dealAddSuccess("Deal update success") }
            .addOnFailureListener { dealAddFailed("Deal update failed") }


    }

    fun delete(deal: TravelDeal?, dealAddSuccess: (msg: String) -> Unit, dealAddFailed: (msg: String) -> Unit) {

        mStorageRef.storage.getReferenceFromUrl("https://firebasestorage.googleapis.com${deal!!.imgUrl}")
            .delete().addOnSuccessListener {

                val doc = FirebaseFirestore.getInstance()
                    .collection("deals")
                    .document(deal.id)

                doc.delete()
                    .addOnSuccessListener { dealAddSuccess("Deal deleted success") }
                    .addOnFailureListener { dealAddFailed("Deal deletion failed") }

            }.addOnFailureListener { dealAddFailed("Deal deletion failed") }


    }


}

