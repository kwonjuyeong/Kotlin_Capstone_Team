package com.example.myapplication.Main.Activity


//import com.example.myapplication.Main.Fragment.ChatFragment.ChatFragment
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.DTO.StatusDTO
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.Main.Fragment.BoardFragment.BoardFragment
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo
import com.example.myapplication.Main.Fragment.ChatFragment.ChatFragment
import com.example.myapplication.Main.Fragment.ChatFragment.ChatRepo
import com.example.myapplication.Main.Fragment.HomeFragment.HomeFragment
import com.example.myapplication.Main.Fragment.MapFragment.MapFragment
import com.example.myapplication.Main.Fragment.MapFragment.MapRepo
import com.example.myapplication.Main.Fragment.Search.SearchFragment
import com.example.myapplication.R
import com.example.myapplication.SettingFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.frag_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    // MainActivity가 가지고 있는 멤버 변수 선언
    private lateinit var homeFragment: HomeFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var boardFragment: BoardFragment
    private lateinit var chatFragment: ChatFragment
    private lateinit var settingFragment: SettingFragment
    private var repo = Repo.StaticFunction.getInstance()
    private var maprepo = MapRepo.StaticFunction.getInstance()
    private var chatRepo = ChatRepo.StaticFunction.getInstance()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var longitude: Double? = null
    private var latitude: Double? = null
    private var locationName : String? = null
    private var backKeyPressedTime = 0L

    init {
        Log.e("MAIM 엑티비티 실행", "init")
    }

    companion object {
        const val TAG: String = "로그"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        overridePendingTransition(R.anim.horizon_enter, R.anim.none)
        bottomNavi.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)
        // repo.createOnlinstatus()
        Log.e("메인 엑티비티 실행 현황", "onCreate:TODO : 데이터 확인할때 확인작업")
        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.frame_container, homeFragment).commit()
        // searchview 클릭 리스너 넣기

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    latitude = location!!.latitude
                    longitude = location!!.longitude
                    locationName = getCityName1(latitude!!,longitude!!)////
                    Log.e(TAG, latitude.toString())
                    Log.e(TAG, longitude.toString())
                    Log.e(TAG, locationName.toString())
                }
        }

    }


    override fun onPause() {
        super.onPause()
        Log.e("메인 엑티비티 실행 현황", "onPause: TODO : 데이터 확인할때 확인작업")
        repo.upDateOnlineState("offline")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("메인 엑티비티 실행 현황", "onRestart: 데이터 확인할때 확인 작업")

    }

    override fun onResume() {
        super.onResume()
        Log.e("메인 엑티비티 실행 현황", "onResume: 데이터 확인할때 확인 작업")
        repo.upDateOnlineState("online")
        repo.getUserInfo()
        repo.getboarddata()
        repo.getboardUid()
        maprepo.LoadLocation()
        chatRepo.CheckChattingRoom()
        chatRepo.getUserStatus()

//        lifecycleScope.launch(Dispatchers.IO) {
//            repo.getUserInfo()
//            repo.getboarddata()
//            repo.getboardUid()
//            maprepo.LoadLocation()
//            chatRepo.CheckChattingRoom()
//        }
    }


    //바텀 네비게이션 아이템 클릭 리스너

    private val onBottomNavItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.action_home -> {
                    overridePendingTransition(R.anim.horizon_enter, R.anim.none)//애니메이션
                    homeFragment = HomeFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, homeFragment).commit()
                }
                R.id.action_Map -> {
                    overridePendingTransition(R.anim.horizon_enter, R.anim.none)//애니메이션
                    mapFragment = MapFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, mapFragment).commit()
                }
                R.id.action_board -> {
                    overridePendingTransition(R.anim.horizon_enter, R.anim.none)//애니메이션
                    //repo.getboarddata()
                    //repo.getboardUid()
                    // 사진을 가져올 수 있는지 확인 하는 작업
                    boardFragment = BoardFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, boardFragment).commit()
                }
                R.id.action_chat -> {
                    overridePendingTransition(R.anim.horizon_enter, R.anim.none)//애니메이션
                    chatFragment = ChatFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, chatFragment).commit()
                }
                R.id.action_setting -> {
                    overridePendingTransition(R.anim.horizon_enter, R.anim.none)//애니메이션
                    settingFragment = SettingFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_container, settingFragment).commit()
                }
            }
            true
        }
    private fun getCityName1(lat: Double, long: Double): String {
        var cityName: String = ""
        var doName: String = ""

        val geoCoder = Geocoder(this, Locale.getDefault())
        val Adress = geoCoder.getFromLocation(lat, long, 3)

        cityName = Adress.get(0).locality
        doName = Adress.get(0).thoroughfare


        //Toast.makeText(context, cityName + " " + doName + " " + jibunName, Toast.LENGTH_LONG).show()
        return "$cityName, $doName"
    }
    interface OnBackPressedListener{
        fun onBack()
    }

    override fun onBackPressed() {
        //super.onBackPressed() // 이걸 없애면 뒤로가기 눌러도 Activity가 finish()되지 않음

        if(mBackListener != null) {
            mBackListener?.onBack()
            mBackListener = null
        } else
            backToFinish()

        //main_toolbar_write_btn.visibility = View.VISIBLE
        //main_toolbar_back_btn.visibility = View.GONE
    }


    fun backToFinish(){
        if(System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "종료하려면 한 번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
    }
    private var mBackListener: OnBackPressedListener? = null

    fun setOnBackPressedListener(listener: OnBackPressedListener){
        mBackListener = listener
    }
}