package edu.ib.aplikacjaszybkieczytanie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mode = intent.getStringExtra("mode")
        val time =intent.getStringExtra("time")
        val vel =intent.getStringExtra("vel")
        val title =intent.getStringExtra("title")
        println(mode + vel+time+title)
    }
}