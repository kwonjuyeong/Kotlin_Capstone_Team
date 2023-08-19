package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_infomation_law.*

class ChangeInformationLaw : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infomation_law)
        overridePendingTransition(R.anim.horizon_enter, R.anim.none)//애니메이션


        back_button3.setOnClickListener({
            val intent = Intent(this, SettingFragment::class.java)
            startActivity(intent)


        })
    }
}