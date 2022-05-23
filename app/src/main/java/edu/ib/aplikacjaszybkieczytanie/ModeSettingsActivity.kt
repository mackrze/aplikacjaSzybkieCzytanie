package edu.ib.aplikacjaszybkieczytanie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_mode_settings.*


class ModeSettingsActivity : AppCompatActivity() {

    var contents:String =" "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_settings)
        contents =intent.getStringExtra("contents")
    }





    override fun onStart() {
        super.onStart()
    }


    fun onClickPlayBtn(view: View) {
//        val mode:String = modeSpinner.selectedItem as String
//        val time:String = timeSpinner.selectedItem as String
//       // val vel:String = velSpinner.selectedItem as String

        var intent2 = Intent(this, ReadingActivity::class.java)
        intent2.putExtra("contents", contents)
//        intent.putExtra("time", time)
//        //intent.putExtra("vel", vel)
//        intent.putExtra("title", title)
//        intent.putExtra("chapter",chapter)
        startActivity(intent2)
    }



}
