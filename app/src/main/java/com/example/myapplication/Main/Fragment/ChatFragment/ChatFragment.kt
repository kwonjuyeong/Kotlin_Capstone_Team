package com.example.myapplication.Main.Fragment.ChatFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.repo.Repo
import com.example.myapplication.R
import kotlinx.android.synthetic.main.frag_chat.*

class ChatFragment: Fragment(){
    companion object {
        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }
    private var repo = Repo.StaticFunction.getInstance()
    private lateinit var chatadapter :  ChatListAdapter
    private val viewModel by lazy{
        ViewModelProvider(this).get(ChatListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatadapter = ChatListAdapter(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.frag_chat,container,false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val fragmentTransaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.horizon_enter, R.anim.none)//위아래 애니메이션(62~63)

        chat_list_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            //val chatlistAdapter = ChatListAdapter()
            adapter = chatadapter
            observeData()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData(){
        viewModel.getChatListData().observe(viewLifecycleOwner, Observer {

            chatadapter.setDataChatAapter(it)
            chatadapter.notifyDataSetChanged()
        })
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


