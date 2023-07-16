package com.noobcode.gamebuddy.home

import android.os.Bundle
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

class ChatActivity : AppCompatActivity() {

    private lateinit var viewmodel: ChatActivityViewModel
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    private var userID: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val viewModel: ChatActivityViewModel by viewModels()
        viewmodel = viewModel
        setContentView(binding.root)

        //show loading chats ui
        loadChats()

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

        binding.textSendBtn.setOnClickListener {
            userID?.let {
                if(!binding.textEditext.text.isNullOrEmpty()){
                    val message = ObjectModels.ChatData()
                    message.message = binding.textEditext.text.toString()
                    message.sender = FirebaseObject.currentUser.uid
                    message.seenStatus = false
                    message.timestamp = System.currentTimeMillis()
                    viewmodel.sendMessage(
                        Utils.getAStringFrom2Users(
                            FirebaseObject.currentUser.uid,
                            it
                        ), message)
                }
            }

        }
    }

    private fun loadUI() {
        binding.avatarImage.setImageDrawable(resources.getDrawable(avatarForUsers[Utils.getRandomNumber(
            0,
            avatarForUsers.size - 1
        )]))
        binding.useridChatText.text = "gb/${userID}"
    }

    private fun loadChats() {
        //show loading image

    }

    private fun updateUI(haveChats: Boolean) {
        //show or hide image for no chats

    }

    override fun onResume() {
        super.onResume()
        //online status update

        //load initial messages
        userID?.let{ userid ->
            viewmodel.loadAllChats(
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