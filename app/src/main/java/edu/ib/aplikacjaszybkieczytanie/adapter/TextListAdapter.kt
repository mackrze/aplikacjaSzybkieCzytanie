package edu.ib.aplikacjaszybkieczytanie.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.ib.aplikacjaszybkieczytanie.R
import edu.ib.aplikacjaszybkieczytanie.TextsListActivity
import edu.ib.aplikacjaszybkieczytanie.model.TextModel
import kotlinx.android.synthetic.main.item_layout.view.*

class TextListAdapter(var textListItems: List<TextModel>, val clickListener: TextsListActivity):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return DescViewHolder(view)
    }

    override fun getItemCount(): Int {
        return textListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DescViewHolder).bind(textListItems[position],clickListener)
    }

    class DescViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(textModel: TextModel, clickListener:(TextModel)->Unit){
            if (textModel.id.toInt()==0) {
                itemView.card.setCardBackgroundColor(Color.parseColor("#E8B6B3A9"))
            }

            itemView.title.text=textModel.title
            itemView.author.text=textModel.author
            itemView.chapter.text=textModel.chapter
            itemView.setOnClickListener {
                clickListener(textModel)
            }
        }

    }
}