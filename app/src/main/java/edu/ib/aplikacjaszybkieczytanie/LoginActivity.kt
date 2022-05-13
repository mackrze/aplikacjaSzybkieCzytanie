package edu.ib.aplikacjaszybkieczytanie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth=FirebaseAuth.getInstance()
    }

    fun onClickSignInBtn(view: View) {
        val edtUsername = findViewById<View>(R.id.usernameText) as EditText
        val edtPassword = findViewById<View>(R.id.passwordText) as EditText
        val email = edtUsername.text.toString()
        val password = edtPassword.text.toString()

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val user = mAuth!!.currentUser
                    Toast.makeText(
                        applicationContext, "Authentication OK.",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this, MenuActivity::class.java)
                    this.startActivity(intent)
                } else {
                    Toast.makeText(
                        applicationContext, "Authentication failed.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


    }
    fun onClickRegisterBtn(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        this.startActivity(intent)
    }
}
