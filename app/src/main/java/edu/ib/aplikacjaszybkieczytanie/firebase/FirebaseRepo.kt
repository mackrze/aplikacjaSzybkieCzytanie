package edu.ib.aplikacjaszybkieczytanie.firebase

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class FirebaseRepo() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getUser(): String ?{
        return firebaseAuth.currentUser?.uid
    }

    fun reg(email:String, password:String, applicationContext:Context): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isComplete){
                    Toast.makeText(
                        applicationContext, "Konto zostało utworzone",
                        Toast.LENGTH_LONG
                    ).show()
                }else
                {
                    Toast.makeText(
                        applicationContext, "Spróbuj ponownie",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }



        }


    fun getPostList(): Task<QuerySnapshot> {
        return firebaseFirestore
            .collection("texts").orderBy("id", Query.Direction.ASCENDING).get()

    }

    fun getRandText(id:Long): Task<QuerySnapshot>{
        return firebaseFirestore
            .collection("texts").whereEqualTo("id",id ).get()
    }

    fun getTextByTitleChapter(title:String, chapter:String): Task<QuerySnapshot>{
        return firebaseFirestore
            .collection("texts").whereEqualTo("title",title)
            .whereEqualTo("chapter",chapter)
            .get()
    }

    fun addNewResult(resultMap: MutableMap<String, String>): Task<DocumentReference>{
        return firebaseFirestore
            .collection("USERS").document(getUser().toString()).collection("Results")
            .add(resultMap)
    }

    fun getResultsByMode(mode:String): Task<QuerySnapshot>{
        return firebaseFirestore
            .collection("USERS").document(getUser().toString()).collection("Results").orderBy("date",Query.Direction.DESCENDING)
            .whereEqualTo("mode", mode).get()
    }






}


