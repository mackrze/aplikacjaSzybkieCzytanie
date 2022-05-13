package edu.ib.aplikacjaszybkieczytanie

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class RsvpActivity  : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rsvp)
    }

    fun nextBtnClick (view: View){
        val intent = Intent(this, ReadingActivity::class.java)
        this.startActivity(intent)

    }
}