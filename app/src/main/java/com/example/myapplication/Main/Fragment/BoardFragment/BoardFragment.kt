package com.example.myapplication.Main.Fragment.BoardFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo
import com.example.myapplication.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BoardFragment : Fragment() {
    companion object {
        const val TAG : String = "BoardList"

        fun newInstance(): BoardFragment {

            return BoardFragment()
        }
    }
    private val repo = Repo.StaticFunction.getInstance()
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.frag_board, container, false)

        tabLayout = view.findViewById(R.id.boardTabLayout)
        viewPager = view.findViewById(R.id.search_view_pager)


        val adapter = SearchFragViewPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.requestDisallowInterceptTouchEvent(true)
        val tabName = arrayOf<String>("최근 게시물", "내가 쓴 글")

        //슬라이드로 이동했을 때, 탭이 같이 변경되도록
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabName[position].toString()

        }.attach()

        //탭이 선택되었을 때, 뷰페이저가 같이 변경되도록
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        return view
    }
    override fun onPause() {
        super.onPause()
        repo.upDateOnlineState("offline")

    }

    override fun onResume() {
        super.onResume()
        repo.upDateOnlineState("online")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {  //79~84 추가 애니메이션코드
        super.onViewCreated(view, savedInstanceState)

        val fragmentTransaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.horizon_enter, R.anim.none)//위아래 애니메이션(62~63)
    }


}