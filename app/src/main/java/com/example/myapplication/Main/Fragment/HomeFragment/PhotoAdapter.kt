package com.example.myapplication.Main.Fragment.HomeFragment

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Main.Fragment.Search.SearchFragment
import com.example.myapplication.R
import java.util.Collections.emptyList

class PhotoAdapter(var context: Context) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    var dataList = emptyList<DataModel>()

    internal fun setDataList(dataList: List<DataModel>) {
        this.dataList = dataList
    }

    // Provide a direct reference to each of the views with data items

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var title: TextView
        //var desc: TextView

        init {
            image = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.title)
            //desc = itemView.findViewById(R.id.desc)
        }

    }
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the custom layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_layout, parent, false)
        return ViewHolder(view)
    }
    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val data = dataList[position]
        // Set item views based on your views and data model
        holder.title.text = data.title
        //holder.desc.text = data.desc
        holder.image.setImageResource(data.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, SearchFragment::class.java)
            intent.putExtra("tag",data.title)
            ContextCompat.startActivity(holder.itemView.context,intent,null)
        }

    }

    //  total count of items in the list
    override fun getItemCount() = dataList.size
}