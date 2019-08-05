package me.snowshadow.travelmantics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPause() {
        FirebaseUtil.unlistenAuthentication()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        FirebaseUtil.listenAuthentication(this)
    }

}
