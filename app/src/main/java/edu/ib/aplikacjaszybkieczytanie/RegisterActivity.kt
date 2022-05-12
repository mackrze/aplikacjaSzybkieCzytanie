package edu.ib.aplikacjaszybkieczytanie

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
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
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext, "Account created",
                            Toast.LENGTH_LONG
                        ).show()
                        val user = mAuth!!.currentUser
                    } else {
                        Toast.makeText(
                            applicationContext, "Account was not created",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }



    }
}
