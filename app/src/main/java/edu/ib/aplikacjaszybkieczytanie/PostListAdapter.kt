package edu.ib.aplikacjaszybkieczytanie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout.view.*


class PostListAdapter(var postListItems: List<TextModel>, val clickListener:(TextModel)->Unit):RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    class DescViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(textModel: TextModel, clickListener:(TextModel)->Unit){

            itemView.title.text=textModel.title
            itemView.author.text=textModel.author
            itemView.setOnClickListener{

                clickListener(textModel)
               // itemView.card.setCardBackgroundColor(Color.GREEN)

            }

        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return DescViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postListItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DescViewHolder).bind(postListItems[position],clickListener)
        //notifyDataSetChanged()



    }






}