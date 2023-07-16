package com.noobcode.gamebuddy.home

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.noobcode.gamebuddy.viewmodels.ChatActivityViewModel
import com.noobcode.gamebuddy.adapters.ChatAdapter
import com.noobcode.gamebuddy.utils.FirebaseObject
import com.noobcode.gamebuddy.utils.ObjectModels
import com.noobcode.gamebuddy.utils.ObjectModels.avatarForUsers
import com.noobcode.gamebuddy.utils.Utils
import com.noobcode.gamebuddy.databinding.ActivityChatBinding
import com.vanniktech.emoji.EmojiPopup

class ChatActivity : AppCompatActivity() {

    private lateinit var viewmodel: ChatActivityViewModel
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    private var userID: String? = ""
    private lateinit var emojiPopup: EmojiPopup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val viewModel: ChatActivityViewModel by viewModels()
        viewmodel = viewModel
        setContentView(binding.root)

        emojiPopup = EmojiPopup.Builder
            .fromRootView(binding.root).build(binding.messageText)

        val bundle = this.intent.extras
        if (bundle != null) {
            userID = bundle.getString("userID")
            loadUI()
        } else {
            //chats fails to load, user id is not available anymore
            updateUI(false)
        }

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ChatAdapter(mutableListOf())

        binding.chatRecyclerView.adapter = adapter

        binding.sendMessageBtn.setOnClickListener {
            userID?.let {
                if(!binding.messageText.text.isNullOrEmpty()){
                    val message = ObjectModels.ChatData()
                    message.message = binding.messageText.text.toString()
                    message.sender = FirebaseObject.currentUser.uid
                    message.seenStatus = false
                    message.timestamp = System.currentTimeMillis()
                    //send locally to self
                    adapter.addMessage(message)
                    binding.messageText.setText("")
                    //send to db
                    viewmodel.sendMessage(
                        Utils.getAStringFrom2Users(
                            FirebaseObject.currentUser.uid,
                            it
                        ), message)
                    scrollToBottom()
                }
            }
        }

        binding.emoticonsButton.setOnClickListener {
            emojiPopup.toggle()
        }

        userID?.let {
            viewmodel.listenToChatChanges(Utils.getAStringFrom2Users(
                FirebaseObject.currentUser.uid,
                it
            ), FirebaseObject.currentUser.uid).observe(this@ChatActivity, Observer {message ->
                if(message != null){
                    adapter.addMessage(message)
                }
            })
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.avatarImage.setOnClickListener {
            finish()
        }
    }

    private fun scrollToBottom() {
        if(adapter.itemCount > 0){
            binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount-1)
        }
    }

    private fun loadUI() {
        binding.avatarImage.setImageDrawable(resources.getDrawable(avatarForUsers[Utils.getRandomNumber(
            0,
            avatarForUsers.size - 1
        )]))
        binding.useridChatText.text = "gb/${userID}"
    }

    private fun updateUI(haveChats: Boolean) {
        //show or hide image for no chats

    }

    override fun onResume() {
        super.onResume()
        //online status update

        //load initial messages
        userID?.let{ userid ->
            viewmodel.loadAllChatMessages(
                Utils.getAStringFrom2Users(
                    FirebaseObject.currentUser.uid,
                    userid
                )
            ).observe(this, Observer {

                if(it.isNullOrEmpty()){
                    updateUI(false)
                }
                else{
                    updateUI(true)
                    adapter.updateList(it)
                    scrollToBottom()
                    //listen to updates now
//                    viewmodel.listenToChatChanges(Utils.getAStringFrom2Users(FirebaseObject.currentUser.uid, userid), FirebaseObject.currentUser.uid).observe(this, Observer { message ->
//                        if(message == null){
//                            //show null message -> couldn't load message
//
//                        }else{
//                            adapter.addMessage(message)
//                        }
//                    })
                }

            })
        }
    }

}