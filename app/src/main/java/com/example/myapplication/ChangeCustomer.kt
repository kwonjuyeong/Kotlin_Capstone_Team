package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Main.Fragment.HomeFragment.HomeFragment
import kotlinx.android.synthetic.main.activity_customer_service.*

class ChangeCustomer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_service)


        overridePendingTransition(R.anim.horizon_enter, R.anim.none)//애니메이션

        back_button1.setOnClickListener {
           finish()


        }


    }
}