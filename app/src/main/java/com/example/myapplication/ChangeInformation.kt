package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_change_information.*

class ChangeInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_information)
        overridePendingTransition(R.anim.horizon_enter, R.anim.none)//애니메이션

        board_cancel.setOnClickListener {
            finish()


        }


    }
}