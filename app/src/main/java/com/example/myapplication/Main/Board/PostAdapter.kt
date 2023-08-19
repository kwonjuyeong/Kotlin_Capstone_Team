package com.example.myapplication.Main.Board

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import org.w3c.dom.Text

class PostAdapter: RecyclerView.Adapter<PostAdapter.SliderItemViewHolder>() {

    private val data: ArrayList<String> = ArrayList();
    var callback: Callback? = null

    val clickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            v?.let { callback?.onItemClicked(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderItemViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.tag_item, parent, false)

        itemView.setOnClickListener(clickListener)

        val horizontalViewHolder = SliderItemViewHolder(itemView)
        return horizontalViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SliderItemViewHolder, position: Int) {
        if(data[position] == "null"){
            holder.tags.visibility = View.INVISIBLE
        }else {
            holder.tags.text = data[position]
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    interface Callback {
        fun onItemClicked(view: View)
    }

    class SliderItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)  {
        val tags : TextView = itemView.findViewById(R.id.tags_value)
    }
}
