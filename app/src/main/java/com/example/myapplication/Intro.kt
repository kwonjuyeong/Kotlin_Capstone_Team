package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.Login.LoginActivity

class Intro : AppCompatActivity() {
    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        if(checkPermissions()) {
            val thread = Thread(Runnable {
                Thread.sleep(3000)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }).start()
        }
        else{
            val thread = Thread(Runnable {
                Thread.sleep(3000)
                val intent = Intent(this, PermissionActivity::class.java)
                startActivity(intent)
                finish()
            }).start()
        }
    }
    //퍼미션 체크 및 권한 요청 함수
    private fun checkPermissions() :Boolean {
        //거절되었거나 아직 수락하지 않은 권한(퍼미션)을 저장할 문자열 배열 리스트
        var rejectedPermissionList = ArrayList<String>()

        //필요한 퍼미션들을 하나씩 끄집어내서 현재 권한을 받았는지 체크
        for(permission in requiredPermissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
              return false
            }
        }
        return true
    }

}