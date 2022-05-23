package edu.ib.aplikacjaszybkieczytanie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import edu.ib.aplikacjaszybkieczytanie.adapter.TextListAdapter
import edu.ib.aplikacjaszybkieczytanie.firebase.FirebaseRepo
import edu.ib.aplikacjaszybkieczytanie.model.TextModel
import kotlinx.android.synthetic.main.activity_texts_list.*
import kotlin.collections.ArrayList

class TextsListActivity : AppCompatActivity(), (TextModel) -> Unit {

    private val firebaseRepo: FirebaseRepo =
        FirebaseRepo()

    private var textsList: List<TextModel> = ArrayList()
    private val textListAdapter: TextListAdapter =
        TextListAdapter(textsList, this)
    var title: String = ""
    var chapter: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texts_list)
        loadPostData()
        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = textListAdapter
    }



    private fun loadPostData() {
        firebaseRepo.getPostList().addOnCompleteListener {
            if (it.isSuccessful) {
                textsList = it.result!!.toObjects(TextModel::class.java)
                textListAdapter.textListItems = textsList
                textListAdapter.notifyDataSetChanged()


            } else {
                Toast.makeText(
                    applicationContext, "Nunu",
                    Toast.LENGTH_LONG
                ).show()

            }


        }


    }

    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        return (start..end).random()
    }

    override fun invoke(textModel: TextModel) {
        if (textModel.id.toInt() == 0) {
            val randId = rand(1, textListAdapter.itemCount - 1)
            //   println("RANDOMOWE ID"+randId)
            firebaseRepo.getRandText(randId.toLong()).addOnCompleteListener {
                if (it.isSuccessful) {
                    val randText = it.result!!.toObjects(TextModel::class.java)
                    title = randText[0].title
                    chapter = randText[0].chapter
                    Toast.makeText(this, "Wylosowano: ${title}, ${chapter}", Toast.LENGTH_LONG)
                        .show()
                    nextBtn.visibility=View.VISIBLE;

                } else {
                    Toast.makeText(
                        applicationContext, "Nunu",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            title = textModel.title
            chapter = textModel.chapter
            Toast.makeText(
                this,
                "Wybrano: ${textModel.title}, ${textModel.chapter}",
                Toast.LENGTH_LONG
            ).show()
            nextBtn.visibility=View.VISIBLE;

        }

    }


    fun onClickNextBtn(view: View) {
        firebaseRepo.getTextByTitleChapter(title, chapter).addOnCompleteListener {
            if (it.isSuccessful) {
                val selectedText = it.result!!.toObjects(TextModel::class.java)
                val contents = selectedText[0].contents

                var intent = Intent(this, ModeSettingsActivity::class.java)
                intent.putExtra("contents", contents)
                intent.putExtra("title", selectedText[0].title)
                intent.putExtra("author", selectedText[0].author)
                intent.putExtra("chapter", selectedText[0].chapter)
                startActivity(intent)

            } else {

                Toast.makeText(
                    applicationContext, "Nunu",
                    Toast.LENGTH_LONG
                ).show()
            }
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
        val intent = Intent(this, MenuActivity::class.java)
        this.startActivity(intent)
    }
}
