package com.example.myapplication.Main.Fragment.BoardFragment.Recent

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.Main.Board.Detail.BoardDetail
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@SuppressLint("NotifyDataSetChanged")
class BoardListAdapter() : RecyclerView.Adapter<BoardListAdapter.CTViewholder>() {
    private var firestore = FirebaseFirestore.getInstance().collection("Board")
    private var boarddtos = mutableListOf<BoardDTO>()
    private var contentsUid = mutableListOf<String>()

    init {
        if (boarddtos.size > 0 && contentsUid.size > 0) {
            boarddtos.clear()
            contentsUid.clear()
        }
        firestore.orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                boarddtos.clear()
                contentsUid.clear()
                if (value == null) return@addSnapshotListener
                for (snapshot in value.documents) {
                    val item = snapshot.toObject(BoardDTO::class.java)
                    boarddtos.add(item!!)
                    contentsUid.add(snapshot.id)
                }
                notifyDataSetChanged()
            }


    }

    class CTViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile: ImageView = itemView.findViewById(R.id.boardlist_profile)
        val title: TextView = itemView.findViewById(R.id.boardlist_title)
        val Contents: TextView = itemView.findViewById(R.id.boarlist_content)
        val boarddate: TextView = itemView.findViewById(R.id.boardlist_date)
        val boardimage: ImageView = itemView.findViewById(R.id.boardlist_image)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CTViewholder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.board_list_item, parent, false)
        return CTViewholder(itemView)
    }

    override fun onBindViewHolder(holder: CTViewholder, position: Int) {
        val currentitem = boarddtos[position]
        val currentUid = contentsUid[position]
        holder.title.text = currentitem.postTitle
        holder.Contents.text = currentitem.contents
        holder.boarddate.text = currentitem.Writed_date
        if (currentitem.ProfileUrl.toString() == "null") {
            holder.profile.setImageResource(R.drawable.ic_baseline_account_circle_signiture)
        } else {
            Glide.with(holder.itemView.context).load(currentitem.ProfileUrl).into(holder.profile)
        }
        Glide.with(holder.itemView.context).load(currentitem.imageUrlWrite).into(holder.boardimage)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, BoardDetail::class.java)
            intent.putExtra("contentsUid", currentUid)
            intent.putExtra("owneruid", currentitem.uid)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }
    override fun getItemCount()= boarddtos.size
    fun clear(){
        boarddtos.clear()
        contentsUid.clear()
    }
}

