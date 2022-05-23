@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NAME_SHADOWING",
    "UNUSED_PARAMETER"
)

package edu.ib.aplikacjaszybkieczytanie

import android.app.Dialog
import android.content.Intent
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.MaskFilterSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.type.DateTime
import kotlinx.android.synthetic.main.activity_mode_settings.*
import kotlinx.android.synthetic.main.activity_reading.*
import java.time.LocalDate
import java.time.LocalTime


class ReadingActivity : AppCompatActivity() {


    private var contents: String = ""
    private var textView: TextView? = null
    private var mode: String = ""
    private var numberOfWords: Int = 1
    private var timePerWord: Int = 500
    private var numberOfLetters = 1
    private var numberOfJumpReserve = 0
    private var optionX = false
    private var blur = false
    private var rate:Float = 1f
    private var dateTime:String = ""

    @Volatile
    private var stopThread = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)



        //pobieranie danych z poprzedniego okna
        contents = intent.getStringExtra("contents")
        mode = intent.getStringExtra("mode")
        timePerWord = intent.getIntExtra("timePerWord", 500)
        numberOfWords = intent.getIntExtra("numberOfWords", 1)
        if (mode != "RSVP") {
            optionX = intent.getBooleanExtra("optionX", false)
            blur = intent.getBooleanExtra("blur", false)
        }
        if (mode == "Window") {
            numberOfLetters = intent.getIntExtra("numberOfLetters", 5)
            numberOfJumpReserve = intent.getIntExtra("numberOfJumpReserve", 0)
        }

        // metoda ustwaiająca wybrane textView jako działające
        textView = textViewSetUp(mode)
        textView!!.visibility = View.VISIBLE
        textView!!.movementMethod = ScrollingMovementMethod()

    }

    fun startBtnClick(view: View) {
        stopThread = false
        stop_btn.visibility = View.VISIBLE
        textView?.setBackgroundColor(Color.WHITE)


        showText() // głowna funkcja pokazująca teksty

        start_btn.visibility = View.GONE
    }

    fun stopBtnClick(view: View) {
        stopThread = true
        start_btn.visibility = View.VISIBLE
        stop_btn.visibility = View.GONE
    }

    private fun showText() {
        val parts = wordsSplitter(contents)
        val partsX = wordsX(parts)

        //sprawdzanie metody i uruchamianie odpowiedniej
        if (mode == "RSVP") {
            showTextRsvp(timePerWord, parts)
        } else if (mode == "Point") {
            showTextPoint(timePerWord, partsX, parts)
        } else if (mode == "Window") {
            if (numberOfLetters > numberOfJumpReserve) { // warunek aby tekst sie nie cofał
                showTextWindowed(timePerWord, partsX, parts, numberOfLetters, numberOfJumpReserve)
            }
        }
    }

    private fun showTextRsvp(
        ms: Int,
        textSplit: List<Word>
    ) {

        val handler = Handler()
        var count = 0

        val runnable: Runnable = object : Runnable { //obiekt runnable którym steruje handler
            override fun run() {
                if (stopThread) return //boolean odpowiadający za zatrzymanie
                // need to do tasks on the UI thread
                val ssb =
                    SpannableStringBuilder(textSplit[count].string) // string jako jedno słowo
                if (numberOfWords > 1) // dodawaie słow do tekstu
                    for (i in 1 until numberOfWords) {
                        count++
                        ssb.append(" " + textSplit[count].string)
                    }
                textView!!.text = ssb
                print(textSplit[count])
                if (count++ < textSplit.size - numberOfWords) { //warunek opozniajacy kazdą akcje o dany czas tak długo jak tekst nie osiagnął końca
                    handler.postDelayed(this, ms.toLong() * numberOfWords)
                } else {
                    val ssb2 =
                        SpannableStringBuilder(" ")
                    while (numberOfWords > 1 && count < textSplit.size) {
                        ssb2.append(" " + textSplit[count].string)
                        count++
                        textView!!.text = ssb2 // wyswietlanie fragmentu
                    }
                    endOfText()
                }
            }
        }

        handler.post(runnable)

    }

    private fun showTextPoint(
        ms: Int,
        textSplit: List<Word>,
        textSplitClear: List<Word>
    ) {
        val ssb = SpannableStringBuilder()
        if (optionX) { // jesli opcja X to zaiksuj tekst
            for (part in textSplit)
                ssb.append(part.string).append(" ")
        } else {
            for (part in textSplitClear)
                ssb.append(part.string).append(" ")
        }
        val bcsGreen = BackgroundColorSpan(Color.GREEN)
        val radiusMaxSize = 20
        val radiusUp = 1
        val blurMaskFilters = mutableListOf<BlurMaskFilter>()
        if (blur) {
            var radius = 1.0F
            for (i in 1..radiusMaxSize) {
                val blurMask =
                    BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
                blurMaskFilters.add(blurMask)
                radius += radiusUp
            }
        }

        val handler = Handler()
        var j = 0
        val spannableStringBuilders = mutableListOf<SpannableStringBuilder>()
        for (count in textSplit.indices) {
            val ssb = SpannableStringBuilder()
            if (optionX) { // jesli opcja X to zaiksuj tekst
                for (part in textSplit)
                    ssb.append(part.string).append(" ")
            } else {
                for (part in textSplitClear)
                    ssb.append(part.string).append(" ")
            }
            if (optionX) // jak opcja X to zamien słowo Xxxx na normalne
                ssb.replace(
                    textSplit[count].firstLetterPlace,
                    textSplit[count].lastLetterPlace,
                    textSplitClear[count].string
                )
            ssb.setSpan( // nadawanie koloru
                bcsGreen,
                textSplit[count].firstLetterPlace,
                textSplit[count].lastLetterPlace,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            if (blur) {
                ssb.setSpan( //blur tekstu
                    MaskFilterSpan(blurMaskFilters[radiusMaxSize - 1]),
                    textSplit[0].firstLetterPlace,
                    textSplit[textSplit.size - 1].lastLetterPlace,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                ssb.setSpan( //blur tekstu
                    MaskFilterSpan(blurMaskFilters[0]),
                    textSplit[count].firstLetterPlace,
                    textSplit[count].lastLetterPlace,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                for (i in 1..radiusMaxSize) {
                    if (count >= i) {
                        ssb.setSpan( //blur tekstu
                            MaskFilterSpan(blurMaskFilters[i - 1]),
                            textSplit[count - i].firstLetterPlace,
                            textSplit[count - i].lastLetterPlace,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE
                        )
                    }
                    if (count < textSplit.size - i) {
                        ssb.setSpan( //blur tekstu
                            MaskFilterSpan(blurMaskFilters[i - 1]),
                            textSplit[count + i].firstLetterPlace,
                            textSplit[count + i].lastLetterPlace,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE
                        )

                    }
                }
            }
            spannableStringBuilders.add(ssb)
        }

        val runnable: Runnable = object : Runnable { //obiekt runnable którym steruje handler
            override fun run() {
                if (stopThread) return //boolean odpowiadający za zatrzymanie
                textView!!.text = spannableStringBuilders[j]
                if (j++ < textSplit.size - 1) { //warunek opozniajacy kazdą akcje o dany czas tak długo jak tekst nie osiagnął końca
                    handler.postDelayed(this, ms.toLong())
                } else {
                    endOfText()
                }
            }
        }
        handler.post(runnable)
    }

    private fun showTextWindowed(
        ms: Int,
        textSplit: List<Word>,
        textSplitClear: List<Word>,
        numberOfLetters: Int,
        steps: Int
    ) {
        val sbClear = StringBuilder()
        val sb = StringBuilder()
        if (optionX) {
            for (part in textSplit)
                sb.append(part.string).append(" ")
        } else
            for (part in textSplitClear)
                sb.append(part.string).append(" ")
        for (part in textSplitClear)
            sbClear.append(part.string).append(" ")
        val charArray = sb.toString().toCharArray()
        val charArrayClear = sbClear.toString().toCharArray()
        val bcsGreen = BackgroundColorSpan(Color.GREEN)
        val handler = Handler()
        var startLetterInText = 0
        var endLetterInText = 0
        var remainLetters = numberOfLetters
        val placeOfLetterInText = mutableListOf<Int>()
        var positionOfSign = 0

        val radiusMaxSize = 20
        val radiusUp = 1
        val blurMaskFilters = mutableListOf<BlurMaskFilter>()
        if (blur) {
            var radius = 1.0F
            for (i in 1..radiusMaxSize) {
                val blurMask =
                    BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
                blurMaskFilters.add(blurMask)
                radius += radiusUp
            }
        }

        //ustawianie kazdej literze miejsca w tekscie, omija spacje i przecinki
        for (sign in charArrayClear) {
            if (sign.isLetter()) {
                placeOfLetterInText.add(positionOfSign)
                positionOfSign++
            } else {
                positionOfSign++
            }
        }


        val spannableStringBuilders = mutableListOf<SpannableStringBuilder>()
        var count = 0
        while (placeOfLetterInText.size - startLetterInText > numberOfLetters) {
            val ssb = SpannableStringBuilder()
            if (optionX) {
                for (part in textSplit)
                    ssb.append(part.string).append(" ")
            } else
                for (part in textSplitClear)
                    ssb.append(part.string).append(" ")

            while (remainLetters > 0) {
                endLetterInText++
                remainLetters--
            }

            if (optionX) {
                for (i in 0 until numberOfLetters) {
                    ssb.replace(
                        placeOfLetterInText[startLetterInText + i],
                        placeOfLetterInText[startLetterInText + i] + 1,
                        charArrayClear[placeOfLetterInText[startLetterInText + i]].toString()
                    )
                }
            }
            if (blur) {
                ssb.setSpan( //blur tekstu
                    MaskFilterSpan(blurMaskFilters[radiusMaxSize - 1]),
                    textSplit[0].firstLetterPlace,
                    textSplit[textSplit.size - 1].lastLetterPlace,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                ssb.setSpan( //blur tekstu
                    MaskFilterSpan(blurMaskFilters[0]),
                    placeOfLetterInText[startLetterInText],
                    placeOfLetterInText[startLetterInText + numberOfLetters],
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                for (i in 1..radiusMaxSize) {
                    if (startLetterInText > i * numberOfLetters) {
                        ssb.setSpan( //blur tekstu
                            MaskFilterSpan(blurMaskFilters[i - 1]),
                            placeOfLetterInText[startLetterInText - numberOfLetters * i],
                            placeOfLetterInText[startLetterInText - numberOfLetters * (i - 1)],
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE
                        )

                    } else {
                        ssb.setSpan( //blur tekstu
                            MaskFilterSpan(blurMaskFilters[i - 1]),
                            placeOfLetterInText[0],
                            placeOfLetterInText[startLetterInText - numberOfLetters * (i - 1)],
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE
                        )
                        break
                    }
                }

                for (i in 1..radiusMaxSize) {
                    if (startLetterInText < placeOfLetterInText.size - 1 - numberOfLetters * (i + 1)) {
                        ssb.setSpan( //blur tekstu
                            MaskFilterSpan(blurMaskFilters[i - 1]),
                            placeOfLetterInText[startLetterInText + numberOfLetters * i],
                            placeOfLetterInText[startLetterInText + numberOfLetters * (i + 1)],
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE
                        )
                    } else {
                        ssb.setSpan( //blur tekstu
                            MaskFilterSpan(blurMaskFilters[i - 1]),
                            placeOfLetterInText[startLetterInText + numberOfLetters * i],
                            placeOfLetterInText[placeOfLetterInText.size - 1] + 1,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE
                        )
                        break
                    }
                }
            }


            ssb.setSpan(
                bcsGreen,
                placeOfLetterInText[startLetterInText],
                placeOfLetterInText[endLetterInText],
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableStringBuilders.add(ssb)





            startLetterInText = endLetterInText - steps
            endLetterInText = startLetterInText
            remainLetters = numberOfLetters
        }


        val runnable: Runnable = object : Runnable { //obiekt runnable którym steruje handler
            override fun run() {
                if (stopThread) return //boolean odpowiadający za zatrzymanie
                // need to do tasks on the UI thread

                textView!!.text = spannableStringBuilders[count]


                if (count++ < spannableStringBuilders.size - 1) { //warunek opozniajacy kazdą akcje o dany czas tak długo jak tekst nie osiagnął końca
                    handler.postDelayed(this, ms.toLong())
                } else {
                    val ssb = spannableStringBuilders[count - 1]
                    if (optionX) {
                        for (i in 0 until placeOfLetterInText.size)
                            ssb.replace(
                                placeOfLetterInText[i],
                                placeOfLetterInText[i] + 1,
                                charArray[placeOfLetterInText[count]].toString()
                            )
                        var count = endLetterInText
                        while (count < placeOfLetterInText.size) {
                            ssb.replace(
                                placeOfLetterInText[count],
                                placeOfLetterInText[count] + 1,
                                charArrayClear[placeOfLetterInText[count]].toString()
                            )
                            count++
                        }
                    }
                    ssb.setSpan(
                        bcsGreen,
                        placeOfLetterInText[endLetterInText],
                        placeOfLetterInText[placeOfLetterInText.size - 1] + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    textView!!.text = ssb

                    endOfText()
                }
            }
        }

        handler.post(runnable)
    }


    //funkcja ustawiajaca odpowiednie textView jako widoczne i zwracająca ten textView do dalszego wykorzystania
    private fun textViewSetUp(mode: String): TextView {
        return if (mode == "RSVP")
            textViewRsvp
        else
            textViewNormal
    }

    //funkcja dzieląca tekst na słowa (Word)
    private fun wordsSplitter(contents: String): List<Word> {
        val parts = mutableListOf<Word>()
        val text = contents
        val textSplit = text.split(" ".toRegex()).toTypedArray()
        var letterPlace = 0
        for (part in textSplit) if (part.isNotEmpty() && part != " ") {
            val word = Word(part, letterPlace, letterPlace + part.length)
            letterPlace += part.length + 1
            parts.add(word)
        }
        return parts
    }

    // dzieli tekst na słowa zaiksowane (Xxxxx)
    private fun wordsX(parts: List<Word>): List<Word> {
        val partsX = mutableListOf<Word>()
        val stringBuilder = StringBuilder()
        var letterPlace = 0
        for (part in parts) {
            for (letter in part.string.toCharArray()) {
                if (letter.isLetter() && letter.isUpperCase()) {
                    stringBuilder.append('X')
                } else if (letter.isLetter() && letter.isLowerCase()) {
                    stringBuilder.append('x')
                } else {
                    stringBuilder.append(letter)
                }
            }
            val word =
                Word(stringBuilder.toString(), letterPlace, letterPlace + stringBuilder.length)
            letterPlace += stringBuilder.length + 1
            partsX.add(word)
            stringBuilder.clear()
        }
        return partsX
    }

    // klasa słowo przetrzymująca treść i początkowe i końcowe miejce liter w całym tekscie
    class Word(var string: String, var firstLetterPlace: Int, var lastLetterPlace: Int)

    private fun endOfText() {
        val dateOfGame = LocalDate.now()
        val timeOfGame = LocalTime.now()
        var hour:String = ""
        var minute:String=""
        if (timeOfGame.hour<10){
            hour = "0"+timeOfGame.hour.toString()

        }else
        {
            hour = timeOfGame.hour.toString()

        }

        if (timeOfGame.minute<10){
            minute="0"+timeOfGame.minute.toString()
        }else{
            minute=timeOfGame.minute.toString()
        }
        dateTime = dateOfGame.toString()+" "+hour+":"+minute+":"+timeOfGame.second.toString()

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_dialog_rating)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//        val dialog = MaterialDialog(this)
//            .noAutoDismiss()
//            .customView(R.layout.layout_dialog_rating)
//
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.RED))

        val ratingBar = dialog.findViewById<RatingBar>(R.id.rating_bar)

        dialog.findViewById<TextView>(R.id.resume_button)
            .setOnClickListener {
                //dialog.dismiss()
                rate = ratingBar.rating
                val intentResumeActivity = Intent(this, ResumeActivity::class.java)
                // adding necessary values to intent
                // mode = modeSpinner.selectedItem as String
                // var timePerWord:Int = seekBarTimePerWord.progress+10
                println("Ocena: "+rate)
                intentResumeActivity.putExtra("rate",rate)
                intentResumeActivity.putExtra("mode", mode)
                intentResumeActivity.putExtra("timePerWord",timePerWord)
                intentResumeActivity.putExtra("numberOfWords",numberOfWords)
                intentResumeActivity.putExtra("numberOfLetters",numberOfLetters)
                intentResumeActivity.putExtra("numberOfJumpReserve",numberOfJumpReserve)
                intentResumeActivity.putExtra("title", intent.getStringExtra("title"))
                intentResumeActivity.putExtra("author", intent.getStringExtra("author"))
                intentResumeActivity.putExtra("chapter", intent.getStringExtra("chapter"))
                intentResumeActivity.putExtra("date", dateTime)
                this.startActivity(intentResumeActivity)

            }


        dialog.show()

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