package edu.ib.aplikacjaszybkieczytanie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_mode_settings.*


class ModeSettingsActivity : AppCompatActivity(), (TextModel) -> Unit {


    private val firebaseRepo: FirebaseRepo = FirebaseRepo()

    private var postList: List<TextModel> = ArrayList()
    private val postListAdapter: PostListAdapter = PostListAdapter(postList,this)




//    private val db = FirebaseFirestore.getInstance()
//    private val textRef = db.collection("texts")
//    private var adapter: TextAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_settings)
        //setUpRecyclerView();
        loadPostData()
        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter=postListAdapter
    }

    private fun loadPostData(){
        firebaseRepo.getPostList().addOnCompleteListener {
            if (it.isSuccessful){
                postList = it.result!!.toObjects(TextModel::class.java)
                postListAdapter.postListItems = postList
                postListAdapter.notifyDataSetChanged()


            }else{
                Toast.makeText(
                    applicationContext, "Nunu",
                    Toast.LENGTH_LONG
                ).show()

            }


        }


    }






    override fun onStart() {
        super.onStart()
        recView.adapter!!.notifyDataSetChanged()
    }







    override fun invoke(textModel: TextModel) {
        Toast.makeText(this,"Clicked on item: ${textModel.title}",Toast.LENGTH_LONG).show()
        //card.setCardBackgroundColor(Color.GREEN)


    }

    fun onClickPlayBtn(view: View) {
        val mode:String = modeSpinner.selectedItem as String
        val time:String = timeSpinner.selectedItem as String
        val vel:String = velSpinner.selectedItem as String
        val intent = Intent(this, ReadingActivity::class.java)
        intent.putExtra("mode", mode)
        intent.putExtra("time", time)
        intent.putExtra("vel", vel)
        intent.putExtra("title", "Ludzie bezdomni")
        startActivity(intent)
    }



}
