package com.example.myapplication.Main.Fragment.Search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.Main.Activity.MainActivity
import com.example.myapplication.Main.Board.Detail.BoardDetail
import com.example.myapplication.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_now_my_place.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.search_item.view.*
import kotlinx.android.synthetic.main.view_item_layout.view.*

class SearchFragment : AppCompatActivity() {

    private var firestore: FirebaseFirestore? = null
    private var word: String? = null
    private var searchOption: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        // 파이어스토어 인스턴스 초기화
        firestore = FirebaseFirestore.getInstance()

        if (intent.getStringExtra("tag") != null) {
            word = intent.getStringExtra("tag")
            search_recyclerview.adapter = RecyclerViewAdapter(false, word!!)
            search_recyclerview.layoutManager = LinearLayoutManager(this)


            Log.e("words", word.toString())
            search_what.visibility = View.VISIBLE
            search_what.text = "'${word}'에 대한 검색 결과"

            (search_recyclerview.adapter as RecyclerViewAdapter).checkIsFalse(word!!)

        } else {
            searchOption = "search_title"
            search_recyclerview.adapter = RecyclerViewAdapter(true, "null")
            search_recyclerview.layoutManager = LinearLayoutManager(this)
        }

        // 스피너 옵션에 따른 동작
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (spinner.getItemAtPosition(position)) {
                    "제목" -> {
                        searchOption = "postTitle"
                    }
                    "내용" -> {
                        searchOption = "contents"
                    }
                    "닉네임" -> {
                        searchOption = "nickname"
                    }
                }
            }
        }

        // 검색 옵션에 따라 검색
        searchBtn.setOnClickListener {
            search_what.visibility = View.VISIBLE
            word = searchWord.text.toString()
            search_what.text = "'${word}'에 대한 검색 결과"
            (search_recyclerview.adapter as RecyclerViewAdapter).search(word!!, searchOption!!)
        }


        search_cancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class RecyclerViewAdapter(check: Boolean, checkWord: String) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Post 클래스 ArrayList 생성성
        private var Post: ArrayList<BoardDTO> = arrayListOf()
        private var contentsUid = mutableListOf<String>()

        init {  // BoardDTO의 문서를 불러온 뒤 Post로 변환해 ArrayList에 담음
            if (check == true && checkWord == "null")
                checkIsTrue()
            else {
                checkIsFalse(checkWord)
            }

        }

        // xml파일을 inflate하여 ViewHolder를 생성
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        // onCreateViewHolder에서 만든 view와 실제 데이터를 연결
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = (holder as ViewHolder).itemView
            val boardUid = contentsUid[position]
            val currentUid = Post[position].uid

            viewHolder.search_title.text = Post[position].postTitle
            viewHolder.search_contents.text = Post[position].contents
            viewHolder.search_nickname.text = Post[position].nickname
            viewHolder.search_date.text = Post[position].Writed_date

            viewHolder.setOnClickListener {
                val intent = Intent(viewHolder.context, BoardDetail::class.java)
                intent.putExtra("contentsUid",boardUid)
                intent.putExtra("owneruid", currentUid)
                ContextCompat.startActivity(viewHolder.context, intent, null)
            }
        }

        // 리사이클러뷰의 아이템 총 개수 반환
        override fun getItemCount(): Int {
            return Post.size
        }


        fun search(searchWord: String, option: String) {
            firestore?.collection("Board")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    Post.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        if (snapshot.getString(option)!!.contains(searchWord)) {
                            val item = snapshot.toObject(BoardDTO::class.java)
                            Post.add(item!!)
                        }
                    }
                    notifyDataSetChanged()
                }
        }

        fun checkIsTrue() {
            firestore?.collection("Board")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    // ArrayList 비워줌
                    Post.clear()
                    contentsUid.clear()
                    for (snapshot in querySnapshot!!.documents) {
                        val item = snapshot.toObject(BoardDTO::class.java)
                        Post.add(item!!)
                        contentsUid.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }
        }

        fun checkIsFalse(tag: String) {
            firestore?.collection("Board")?.whereEqualTo("tag", tag)
                ?.addSnapshotListener { value, error ->
                    Post.clear()
                    contentsUid.clear()
                    for (snapshot in value!!.documents) {
                        val item = snapshot.toObject(BoardDTO::class.java)
                        Post.add(item!!)
                        contentsUid.add(snapshot.id)
                        Log.e("post", Post.toString())

                    }
                    notifyDataSetChanged()
                }
        }
    }
}





