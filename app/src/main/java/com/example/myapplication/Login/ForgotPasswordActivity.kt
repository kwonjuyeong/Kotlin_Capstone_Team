package com.example.myapplication.Login

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgotpassword.*

class ForgotPasswordActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)
        overridePendingTransition(R.anim.horizon_enter, R.anim.none)//애니메이션

        reset_password_btn.setOnClickListener {

            val email: String = reset_password_edit.text.toString().trim{it <= ' ' }
            if(email.isEmpty()) {
                Toast.makeText(this@ForgotPasswordActivity,
                    "이메일을 입력해주세요!",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        Toast.makeText(this@ForgotPasswordActivity,
                            "이메일이 전송되었습니다. 확인해주세요!",
                            Toast.LENGTH_LONG)
                            .show()
                        finish()
                    }else{
                        Toast.makeText(this@ForgotPasswordActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }
}