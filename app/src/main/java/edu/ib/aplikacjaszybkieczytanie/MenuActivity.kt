package edu.ib.aplikacjaszybkieczytanie

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun onClickPlayBtn(view: View)
    {
        val intent = Intent(this, ModeSettingsActivity::class.java)
        this.startActivity(intent)
    }

    fun onClickShowResultsBtn(view: View) {}
    fun onClickLogOutBtn(view: View) {}
}
