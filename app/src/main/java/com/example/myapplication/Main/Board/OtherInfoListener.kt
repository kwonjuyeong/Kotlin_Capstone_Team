package com.example.myapplication.Main.Board

import com.example.myapplication.DTO.UserinfoDTO
import com.google.firebase.auth.FirebaseAuth

interface OtherInfoListener {
    fun loadData(userinfoDTO: UserinfoDTO)
    fun getOtherInfo(listener: OtherInfoListener, uid:String)
}