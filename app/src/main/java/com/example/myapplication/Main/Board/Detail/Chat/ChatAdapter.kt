package com.example.myapplication.Main.Board.Detail.Chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

@SuppressLint("NotifyDataSetChanged")
class  ChatAdapter(var boarduid : String): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val commentdto  : MutableList<BoardDTO.Chat> = arrayListOf()
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid
    init {
        FirebaseFirestore.getInstance().collection("Chat").document(boarduid).collection("Messages").orderBy("timestamp")
            .addSnapshotListener { value, error ->
                commentdto.clear()
                if(value == null) return@addSnapshotListener

                for(snapshot in value.documents!! ){
                    commentdto.add(snapshot.toObject(BoardDTO.Chat::class.java)!!)
                }
                notifyDataSetChanged()
            }
    }

    override fun getItemViewType(position: Int): Int {
        if(uid == commentdto[position].UID ){
            return 0
        }else{
            return 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view : View?
        return when(viewType){
            0 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.right_chat_item,parent,false)
                MultiViewHolder1(view)
            }
            else ->{
                view = LayoutInflater.from(parent.context).inflate(R.layout.left_chat_item,parent,false)
                MultiViewHolder2(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(commentdto[position].UID == uid){
            (holder as MultiViewHolder1).bind(commentdto[position])
            holder.setIsRecyclable(false)
        }
        else{
            (holder as MultiViewHolder2).bind(commentdto[position])
            holder.setIsRecyclable(false)
        }
    }

    override fun getItemCount() = commentdto.size

    inner class MultiViewHolder1(view : View) : RecyclerView.ViewHolder(view){
        private val nickname: TextView = view.findViewById(R.id.chat_nickname)
        private val contents : TextView = view.findViewById(R.id.chat_contents)
        private val date : TextView = view.findViewById(R.id.chat_date)
        private val profile : CircleImageView = view.findViewById(R.id.chat_profile)
        fun bind(item : BoardDTO.Chat){
            nickname.text = item.userNickname
            contents.text = item.message
            date.text = item.date
            if(item.userprofile != null){
                Glide.with(itemView).load(item.userprofile).into(profile)
            }
        }
    }
    inner class MultiViewHolder2(view : View) : RecyclerView.ViewHolder(view){
        private val nickname: TextView = view.findViewById(R.id.chat_nickname)
        private val contents : TextView = view.findViewById(R.id.chat_contents)
        private val date : TextView = view.findViewById(R.id.chat_date)
        private val profile : CircleImageView = view.findViewById(R.id.chat_profile)
        fun bind(item : BoardDTO.Chat){
            nickname.text = item.userNickname
            contents.text = item.message
            date.text = item.date
            if(item.userprofile != null){
                Glide.with(itemView).load(item.userprofile).into(profile)
            }
        }
    }
}



