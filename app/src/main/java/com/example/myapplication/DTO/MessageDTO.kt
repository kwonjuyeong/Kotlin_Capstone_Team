package com.example.myapplication.DTO

data class MessageDTO (
    var boardUid : String? = null,
    var UserCheck : MutableMap<String, Boolean> = HashMap(),
    var OwnerUid : String? = null,
    var currentUID : String? = null,
    var timeStamp: Long? = null
) {
    data class lastMessage(
        var boardChatuid : String? = null,
        var senderuid : String? = null,
        var lastContent : String? = null,
        var time : String? = null,
        var profileUrl : String? = null,
        var nickname: String? = null,
        var timeStamp : Long? = null
    )
}