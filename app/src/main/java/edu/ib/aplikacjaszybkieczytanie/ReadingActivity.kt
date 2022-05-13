package edu.ib.aplikacjaszybkieczytanie

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_reading.*


class ReadingActivity : AppCompatActivity() {

    @Volatile
    private var stopThread = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)
    }

    fun startBtnClick(view: View) {
        stopThread = false
        stop_btn.visibility = View.VISIBLE
        if (milliseconds.text.toString().isBlank() || milliseconds.text.toString().isEmpty()
        )
            showText(500)
        else if(milliseconds.text.toString().toInt()>0)
            showText(milliseconds.text.toString().toInt())
        else showText(500)


    }

    fun stopBtnClick(view: View) {
        stopThread = true
    }

    private fun showText(ms: Int) {
        //val text = "To bedzie jakis bardzo długi tekst który będzie wyświeltany na ekranie"
        val text = "Rok 1647 był to dziwny rok, w którym rozmaite znaki na niebie i ziemi zwiastowały jakoweś klęski i nadzwyczajne zdarzenia.Współcześni kronikarze wspominają, iż z wiosny szarańcza w niesłychanej ilości wyroiła się z Dzikich Pól i zniszczyła zasiewy i trawy, co było przepowiednią napadów tatarskich. Latem zdarzyło się wielkie zaćmienie słońca, a wkrótce potem kometa pojawiła się na niebie. W Warszawie widywano też nad miastem mogiłę i krzyż ognisty w obłokach; odprawiano więc posty i dawano jałmużny, gdyż niektórzy twierdzili, że zaraza spadnie na kraj i wygubi rodzaj ludzki. Nareszcie zima nastała tak lekka, że najstarsi ludzie nie pamiętali podobnej. W południowych województwach lody nie popętały wcale wód, które podsycane topniejącym każdego ranka śniegiem wystąpiły z łożysk i pozalewały brzegi. Padały częste deszcze. Step rozmókł i zmienił się w wielką kałużę, słońce zaś w południe dogrzewało tak mocno, że — dziw nad dziwy! — w województwie bracławskim i na Dzikich Polach zielona ruń okryła stepy i rozłogi już w połowie grudnia. Roje po pasiekach poczęły się burzyć i huczeć, bydło ryczało po zagrodach. Gdy więc tak porządek przyrodzenia zdawał się być wcale odwróconym, wszyscy na Rusi, oczekując niezwykłych zdarzeń, zwracali niespokojny umysł i oczy szczególniej ku Dzikim Polom, od których łatwiej niźli skądinąd mogło się ukazać niebezpieczeństwo."
        val textSplit = text.split(" ".toRegex()).toTypedArray()
        val handler = Handler()
        var count = 0


        val runnable: Runnable = object : Runnable { //obiekt runnable którym steruje handler
            override fun run() {
                if (stopThread) return //boolean odpowiadający za zatrzymanie
                // need to do tasks on the UI thread
                textView.text = textSplit[count] // wyswietlanie fragmentu
                print(textSplit[count])
                if (count++ < textSplit.size - 1) { //warunek opozniajacy kazdą akcje o dany czas tak długo jak tekst nie osiagnął końca
                    handler.postDelayed(this, ms.toLong())
                }
            }
        }

        handler.post(runnable)

    }
}