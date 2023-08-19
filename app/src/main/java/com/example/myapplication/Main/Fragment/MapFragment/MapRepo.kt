package com.example.myapplication.Main.Fragment.MapFragment

import android.util.Log
import com.example.myapplication.DTO.BoardDTO
import com.google.firebase.firestore.FirebaseFirestore

class MapRepo {
    private var savedMapdata = mutableListOf<BoardDTO>()
    private var firestore = FirebaseFirestore.getInstance()
    private var mapIntentUid = mutableListOf<String>()


    object StaticFunction {
        private var instance: MapRepo? = null


        fun getInstance(): MapRepo {
            if (instance == null)
                instance = MapRepo()
            return instance!!
        }
    }

    // 메인에서 실행
    fun LoadLocation() {
        firestore.collection("Board").orderBy("timestamp")
            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot.documents) {
                    val item = snapshot.toObject(BoardDTO::class.java)
                    savedMapdata.add(item!!)
                    mapIntentUid.add(snapshot.id)
                }

                /*getgender(savedLocation)*/

            }
    }
    fun returnMapdata() : MutableList<BoardDTO>{
        return savedMapdata
    }
    fun returnIntentUid() : MutableList<String>{
        return  mapIntentUid
    }
}
