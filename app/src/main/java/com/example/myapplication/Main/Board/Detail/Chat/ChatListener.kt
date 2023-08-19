package com.example.myapplication.Main.Board.Detail.Chat

interface ChatListener {
    fun getChatInfo(listener: ChatListener)
    fun setDrawerAdapter(userList : ArrayList<String>)
}