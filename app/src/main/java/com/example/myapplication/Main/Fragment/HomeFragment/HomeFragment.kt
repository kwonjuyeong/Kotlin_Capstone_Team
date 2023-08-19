package com.example.myapplication.Main.Fragment.HomeFragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.DTO.UserinfoDTO
import com.example.myapplication.Main.Board.BoardPost
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo
import com.example.myapplication.Main.Fragment.Search.SearchFragment
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.frag_home.*
import kotlinx.android.synthetic.main.frag_home.view.*


// 호출시 HomeFragment.newInstance() 를 이용해서 외부에서 호출
class HomeFragment : Fragment() {
    companion object {
        const val TAG: String = "로그"

        // 외부 호출시 메모리에 적제된 HomeFragment를 불러올수 있게함
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private val repo = Repo.StaticFunction.getInstance()
    private lateinit var photoAdapter: PhotoAdapter
    private var dataList = mutableListOf<DataModel>()
    private var userinfoDTO = UserinfoDTO()

    // 메모리에 적제 되었을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userinfoDTO = repo.returnUserInfo()

    }


    // Activity 안에 Fragment가 들어가게 되는데, onAttach가 Fragment와 Activity에 붙게됨(의존)
// 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    // 뷰가 생성되었을때
// 프레그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.frag_home, container, false)
        //  inflater 레이아웃과 frag를 연결해줌

        view.btn_board.setOnClickListener {
            GoBorad()
        }


        return view
    }

    fun GoBorad() {
        var intent = Intent(requireActivity(), BoardPost::class.java)
        startActivity(intent)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun startActivity(activity: Activity) {
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)//위 아래 애니메이션코드
        }

        val fragmentTransaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.horizon_enter, R.anim.none)//위아래 애니메이션(62~63)

        main_search_view.setOnClickListener{
            val intent = Intent(requireContext(), SearchFragment::class.java)
            ContextCompat.startActivity(requireContext(),intent,null)
        }


        recyclerView.layoutManager = GridLayoutManager(requireContext(), 5)
        photoAdapter = PhotoAdapter(requireContext())
        recyclerView.adapter = photoAdapter

        dataList.add(DataModel("치킨", R.drawable.chiken))
        dataList.add(DataModel("피자", R.drawable.pizza))
        dataList.add(DataModel("떡볶이", R.drawable.ddukbokki))
        dataList.add(DataModel("파스타", R.drawable.pasta))
        dataList.add(DataModel("돈까스", R.drawable.gas))
        dataList.add(DataModel("초밥", R.drawable.sushi))
        dataList.add(DataModel("카페", R.drawable.coffeee))
        dataList.add(DataModel("햄버거", R.drawable.hamberger))
        dataList.add(DataModel("중국집", R.drawable.jjajang))
        dataList.add(DataModel("스테이크", R.drawable.steak))
        dataList.add(DataModel("고기", R.drawable.samgupsal))
        dataList.add(DataModel("곱창", R.drawable.gopchang))
        dataList.add(DataModel("육회", R.drawable.yuksushi))
        dataList.add(DataModel("보쌈", R.drawable.bossam))
        dataList.add(DataModel("닭발", R.drawable.chickenfoot))
        dataList.add(DataModel("카레", R.drawable.curry))
        dataList.add(DataModel("브리또", R.drawable.buritto))
        dataList.add(DataModel("도시락", R.drawable.dosirak))
        dataList.add(DataModel("국밥", R.drawable.gookbap))
        dataList.add(DataModel("찌개", R.drawable.gimchijjigae))
        dataList.add(DataModel("덮밥", R.drawable.dupbap))
        dataList.add(DataModel("생선 구이", R.drawable.sangsun92))
        dataList.add(DataModel("냉면", R.drawable.yesmyen))
        dataList.add(DataModel("마라탕", R.drawable.maratang))
        dataList.add(DataModel("샌드위치", R.drawable.sandwich))
        dataList.add(DataModel("라멘", R.drawable.ramyen))
        dataList.add(DataModel("찜닭", R.drawable.jjimdak))



        photoAdapter.setDataList(dataList)

    }

    override fun onPause() {
        super.onPause()
        repo.upDateOnlineState("offline")

    }

    override fun onResume() {
        super.onResume()
        repo.upDateOnlineState("online")
        if (userinfoDTO.ProfileUrl != null) {
            Glide.with(this).load(userinfoDTO.ProfileUrl).into(main_page_user_img)
        } else {
            main_page_user_img.setImageResource(R.drawable.ic_baseline_account_circle_signiture)
        }

    }
}