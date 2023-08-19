package com.example.myapplication.Main.Board.Detail.Chat

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo
import com.example.myapplication.R
import com.example.myapplication.SettingFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_board_chat.*
import kotlinx.android.synthetic.main.activity_change_information.*
import kotlinx.android.synthetic.main.activity_chatadd.*
import java.text.SimpleDateFormat
import java.util.*

class BoardChat : AppCompatActivity() {
    private var firestore = FirebaseFirestore.getInstance()
    private var currentDTO = UserinfoDTO()
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private var Chats = BoardDTO.Chat()
    private lateinit var chatAdapter: ChatAdapter
    private var repo = Repo.StaticFunction.getInstance()
    private var drawerLayout: DrawerLayout? = null
    private var drawerView: View? = null
    private lateinit var chatOnlineAdapter: chatOnlineAdapter
    var commentUid: String? = null
    private val viewModel by lazy {
        ViewModelProvider(this).get(OnlinViewModel::class.java)
    }
    var listener: DrawerLayout.DrawerListener = object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
        override fun onDrawerOpened(drawerView: View) {}
        override fun onDrawerClosed(drawerView: View) {}
        override fun onDrawerStateChanged(newState: Int) {}
    }

    init {
        Log.e("init", "init: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_chat)
        commentUid = intent.getStringExtra("commentUid")!!
        val context = this


        chatOnlineAdapter = chatOnlineAdapter(this, commentUid!!)
        // 해당 게시글 uid를 intent로 받아옴
        firestore.collection("userid").document(uid!!).get().addOnCompleteListener {
            if (it.isSuccessful) {
                currentDTO = it.result?.toObject(UserinfoDTO::class.java)!!
            }
        }
        chatAdapter = ChatAdapter(commentUid!!)
        comment_recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
        status_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatOnlineAdapter
            observerData()}

        //getChatInfo()
        btn_comment_send?.setOnClickListener {
            // 채팅내용 업로드
            updateChat()
            setLastMessage()
            comment_text.setText("")
        }
        commant_back.setOnClickListener({                  //화면전환
            finish()


        })


        drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        drawerView = findViewById(R.id.drawer) as View
        val btn_open = findViewById<View>(R.id.hamburger) as ImageView
        btn_open.setOnClickListener {
            drawerLayout!!.openDrawer(drawerView!!)
        }
        drawerLayout!!.setDrawerListener(listener)
        drawerView!!.setOnTouchListener { v, event -> true }
        val ExitChat = findViewById<View>(R.id.ExitChat) as ImageButton
        ExitChat.setOnClickListener(){
            val builder = AlertDialog.Builder(this) //아래 builder.show 까지 명령어
            builder.setTitle("채팅방 나가기")
            builder.setMessage("채팅방에서 나가시겠습니까?\n나가기를 하면 대화내용이 모두 삭제되고\n채팅목록에서도 삭제됩니다.")
            builder.setPositiveButton(
                "확인"
            ) { dialogInterface: DialogInterface?, i: Int ->
                val BeR = firestore.collection("Chat").document(commentUid!!)
                firestore.runTransaction { transition ->
                    val chatDTO = transition.get(BeR).toObject(MessageDTO::class.java)
                    if (chatDTO!!.UserCheck.containsKey(uid)) {
                        chatDTO.UserCheck.remove(uid)
                    }
                    transition.set(BeR, chatDTO)
                }
                finish()
            }
            builder.setNegativeButton(
                "취소"
            ) { dialogInterface: DialogInterface?, i: Int ->
                //원하는 명령어
            }
            builder.show()
        }
    }

    private fun updateChat() {
        val commentUid = intent.getStringExtra("commentUid")!!
        Chats.UID = uid
        Chats.message = comment_text.text.toString()
        Chats.userprofile = currentDTO.ProfileUrl
        Chats.userNickname = currentDTO.nickname
        Chats.date = SimpleDateFormat("MM월 dd일").format(Date())
        Chats.timestamp = System.currentTimeMillis()
        firestore.collection("Chat").document(commentUid).collection("Messages").document()
            .set(Chats)
    }

    private fun setLastMessage() {
        val time = SimpleDateFormat("MM월 dd일").format(Date())
        val lastchat = comment_text.text.toString()
        val commentUid = intent.getStringExtra("commentUid")!!
        val docName = commentUid + "_last"
        val DoR = firestore.collection("Chat").document(commentUid)
        DoR.collection("LastMessage").document(docName).update(
            mapOf(
                "boardChatuid" to commentUid,
                "senderuid" to uid,
                "lastContent" to lastchat,
                "time" to time,
                "profileUrl" to currentDTO.ProfileUrl,
                "nickname" to currentDTO.nickname,
                "timeStamp" to System.currentTimeMillis()
            )
        )
        setResult(RESULT_OK)
    }


    override fun onPause() {
        super.onPause()
        repo.upDateOnlineState("offline")

    }

    override fun onResume() {
        super.onResume()
        repo.upDateOnlineState("online")


    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observerData() {
        viewModel.getChatOnlineData().observe(this, androidx.lifecycle.Observer {
            chatOnlineAdapter.setDataOnlineAdapter(it)
            chatOnlineAdapter.notifyDataSetChanged()
        })
    }

}