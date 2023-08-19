package com.example.myapplication.DTO

import java.sql.Timestamp

data class UserinfoDTO(
    var userEmail: String? = null,     // 아이디
    var UID: String? = null,        // Uid
    var userPassword: String? = null,  // 비밀번호
    var signUpdate: String? = null,    // 가입 날짜
    var phoneNumber: String? = null,    // 핸드폰 번호
    var name: String? = null,           // 이름
    var gender: String? = null,         // 성별
    var nickname: String? = null,       // 닉네임
    var ProfileUrl: String? = null,       // 프로필 사진 url
    var Profile_timestamp: Long? = null     // 언제 컨텐츠를 올렸는 관리

)