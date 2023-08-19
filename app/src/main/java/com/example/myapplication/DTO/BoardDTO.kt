package com.example.myapplication.DTO

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

data class BoardDTO(
    var contents: String? = null,
    var uid: String? = null,
    var postTitle: String? = null,
    var Writed_date: String? = null,
    var imageUrlWrite: String? = null,
    var timestamp: Long? = null,
    var imageWriteExplain: String? = null,
    var tag: String? = null,
    var nickname: String? = null,
    var ProfileUrl: String? = null,
    var likeCount: Int = 0,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var like: MutableMap<String, Boolean> = HashMap(),
    var gender: String? = null,
    var locationName: String? = null ////

) {
    data class Chat(
        var UID: String? = null,
        var userNickname: String? = null,
        var userprofile: String? = null,
        var message: String? = null,
        var date: String? = null,
        var readUser: Map<String, Any>? = null,
        var timestamp: Long? = null
    )

    companion object {
        @JvmStatic
        @BindingAdapter("imageMessage")
        public fun loadImage(imageView: ImageView, image: String?) {
            if (image != null) {
                Glide.with(imageView.context).load(image).into(imageView)
            }
        }

    }
}

