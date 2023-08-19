package com.example.myapplication.Main.Board.Detail.Chat

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.DTO.StatusDTO
import com.example.myapplication.Main.Fragment.ChatFragment.ChatRepo


class OnlinViewModel : ViewModel(){
    @SuppressLint("StaticFieldLeak")
    private var chatRepo = ChatRepo.StaticFunction.getInstance()

    fun getChatOnlineData(): LiveData<MutableList<StatusDTO>> {
        val userInfos = MutableLiveData<MutableList<StatusDTO>>()
        chatRepo.getUserStatus().observeForever {
                userInfos.value = it
            }
        return userInfos
    }
}