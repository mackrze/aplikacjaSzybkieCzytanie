package edu.ib.aplikacjaszybkieczytanie

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.ib.aplikacjaszybkieczytanie.firebase.FirebaseRepo
import kotlinx.android.synthetic.main.activity_resume.*
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.android.synthetic.main.result_item_layout.view.*

class ResumeActivity : AppCompatActivity() {

    private var rate:Float = 6f
    private var mode:String = ""
    private var timePerWord:Int = 0
    private var numberOfWords:Int = 0
    private var numberOfLetters:Int = 0
    private var numberOfJumpReserve:Int = 0
    private var author:String = ""
    private var title:String = ""
    private var chapter:String = ""
    private var date:String=""

    private var nFirestore: FirebaseFirestore? = null
    private var afFirestore: FirebaseAuth? = null
    private val firebaseRepo: FirebaseRepo =
        FirebaseRepo()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume)

        rate = intent.getFloatExtra("rate",5f)
        mode = intent.getStringExtra("mode")
        timePerWord = intent.getIntExtra("timePerWord",5)
        numberOfWords = intent.getIntExtra("numberOfWords",5)
        numberOfLetters = intent.getIntExtra("numberOfLetters",5)
        numberOfJumpReserve = intent.getIntExtra("numberOfJumpReserve",5)
        title = intent.getStringExtra("title")
        author = intent.getStringExtra("author")
        chapter = intent.getStringExtra("chapter")
        date=intent.getStringExtra("date")

//        textView.text = rate.toString()+System.lineSeparator()+mode+System.lineSeparator()+timePerWorde+System.lineSeparator()+numberOfWords+
//        System.lineSeparator()+numberOfLetters+System.lineSeparator()+numberOfJumpReserve+System.lineSeparator()+title+System.lineSeparator()+author+System.lineSeparator()+chapter

        val resultMap: MutableMap<String, String> = mutableMapOf<String,String>()
        resultMap.set("rate",rate.toString()) //wszystkie
        resultMap.set("mode", mode) //wszystkie
        resultMap.set("timePerWord",timePerWord.toString()) //wszystkie
        resultMap.set("title",title) //wszystkie
        resultMap.set("author",author) //wszystkie
        resultMap.set("chapter",chapter) //wszystkie
        resultMap.set("date",date)
        resultMap.set("numberOfWords",numberOfWords.toString()) //rsvp
        resultMap.set("numberOfLetters",numberOfLetters.toString()) //window
        resultMap.set("numberOfJumpReserve",numberOfJumpReserve.toString()) //window

        titleTV.text= title;authorTV.text=author; chapterTV.text=chapter; dateTV.text=date;rateTV.text=rate.toString();timePerWordTV.text=timePerWord.toString()

        if (mode.equals("RSVP")){
            numberOfWordsTV.text=numberOfWords.toString()
            numberOfLettersTV.visibility=View.GONE
            numOfLettTV.visibility=View.GONE
            numOfJumpResTV.visibility=View.GONE
            numberOfJumpReserveTV.visibility=View.GONE
        }else if (mode.equals("Point")){
            numberOfLettersTV.visibility=View.GONE
            numOfLettTV.visibility=View.GONE
            numOfJumpResTV.visibility=View.GONE
            numberOfJumpReserveTV.visibility=View.GONE
            numberOfWordsTV.visibility=View.GONE
            numOfWordTV.visibility=View.GONE
        }else
        {
            numberOfWordsTV.visibility=View.GONE
            numOfWordTV.visibility=View.GONE
            numberOfLettersTV.text=numberOfLetters.toString()
            numberOfJumpReserveTV.text=numberOfJumpReserve.toString()
        }


        firebaseRepo.addNewResult(resultMap)

    }



    fun showAllResultsOnClick(view: View) {
        val intent = Intent(this,ResultsListActivity::class.java )
        this.startActivity(intent)
    }

    fun nextPlayOnClick(view: View) {
        val intent = Intent(this, TextsListActivity::class.java)
        this.startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater as MenuInflater
        inflater.inflate(R.menu.menu3,menu)
        return true
    }

    fun backHomeOnClick(item: MenuItem) {
        val intent = Intent(this, MenuActivity::class.java)
        this.startActivity(intent)
    }
}