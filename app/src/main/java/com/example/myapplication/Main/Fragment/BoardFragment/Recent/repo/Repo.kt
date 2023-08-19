package com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.DTO.MessageDTO
import com.example.myapplication.DTO.StatusDTO
import com.example.myapplication.DTO.UserinfoDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.frag_board.*

class Repo {
    // 초기값 백업1
    private var firestore = FirebaseFirestore.getInstance()
    private var listdata = mutableListOf<BoardDTO>()
    private var contentsuid = arrayListOf<String>()
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid
    private var userinfoDTO = UserinfoDTO()
    private var photoString = UserinfoDTO()


    object StaticFunction {
        private var instance: Repo? = null
        fun getInstance(): Repo {
            if (instance == null)
                instance = Repo()
            return instance!!
        }
    }

    init {
        listdata.clear()
        contentsuid.clear()
        firestore.collection("Board").orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot == null) return@addSnapshotListener
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(BoardDTO::class.java)
                    listdata.add(item!!)
                    contentsuid.add(snapshot.id)
                }
            }

    }

    fun getboarddata(): MutableList<BoardDTO> {
//        firestore.collection("Board").orderBy("timestamp")
//            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
//                if (querySnapshot == null) return@addSnapshotListener
//                for (snapshot in querySnapshot!!.documents) {
//                    var item = snapshot.toObject(BoardDTO::class.java)
//                    listdata.add(item!!)
        Log.e("Repo listdata ", listdata.size.toString())
        return listdata
    }

    fun getboardUid(): ArrayList<String> {
//        firestore.collection("Board").orderBy("timestamp")
//            .addSnapshotListener() { querySnapshot, firebaseFirestoreException ->
//                if (querySnapshot == null) return@addSnapshotListener
//                for (snapshot in querySnapshot!!.documents) {
//                    contentsuid.add(snapshot.id)
//                }
//            }
        return contentsuid
    }
    fun upDateOnlineState(status: String) {
        val uidCollection = uid + "_status"
        val statusDTO = StatusDTO()
        statusDTO.status["Online"] = status
        val firestoreRef = firestore.collection("Status").document(uid)
        firestoreRef.update(
            mapOf(
                "status" to statusDTO.status
            )
        )
    }
    fun getUserInfo(){
        var Uef = firestore.collection("userid").document(uid)
        Uef.addSnapshotListener { value, error ->
            userinfoDTO = value?.toObject(UserinfoDTO::class.java)!!
        }
    }
    fun returnUserInfo() : UserinfoDTO{
        return userinfoDTO
    }



}







