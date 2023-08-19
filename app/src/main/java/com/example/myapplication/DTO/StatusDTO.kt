package com.example.myapplication.DTO

import java.sql.Timestamp

data class StatusDTO (
    var uid : String? = null,
    var profileUrl : String? = null,
    var nickname : String? = null,
    var status : MutableMap<String, Any> = HashMap(),
    var timestamp: Long? = null
)