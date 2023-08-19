package com.example.myapplication


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var  photoAdapter: PhotoAdapter
    private var dataList = mutableListOf<DataModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView.layoutManager = GridLayoutManager(applicationContext,4)
        photoAdapter = PhotoAdapter(applicationContext)
        recyclerView.adapter = photoAdapter



        dataList.add(DataModel("치킨","마싯겠당",R.drawable.chiken))
        dataList.add(DataModel("피자","히히",R.drawable.mama))
        dataList.add(DataModel("중식","호호",R.drawable.mama))
        dataList.add(DataModel("한식","하하",R.drawable.rice))
        dataList.add(DataModel("양식","뭐먹징",R.drawable.mama))
        dataList.add(DataModel("Title","Desc",R.drawable.mama))
        dataList.add(DataModel("Title","Desc",R.drawable.mama))


        photoAdapter.setDataList(dataList)

    }
}

