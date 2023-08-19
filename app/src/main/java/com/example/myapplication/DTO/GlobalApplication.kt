package com.example.myapplication.DTO
import android.app.Application
import com.example.myapplication.R
import com.kakao.sdk.common.KakaoSdk
// 카카오 API 초기화
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}