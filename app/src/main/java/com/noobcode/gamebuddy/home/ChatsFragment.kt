package com.noobcode.gamebuddy.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.noobcode.gamebuddy.CustomApplication
import com.noobcode.gamebuddy.adapters.MatchAdapter
import com.noobcode.gamebuddy.databinding.FragmentChatsBinding
import com.noobcode.gamebuddy.interfaces.MatchCallBack
import com.noobcode.gamebuddy.utils.FirebaseObject
import com.noobcode.gamebuddy.utils.ObjectModels
import com.noobcode.gamebuddy.viewmodels.ChatActivityViewModel

class ChatsFragment : Fragment(), MatchCallBack {

    lateinit var binding: FragmentChatsBinding
    private lateinit var viewmodel: ChatActivityViewModel
    private lateinit var matchAdapter: MatchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(layoutInflater)
        FirebaseObject.fireStore = Firebase.firestore
        val viewModel: ChatActivityViewModel by viewModels()
        viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matchAdapter = MatchAdapter(mutableListOf(), this)

        binding.matchRecyclerView.adapter = matchAdapter
        binding.matchRecyclerView.layoutManager = LinearLayoutManager(CustomApplication.ApplicationContext)

    }

    override fun onResume() {
        super.onResume()

        showLoading()
        //load all chats
        loadChats()

    }

    private fun showLoading() {
        binding.fabProgress.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.fabProgress.visibility = View.GONE
    }

    private fun loadChats() {
        //show loading image
        viewmodel.loadAllMessages(FirebaseObject.currentUser.uid).observe(viewLifecycleOwner, Observer { chats ->
            if(chats == null){
                hideLoading()
                showEmptyChats()
            }else{
                if(chats.size > 0){
                    //not empty, load chats
                    hideLoading()
                    matchAdapter.updateList(chats)
                }else{
                    hideLoading()
                    showEmptyChats()
                }
            }
        })
    }

    private fun showEmptyChats() {
        var match = ObjectModels.Chat()
        match.userID1 = FirebaseObject.currentUser.uid
        match.userID2 = "No Matches, Nothing to show here..."
        matchAdapter.addMatch(match)
    }

    override fun matchClicked(userID: String) {
        if(userID != "No Matches, Nothing to show here..."){
            var chatIntent = Intent(CustomApplication.ApplicationContext, ChatActivity::class.java)
            var chatData = Bundle()
            chatData.putString("userID", userID)
            chatIntent.putExtras(chatData)
            startActivity(chatIntent)
        }
    }


}