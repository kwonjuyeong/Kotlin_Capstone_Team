package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication.Login.LoginActivity
import kotlinx.android.synthetic.main.activity_customer_service.*
import kotlinx.android.synthetic.main.activity_infomation_law.*
import kotlinx.android.synthetic.main.frag_setting.*

//import com.google.api.LogDescriptor

// 호출시 HomeFragment.newInstance() 를 이용해서 외부에서 호출
class SettingFragment : Fragment(){
    companion object{
        const val TAG : String ="로그"

        // 외부 호출시 메모리에 적제된 HomeFragment를 불러올수 있게함
        fun newInstance() : SettingFragment {
            return SettingFragment()
        }
    }

    // 메모리에 적제 되었을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"SettingFragment - onCreate() called")



    }
    // Activity 안에 Fragment가 들어가게 되는데, onAttach가 Fragment와 Activity에 붙게됨(의존)
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "SettingFragment - onAttach: onAttach called")
    }
    // 뷰가 생성되었을때
    // 프레그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        Log.d(TAG, "SettingFragment - onCreateView: called")
        //  inflater 레이아웃과 frag를 연결해줌
        val view = inflater.inflate(R.layout.frag_setting, container, false)


        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentTransaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.horizon_enter, R.anim.none)//위아래 애니메이션(62~63)



        button_expire.setOnClickListener {
            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        button_changeInfo.setOnClickListener {
            activity?.let {
                val intent = Intent(context, ChangeInformationLaw::class.java)
                startActivity(intent)
            }
        }
        button_logout.setOnClickListener {
            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        button_service.setOnClickListener {
            activity?.let {
                val intent = Intent(context, ChangeCustomer::class.java)
                startActivity(intent)
            }
        }

        button_changeInfo.setOnClickListener {
            activity?.let{
                val intent = Intent(context, ChangeInformation::class.java)
                startActivity(intent)
            }








        }


        // 여기다가 view 구현하는거 정의 하면 됨 딴거 다 쓰잘때기 없음
    }
}