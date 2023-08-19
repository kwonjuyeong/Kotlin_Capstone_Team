package com.example.myapplication.Main.Fragment.ChatFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.DTO.StatusDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepo {
    private var firestore = FirebaseFirestore.getInstance()
    private var userUid = FirebaseAuth.getInstance().currentUser!!.uid
    private var messageDTO = mutableListOf<MessageDTO>()
    private var lastMessageDTO = mutableListOf<MessageDTO.lastMessage>()
    private var liveLastData = MutableLiveData<MutableList<MessageDTO.lastMessage>>()
    private var statusList = mutableListOf<StatusDTO>()
    private var liveStatusList = MutableLiveData<MutableList<StatusDTO>>()
    //private var liveLatestMessages = MutableLiveData<MutableList<MessageDTO.lastMessage>>

    object StaticFunction {
        private var instance: ChatRepo? = null
        fun getInstance(): ChatRepo {
            if (instance == null)
                instance = ChatRepo()
            return instance!!
        }
    }

    fun CheckChattingRoom() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        firestore.collection("Chat").orderBy("timeStamp")
            .addSnapshotListener { value, error ->
                messageDTO.clear()
                if (value == null) return@addSnapshotListener
                for (document in value.documents) {
                    val temp = document.toObject(MessageDTO::class.java)
                    if (temp?.UserCheck?.containsKey(uid) == true)
                        messageDTO.add(temp)
                    //messageDTO.add(temp!!)
                    //Log.e("메세지 리스트 확인", messageDTO.toString())
                }
            }
    }

    fun returnLastMessageDTO(): LiveData<MutableList<MessageDTO.lastMessage>> {
        lastMessageDTO.clear()
        for (i in messageDTO) {
            val docName = i.boardUid + "_last"
            firestore.collection("Chat").document(i.boardUid.toString()).collection("LastMessage")
                .document(docName).addSnapshotListener { value, error ->
                    val item = value?.toObject(MessageDTO.lastMessage::class.java)

                    for (j in 0 until lastMessageDTO.size) {
                        if (item?.boardChatuid == lastMessageDTO[j].boardChatuid)
                            lastMessageDTO.removeAt(j)
                    }
                    lastMessageDTO.add(item!!)
                    lastMessageDTO.reverse()
                    liveLastData.value = lastMessageDTO
                }
        }
        return liveLastData
    }

    fun getUserStatus() : LiveData<MutableList<StatusDTO>> {
        firestore.collection("Status").orderBy("timestamp")
            .addSnapshotListener { value, error ->
                for (document in value!!.documents) {
                    val item = document.toObject(StatusDTO::class.java)
                    statusList.add(item!!)
                    liveStatusList.value = statusList
                }
            }
        return liveStatusList
    }
}
