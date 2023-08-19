package com.example.myapplication.Main.Fragment.ChatFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.Main.Board.Detail.Chat.BoardChat
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

@SuppressLint("NotifyDataSetChanged")
class ChatListAdapter(val context: Context): RecyclerView.Adapter<ChatListAdapter.ChatListHolder>()  {
    private var lastMessageDTO = mutableListOf<MessageDTO.lastMessage>()
    fun setDataChatAapter(data:MutableList<MessageDTO.lastMessage>){
        lastMessageDTO = data
    }
    init {
        notifyDataSetChanged()
    }
    class ChatListHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var lastChatProfile : CircleImageView = itemView.findViewById(R.id.last_chat_profile)
        var lastChatNickname : TextView = itemView.findViewById(R.id.chat_tv_nickname)
        var lastChatContents : TextView = itemView.findViewById(R.id.chat_tv_contents)
        var lastChatTime : TextView = itemView.findViewById(R.id.chat_tv_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_layout_item,parent,false)
        return ChatListHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatListHolder, position: Int) {
        val data = lastMessageDTO[position]
        holder.lastChatNickname.text = data.nickname
        holder.lastChatContents.text = data.lastContent
        holder.lastChatTime.text = data.time
        Log.e("chatadapter", data.boardChatuid.toString() )
        if(data.profileUrl.toString() != "null")
            Glide.with(holder.itemView.context).load(data.profileUrl).into(holder.lastChatProfile)
        else
            holder.lastChatProfile.setImageResource(R.drawable.ic_baseline_account_circle_signiture)
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, BoardChat::class.java)
            intent.putExtra("commentUid",data.boardChatuid)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
        fun retrunuid() : String?{
            return data.boardChatuid
        }
    }

    override fun getItemCount() : Int {
        return lastMessageDTO.size
    }


}