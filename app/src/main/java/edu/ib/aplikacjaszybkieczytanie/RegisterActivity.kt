package edu.ib.aplikacjaszybkieczytanie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.firebase.auth.FirebaseAuth
import edu.ib.aplikacjaszybkieczytanie.firebase.FirebaseRepo

class RegisterActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private val firebaseRepo: FirebaseRepo =
        FirebaseRepo()
    var signUp: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth=FirebaseAuth.getInstance()
        signUp = findViewById<View>(R.id.registerBtn) as Button

    }



    fun onClickSignUpBtn(view: View) {
        val edtEmail = findViewById<View>(R.id.usernameText) as EditText
        val edtPassword = findViewById<View>(R.id.passwordText) as EditText
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else if (password.length < 7) {
            Toast.makeText(
                this,
                "Your password is too short (min.6 characters)",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            firebaseRepo.reg(email,password,applicationContext)
        }
    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater as MenuInflater
        inflater.inflate(R.menu.menu2,menu)
        return true
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

    fun backHomeOnClick(item: MenuItem) {
        //val intent = Intent(this, LoginActivity::class.java)
        finish()

    }
}
