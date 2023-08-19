package com.example.myapplication.Login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.DTO.StatusDTO
import com.example.myapplication.KeyboardVisibilityUtils
import com.example.myapplication.Main.Activity.MainActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_login.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_item_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class Add_LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var PICK_IMAGE_FROM_ALBUM = 1
    private var photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
    private var storage: FirebaseStorage? = null
    private var firestore: FirebaseFirestore? = null
    private var gender: String? = null
    private var photoUri: Uri? = null
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils //키보드 움직이기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication.R.layout.activity_add_login)


        overridePendingTransition(R.anim.horizon_enter, R.anim.none)//애니메이션

        //fireStorage 초기화
        storage = FirebaseStorage.getInstance()
        //fireStore Database
        firestore = FirebaseFirestore.getInstance()
        //firebase Auth
        auth = FirebaseAuth.getInstance()

        //갤러리 오픈
        // ACTION_GET_CONTENT : YOU CAN CHOOSE SOMETHING BASED ON MIME type
        // or ACTION_PICK

        //라디오 버튼 값 설정
        add_login_sex_input_set.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                R.id.add_login_male_btn -> gender = "남"
                R.id.add_login_female_btn -> gender = "여"
            }
        }
        //이미지 업로드 이벤트 처리
        upload.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            photoPickerIntent.type = "image/*"
            photoPickerIntent.putExtra("crop", true)
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        }
        upload_sign.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                CoroutineScope(IO).launch{
                    contentUpload()
                }
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        photoUri = Uri.parse("android.resource://com.example.myapplication/ic_baseline_account_circle_24")
        when (requestCode) {
            PICK_IMAGE_FROM_ALBUM -> {
                data?.data?.let { uri ->
                    cropImage(uri) //이미지를 선택하면 여기가 실행됨
                }
                photoUri = data?.data
                Log.e("이미지 확인", photoUri.toString())
                upload_image.setImageURI(photoUri)
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                //그후, 이곳으로 들어와 RESULT_OK 상태라면 이미지 Uri를 결과 Uri로 저장!
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    result.uri?.let {
                        upload_image.setImageBitmap(result.bitmap)
                        upload_image.setImageURI(result.uri)
                        photoUri = result.uri
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    Toast.makeText(this@Add_LoginActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                finish()
            }

        }

    }

    //upload_image
    //좋은 코드는 아니지만 간소화 하는 방법을... 모색
    private suspend fun contentUpload() {
        //ProgressBar <<< 효과 넣을지 말지? 살짝 구시대적 ui
        val statusDTO = StatusDTO()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_.png"
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val NM = add_login_nickname_edit.text.toString()
        val profile_timestamp = System.currentTimeMillis()
        val name = add_login_edit.text.toString()
        if (photoUri != null) {
            // images/(imageFilename) 위치를 가리키는 참조 변수-> 를 putFile로 storage서버에 업로드
            val storageRef = storage?.reference?.child("Profiles")?.child(imageFileName)
            // storageRef?.putFile()의 반환값은 StorageTask
            storageRef?.putFile(photoUri!!)?.addOnSuccessListener { taskSnapshot ->
                //firebase Strage 서버에 저장된 파일 다운로드 URL 가져옴
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    var UR = uri.toString()
                    firestore?.collection("userid")?.document(uid)?.update(
                        mapOf(
                            "profileUrl" to UR,
                            "nickname" to NM,
                            "profile_timestamp" to profile_timestamp,
                            "name" to name,
                            "gender" to gender
                        )
                    )
                    setResult(RESULT_OK)
                    statusDTO.uid = uid
                    statusDTO.profileUrl = UR
                    statusDTO.nickname = NM
                    statusDTO.timestamp = profile_timestamp
                    FirebaseFirestore.getInstance().collection("Status").document(uid).set(statusDTO)
                }
            }
        } else {
            firestore?.collection("userid")?.document(uid)?.update(
                mapOf(
                    "nickname" to NM,
                    "profile_timestamp" to profile_timestamp,
                    "profileUrl" to "null",
                    "name" to name,
                    "gender" to gender
                )
            )
            setResult(RESULT_OK)
            statusDTO.uid = uid
            statusDTO.profileUrl = "null"
            statusDTO.nickname = NM
            statusDTO.timestamp = profile_timestamp
            FirebaseFirestore.getInstance().collection("Status").document(uid).set(statusDTO)
        }
        finish()

    }

    private fun cropImage(uri: Uri?) {
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            //사각형 모양으로 자른다
            .start(this)
    }
}

