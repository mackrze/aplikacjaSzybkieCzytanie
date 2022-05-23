package edu.ib.aplikacjaszybkieczytanie

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_mode_settings.*
import kotlin.math.round


class ModeSettingsActivity : AppCompatActivity() {

    var contents:String =" "

    var mode:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_settings)
        contents =intent.getStringExtra("contents")
        mode = modeSpinner.selectedItem as String

        checkBoxRsvpPlus.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                layoutNumOfWords.visibility = View.VISIBLE
            } else {
                layoutNumOfWords.visibility = View.GONE
            }
        }




        seekBarTimePerWord.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                timePerWordView.text=((progress+10).toString()+"ms      (wpm = " + round((60000/(progress+10)).toDouble())+" )")
                val hsvColor = floatArrayOf(0f, 1f, 1f)
                hsvColor[0] = 100f * progress/ seekBar!!.max
                //seekBar!!.setBackgroundColor(Color.HSVToColor(hsvColor))
                seekBar.setProgressTintList(ColorStateList.valueOf(Color.HSVToColor(hsvColor)))
                seekBar.thumbTintList = ColorStateList.valueOf(Color.HSVToColor(hsvColor))

                if (progress <= (1.2*seekBar.max)/6){
                    categoryView.text ="Bardzo szybko"
                }else if (progress <=(2.05*seekBar.max)/6){
                    categoryView.text ="Szybko"
                }else if (progress <= (3.0*seekBar.max)/6){
                    categoryView.text ="Umiarkowanie"
                }else if (progress <= (4.8*seekBar.max)/6){
                    categoryView.text ="Wolno"
                }else {
                    categoryView.text ="Bardzo wolno"
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        }
        )



        seekBarNumOfLetters.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                numOfWords.text=(progress+1).toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekBarNumOfWords.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                numOfWordsTextView.text=(progress+2).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }

        })

        seekBarNumOfJumpReserve.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                numOfJumpReserve.text=progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })









        modeSpinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position==0){
                    checkBoxRsvpPlus.visibility=View.VISIBLE
                }else {
                    checkBoxRsvpPlus.visibility=View.GONE
                    checkBoxRsvpPlus.isChecked=false
                }

                if(position==1 || position==2){
                    checkBoxOptionX.visibility=View.VISIBLE
                    checkBoxBlur.visibility=View.VISIBLE
                }else {
                    checkBoxOptionX.visibility=View.GONE
                    checkBoxOptionX.isChecked=false
                    checkBoxBlur.visibility=View.GONE
                    checkBoxBlur.isChecked=false
                }

                if (position==2){
                    layoutForWindow.visibility=View.VISIBLE
                }else{
                    layoutForWindow.visibility=View.GONE
                }

            }
        }

//        if (mode.equals("RSVP")){
//            checkBoxRsvpPlus.visibility=View.VISIBLE
//        }else {
//            checkBoxRsvpPlus.visibility=View.GONE
//        }

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





    override fun onStart() {
        super.onStart()
    }


    fun onClickPlayBtn(view: View) {
        var intent2 = Intent(this, ReadingActivity::class.java)
        var intent3 = Intent(this, MenuActivity::class.java)
        // adding necessary values to intent
        mode = modeSpinner.selectedItem as String
        var timePerWord:Int = seekBarTimePerWord.progress+10
        intent2.putExtra("contents", contents)
        intent2.putExtra("mode", mode)
        intent2.putExtra("timePerWord", timePerWord)
        intent2.putExtra("title",intent.getStringExtra("title"))
        intent2.putExtra("author",intent.getStringExtra("author"))
        intent2.putExtra("chapter",intent.getStringExtra("chapter"))


        //        val time:String = timeSpinner.selectedItem as String
        // val vel:String = velSpinner.selectedItem as String

        //checking mode and adding specific values to intent
        if(mode=="RSVP") {
            var numberOfWords: Int = 1
            if (checkBoxRsvpPlus.isChecked) {
                numberOfWords = seekBarNumOfWords.progress+2
            }
            intent2.putExtra("numberOfWords", numberOfWords)
        } else if(mode=="Point"){
            var numberOfWords: Int = 1
            numberOfWords = seekBarNumOfWords.progress
            intent2.putExtra("numberOfWords", numberOfWords)
            if (checkBoxOptionX.isChecked){
                var optionX:Boolean = true
                intent2.putExtra("optionX",optionX)
            }
            if (checkBoxBlur.isChecked){
                var blur:Boolean = true
                intent2.putExtra("blur",blur)
            }
        }else{
            var numberOfLetters:Int = seekBarNumOfLetters.progress+1
            var numberOfJumpReserve:Int = seekBarNumOfJumpReserve.progress
            intent2.putExtra("numberOfLetters",numberOfLetters)
            intent2.putExtra("numberOfJumpReserve",numberOfJumpReserve)
            if (checkBoxOptionX.isChecked){
                var optionX:Boolean = true
                intent2.putExtra("optionX",optionX)
            }
            if (checkBoxBlur.isChecked){
                var blur:Boolean = true
                intent2.putExtra("blur",blur)
            }
        }

        intent3.putExtra("mode", mode)
        startActivity(intent2)
    }


}
