package edu.ib.aplikacjaszybkieczytanie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.ib.aplikacjaszybkieczytanie.R
import edu.ib.aplikacjaszybkieczytanie.model.ResultModel
import kotlinx.android.synthetic.main.item_layout.view.author
import kotlinx.android.synthetic.main.item_layout.view.chapter
import kotlinx.android.synthetic.main.item_layout.view.title
import kotlinx.android.synthetic.main.result_item_layout.view.*

class ResultListAdapter(var resultListItems: List<ResultModel>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.result_item_layout,parent,false)
        return DescViewHolder(view)
    }

    override fun getItemCount(): Int {
        return resultListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DescViewHolder).bind(resultListItems[position])
    }

    class DescViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(rsvpModel: ResultModel){

            itemView.title.text=rsvpModel.title
            itemView.author.text=rsvpModel.author
            itemView.chapter.text=rsvpModel.chapter
            itemView.date.text=rsvpModel.date
            itemView.rate.text=rsvpModel.rate
            itemView.timePerWord.text = rsvpModel.timePerWord
            if (rsvpModel.mode.equals("RSVP")){
                itemView.numberOfWords.text= rsvpModel.numberOfWords
                itemView.numberOfLetters.visibility=View.GONE
                itemView.numOfLettTV.visibility=View.GONE
                itemView.numOfJumpResTV.visibility=View.GONE
                itemView.numberOfJumpReserve.visibility=View.GONE
            }else if (rsvpModel.mode.equals("Point")){
                itemView.numberOfLetters.visibility=View.GONE
                itemView.numOfLettTV.visibility=View.GONE
                itemView.numOfJumpResTV.visibility=View.GONE
                itemView.numberOfJumpReserve.visibility=View.GONE
                itemView.numberOfWords.visibility=View.GONE
                itemView.numOfWordTV.visibility=View.GONE
            }

        }
    }

}