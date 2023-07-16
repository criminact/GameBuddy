package com.noobcode.gamebuddy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noobcode.gamebuddy.utils.ObjectModels
import com.noobcode.gamebuddy.repo.UserRepo

class ChatActivityViewModel : ViewModel(){

    var userRepo = UserRepo()

    fun loadAllChatMessages(documentName: String): LiveData<MutableList<ObjectModels.ChatData>?> {
        return userRepo.loadAllChatMessages(documentName)
    }

    fun loadAllMessages(userID: String): LiveData<MutableList<ObjectModels.Chat>?> {
        return userRepo.loadAllMessages(userID)
    }

    fun listenToChatChanges(documentName: String, userID: String): LiveData<ObjectModels.ChatData?> {
        return userRepo.listenToChatEvents(documentName, userID)
    }

    fun sendMessage(documentName: String, message: ObjectModels.ChatData) {
        userRepo.sendMessage(documentName, message)
    }

}
