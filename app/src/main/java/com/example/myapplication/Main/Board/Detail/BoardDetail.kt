package com.example.myapplication.Main.Board.Detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.frag_board.*


class BoardDetail : AppCompatActivity(){
    private var boarddto = BoardDTO()
//    private val nickname : TextView = findViewById(R.id.boradCheck_nickname)
//    private val profile : ImageView = findViewById(R.id.boardCheck_profile)
//    private val title : TextView = findViewById(R.id.boradCheck_title)
//    private val date : TextView = findViewById(R.id.boradCheck_date)
//    private val contents : TextView = findViewById(R.id.boardCheck_contents)
//    private val expain : TextView = findViewById(R.id.board_explain)
//    private val boardimage : ImageView = findViewById(R.id.boardCheck_image)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)
        val chooseUid = intent.getStringExtra("contentsUid")!!
//        getData(this, chooseUid,)

        fun favoilitEvent() {

        }

//        override fun loadPage(noti: BoardDTO) {
//            val nickname: TextView = findViewById(R.id.boradCheck_nickname)
//            val profile: ImageView = findViewById(R.id.boardCheck_profile)
//            val title: TextView = findViewById(R.id.boradCheck_title)
//            val date: TextView = findViewById(R.id.boradCheck_date)
//            val contents: TextView = findViewById(R.id.boardCheck_contents)
//            val expain: TextView = findViewById(R.id.board_explain)
//            val boardimage: ImageView = findViewById(R.id.boardCheck_image)
//
//            Glide.with(this).load(boarddto.ProfileUrl).into(profile)
//            if (boarddto.imageUrlWrite != null) {
//                Glide.with(this).load(boarddto.imageUrlWrite).into(boardimage)
//                // 여기 레이아웃 set
//                expain.text = boarddto.imageWriteExplain.toString()
//            }
//            contents.text = boarddto.contents.toString()
//            date.text = boarddto.Writed_date.toString()
//            title.text = boarddto.postTitle.toString()
//            nickname.text = boarddto.nickname.toString()
//        }
    }
}