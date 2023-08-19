 package com.example.myapplication.Main.Fragment.MapFragment


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.DTO.BoardDTO
import com.example.myapplication.Main.Board.Detail.BoardDetail
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo
import com.example.myapplication.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_board_detail.*
import kotlinx.android.synthetic.main.activity_board_post.*
import kotlinx.android.synthetic.main.activity_now_my_place.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback {
    private var client: FusedLocationProviderClient? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID = 1010
    private lateinit var mView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var auth: FirebaseAuth
    var firestore: FirebaseFirestore? = null
    var storage: FirebaseStorage? = null
    private var maprepo = MapRepo.StaticFunction.getInstance()
    private var count = 0
    private var repo = Repo.StaticFunction.getInstance()


    companion object {
        const val TAG: String = "로그"

        // 외부 호출시 메모리에 적제된 HomeFragment를 불러올수 있게함
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()


        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val main_view = inflater.inflate(R.layout.activity_now_my_place, container, false)

        mView = main_view.findViewById(R.id.realtime_map) as MapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)
        main_view.findViewById<CardView>(R.id.card_view).visibility = View.GONE

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        return main_view
    }


    fun CheckPermission(): Boolean {
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if (
            ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    fun isLocationEnabled(): Boolean {
        var locationManager =
            requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (CheckPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        NewLocationData()
                    } else {
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please Turn on Your device Location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {

        }
    }


    private fun getCityName(lat: Double, long: Double): String {
        var cityName: String = ""
        var doName: String = ""
        var jibunName: String = ""

        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
        val Adress = geoCoder.getFromLocation(lat, long, 3)

        cityName = Adress.get(0).locality
        doName = Adress.get(0).thoroughfare
        jibunName = Adress.get(0).featureName

        //Toast.makeText(context, cityName + " " + doName + " " + jibunName, Toast.LENGTH_LONG).show()
        return "$cityName, $doName, $jibunName"
    }

    @SuppressLint("MissingPermission")
    fun NewLocationData() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("Debug:", "You have the Permission")
            }
        }
    }

    private fun getBitmap(url: String): Bitmap? {

        try {
            val url = URL(url)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)
            val image = Bitmap.createScaledBitmap(bitmap, 80, 80, true)
            return image
        } catch (e: IOException) {
        }
        return null
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }


    //다른 사용자 마커 찍는 함수 with courutine
    private fun otherUserMaker(googleMap: GoogleMap) {
        lifecycleScope.launch(Dispatchers.IO) {
            var mapUserData = mutableListOf<BoardDTO>()
            var intentUID = mutableListOf<String>()
            mapUserData = maprepo.returnMapdata()
            intentUID = maprepo.returnIntentUid()

            for (i in mapUserData) {
                val bitmap1 = getBitmap(i.ProfileUrl.toString())
                if(bitmap1==null){
                    lifecycleScope.launch(Dispatchers.Main) {

                        val makerOptions = MarkerOptions()
                        makerOptions
                            .position(LatLng(i.latitude!!, i.longitude!!))
                            .title(i.nickname)
                            .snippet(i.postTitle)
                            .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.noimage))

                        val marker1: Marker = googleMap.addMarker(makerOptions)!!
                        marker1.tag =
                            i.Writed_date + "/" + i.contents + "/" + i.gender + "/" + i.locationName
                        Log.e("null일때 ", marker1.tag.toString() )
                    }
                }else{
                    lifecycleScope.launch(Dispatchers.Main) {

                        val makerOptions = MarkerOptions()
                        makerOptions
                            .position(LatLng(i.latitude!!, i.longitude!!))
                            .title(i.nickname)
                            .snippet(i.postTitle)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap1))

                        val marker1: Marker = googleMap.addMarker(makerOptions)!!
                        marker1.tag =
                            i.Writed_date + "/" + i.contents + "/" + i.gender + "/" + i.locationName
                      }
                }
                lifecycleScope.launch(Dispatchers.Main){
                googleMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
                    override fun onMarkerClick(marker1: Marker): Boolean {
                        card_view.visibility = View.VISIBLE
                        val arr = marker1.tag.toString().split("/")
                        board_nickname.text = marker1.title
                        board_title.text = marker1.snippet
                        board_time.text = arr[0]
                        board_contents.text = arr[1]
                        board_gender.text = arr[2]
                        board_locate.text = arr[3] ////
                        MapIntentUID.text = intentUID[count]
                        ownerUID.text = i.uid

                        board_move_button.setOnClickListener {
                            //여기다가 아이디태그 달아서 버튼누르면 화면이동.
                            var intent = Intent(requireActivity(), BoardDetail::class.java)
                            intent.putExtra("contentsUid", MapIntentUID.text.toString())
                            intent.putExtra("owneruid", ownerUID.text.toString())
                            startActivity(intent)
                        }
                        count++
                        return false
                    }
                })
                googleMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
                    override fun onMapClick(p0: LatLng) {
                        card_view.visibility = View.GONE
                    }
                })}

            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val myLocation = location?.let { LatLng(it.latitude, it.longitude) }

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation!!))
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                //현재위치 최신화 버튼을 누르면 현재 위치가 뜸
                recent_button.setOnClickListener {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                    googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
                    val marker = MarkerOptions()
                        .position(myLocation)
                        .title("현재 위치 :")
                        .snippet(location?.let { it1 -> getCityName(it1.latitude, it1.longitude) })

                    val marker2: Marker = googleMap.addMarker(marker)!!
                    marker2.tag = null

                    googleMap.setOnMarkerClickListener { marker2 ->
                        Toast.makeText(context, marker2.snippet!! + "에요!!!", Toast.LENGTH_LONG)
                            .show() ////
                        false
                    }

                    //googleMap.addMarker(marker)
                    otherUserMaker(googleMap)
                }

            }
    }


    override fun onStart() {
        super.onStart()
        mView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mView.onStop()
    }

    override fun onResume() {
        super.onResume()
        repo.upDateOnlineState("online")
        mView.onResume()
    }

    override fun onPause() {
        super.onPause()
        repo.upDateOnlineState("offline")
        mView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }

    override fun onDestroy() {
        mView.onDestroy()
        super.onDestroy()
    }
}
