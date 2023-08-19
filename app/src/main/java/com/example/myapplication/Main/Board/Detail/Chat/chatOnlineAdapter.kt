package com.example.myapplication.Main.Board.Detail.Chat

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.DTO.StatusDTO
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

@SuppressLint("NotifyDataSetChanged")
class chatOnlineAdapter(val context: Context, val commentUid: String) :
    RecyclerView.Adapter<chatOnlineAdapter.ChatOnlineHolder>() {
    val firestore = FirebaseFirestore.getInstance()
    var statuslist = mutableListOf<StatusDTO>()
    var uid = FirebaseAuth.getInstance().currentUser!!.uid
    var infoList = mutableListOf<StatusDTO>()

    fun setDataOnlineAdapter(data: MutableList<StatusDTO>) {
        statuslist = data
        Log.e("infolit 확인", infoList.toString())
    }

    init {
        firestore.collection("Chat").document(commentUid)
            .addSnapshotListener { value, error ->
                val item = value!!.toObject(MessageDTO::class.java)
                if (item!!.UserCheck.containsKey(uid)) {
                    for (j in item.UserCheck.keys) {
                        for (i in statuslist) {
                            if (j == i.uid) {
                                infoList.add(i)
                                break
                            }
                        }
                    }
                }
            }
        notifyDataSetChanged()

    }

    class ChatOnlineHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile: CircleImageView = itemView.findViewById(R.id.status_profile)
        val nickname: TextView = itemView.findViewById(R.id.status_nickname)
        val online: ImageView = itemView.findViewById(R.id.status_sign)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatOnlineHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent, false)
        return ChatOnlineHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatOnlineHolder, position: Int) {
        val data = infoList[position]
        if (data.profileUrl.toString() != "null")
            Glide.with(holder.itemView.context).load(data.profileUrl).into(holder.profile)
        else
            holder.profile.setImageResource(R.drawable.ic_baseline_account_circle_signiture)
        Log.e("데이터 확인", data.status[uid].toString() )
        if (data.status["Online"] == "online") {
            holder.online.setImageResource(R.drawable.round_online)
        } else {
            holder.online.setImageResource(R.drawable.round_offline)

        }
        holder.nickname.text = data.nickname
    }

    override fun getItemCount() = infoList.size
}
