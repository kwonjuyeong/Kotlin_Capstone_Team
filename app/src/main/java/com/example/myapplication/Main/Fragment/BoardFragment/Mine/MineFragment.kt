package com.example.myapplication.Main.Fragment.BoardFragment.Mine

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Main.Fragment.BoardFragment.BoardFragment
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_recent.*

class MineFragment : Fragment() {
    private var repo: Repo
    private lateinit var mineAdapter : MineAdapter

    companion object {
        fun newInstance(): MineFragment = MineFragment()
    }

    init {
        repo = Repo.StaticFunction.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mineAdapter = MineAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_mine, container, false)
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mine_recyclerview.apply{
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mineAdapter
            mine_swip.setOnRefreshListener {
                mineAdapter.clear()
                mineAdapter.notifyDataSetChanged() // 새로고침 하고
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