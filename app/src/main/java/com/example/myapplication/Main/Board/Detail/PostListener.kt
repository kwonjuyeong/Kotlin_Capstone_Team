package com.example.myapplication.Main.Board.Detail

import com.example.myapplication.DTO.BoardDTO

interface PostListener {
    fun loadPage(noti : BoardDTO)
    fun getData(listener: PostListener, chooseUid : String)
    fun createChatting(listener: PostListener)
    fun joinChat()

}
