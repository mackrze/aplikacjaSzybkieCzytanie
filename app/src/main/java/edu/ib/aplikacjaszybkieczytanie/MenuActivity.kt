package edu.ib.aplikacjaszybkieczytanie

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater as MenuInflater
        inflater.inflate(R.menu.menu,menu)
        return true
    }


    fun onClickPlayBtn(view: View)
    {
        val intent = Intent(this, TextsListActivity::class.java)
        this.startActivity(intent)
    }

    fun onClickShowResultsBtn(view: View) {
        val intent = Intent(this, ResultsListActivity::class.java)
        this.startActivity(intent)
    }
    fun onClickLogOutBtn(view: View) {
        val intent = Intent(this,LoginActivity::class.java)
        this.startActivity(intent)
    }


    fun showInfoOnClick(item: MenuItem) {
        Toast.makeText(
            applicationContext, "Info clicked",
            Toast.LENGTH_LONG
        ).show()

        showInfoDialog()

    }

    private fun showInfoDialog() {
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.layout_dialog_info)


        dialog.findViewById<TextView>(R.id.ok_button)
            .setOnClickListener {
                dialog.dismiss()
            }

        dialog.show()
    }
}
