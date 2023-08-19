/*package com.example.myapplication

import android.content.Intent
import android.icu.util.TimeUnit
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_phone_auth.*
import javax.xml.datatype.DatatypeConstants.SECONDS
import com.example.myapplication.DTO.UserinfoDTO

class PhoneAuthActivity: AppCompatActivity() {

    var mAuth: FirebaseAuth ?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        mAuth = FirebaseAuth.getInstance()

        val sendVerificationCodeButton: Button = findViewById(R.id.sendVerificationCodeButton)
        val phoneNumberInput: EditText = findViewById(R.id.phoneNumberInput)

        sendVerificationCodeButton.setOnClickListener {
            val phoneNumber = phoneNumberInput.text.toString()
            sendVerificationCode(phoneNumber)
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack:PhoneAuthProvider.OnVerificationStateChangedCallbacks=
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                val intent = Intent(this@PhoneAuthActivity, CodeVerificationActivity::class.java)
                intent.putExtra("code", p0)
                intent.putExtra("phoneNumber", phoneNumberInput.text.toString())
                startActivity(intent)
            }
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            }
            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@PhoneAuthActivity,p0.message, Toast.LENGTH_LONG).show()
            }
        }

}

*/


