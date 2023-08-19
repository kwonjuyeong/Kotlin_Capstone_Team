package com.example.myapplication.Main.Fragment.BoardFragment.Recent

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_recent.*

class RecentFragment : Fragment(){
    companion object {
        fun newInstance() : RecentFragment = RecentFragment()
    }
    private var boardListAdapter = BoardListAdapter()
    private var repo : Repo
    private lateinit var viewPager2: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    init {
        repo = Repo.StaticFunction.getInstance()
    }
    private fun getfoodlList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.bannerpic,R.drawable.bannerpic1,R.drawable.bannerpic2,R.drawable.bannerpic3,R.drawable.bannerpic4,R.drawable.bannerpic5,R.drawable.bannerpic6)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPagerAdapter = ViewPagerAdapter(requireContext(),getfoodlList())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recent,container,false)
        viewPager2 = view.findViewById(R.id.viewPager_food)
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager2.adapter = viewPagerAdapter
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
        viewPager2.requestDisallowInterceptTouchEvent(false)



        board_fragement_recycler_view.apply {
            var boardlistadapter: BoardListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            boardlistadapter = BoardListAdapter()
            adapter = boardlistadapter
            boradSwiprefresh.setOnRefreshListener {
                //notices.clear() // 리스트를 한 번 비워주고
                //crawler.activateBot(page) // 리스트에 값을 넣어주고
                boardListAdapter.clear()
                boardListAdapter.notifyDataSetChanged() // 새로고침 하고
                boradSwiprefresh.isRefreshing = false // 새로고침을 완료하면 아이콘을 없앤다.
            }
        }
    }
    override fun onPause() {
        super.onPause()
        repo.upDateOnlineState("offline")

    }

    override fun onResume() {
        super.onResume()
        repo.upDateOnlineState("online")

    }
}