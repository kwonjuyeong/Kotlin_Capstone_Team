package com.example.myapplication.Main.Fragment.ChatFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.DTO.MessageDTO

class ChatListViewModel : ViewModel() {
    private var chatRepo = ChatRepo.StaticFunction.getInstance()

    fun getChatListData() : LiveData<MutableList<MessageDTO.lastMessage>> {
        var listLastData = MutableLiveData<MutableList<MessageDTO.lastMessage>>()
        chatRepo.returnLastMessageDTO().observeForever {
            listLastData.value = it
        }
        return listLastData
    }
} 