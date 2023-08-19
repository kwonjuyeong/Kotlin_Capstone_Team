package com.example.myapplication.Main.Fragment.BoardFragment.Mine

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

@SuppressLint("NotifyDataSetChanged")
class MineAdapter(val context : Context) : RecyclerView.Adapter<MineAdapter.MViewholder>() {
    private var firestore = FirebaseFirestore.getInstance()
    private val Mef = firestore.collection("Board")
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid
    private var myList = mutableListOf<BoardDTO>()
    private var contentsUid = mutableListOf<String>()

    fun clear(){
        contentsUid.clear()
    }
    init {
        if (myList.size > 0 && contentsUid.size > 0) {
            myList.clear()
            contentsUid.clear()
        }
        Mef.whereEqualTo("uid", uid).addSnapshotListener { value, error ->
            myList.clear()
            contentsUid.clear()
            for(document in value!!.documents){
                val item = document.toObject(BoardDTO::class.java)
                myList.add(item!!)
                contentsUid.add(document.id)
            }
            notifyDataSetChanged()
        }

    }
    class MViewholder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val profile : CircleImageView = itemView.findViewById(R.id.boardlist_profile)
        val title: TextView = itemView.findViewById(R.id.boardlist_title)
        val Contents: TextView = itemView.findViewById(R.id.boarlist_content)
        val boarddate: TextView = itemView.findViewById(R.id.boardlist_date)
        val boardimage: ImageView = itemView.findViewById(R.id.boardlist_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewholder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.board_list_item,parent,false)
        return MViewholder(itemView)
    }

    override fun onBindViewHolder(holder: MViewholder, position: Int) {
        val currentitem = myList[position]
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
            intent.putExtra("owneruid", uid)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount() = myList.size
}