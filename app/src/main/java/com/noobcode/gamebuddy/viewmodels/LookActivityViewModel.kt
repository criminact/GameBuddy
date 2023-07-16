package com.noobcode.gamebuddy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noobcode.gamebuddy.repo.UserRepo

class LookActivityViewModel: ViewModel() {

    var userRepo: UserRepo = UserRepo()

    fun lookForAPlayer(game: Int, userID: String): LiveData<String?>{
        return userRepo.lookForAPlayer(game, userID)
    }

    fun setStatusForLookup(userID: String, status: Boolean, game: Int): LiveData<Int>{
        return userRepo.setStatusForLookup(userID, status, game)
    }

    fun openChatBetweenUsers(documentName: String, userID1: String, userID2: String): LiveData<Int>{
        return userRepo.openChatBetweenUsers(documentName, userID1, userID2)
    }

    fun submitASuggestion(userID: String, suggestion: String): LiveData<Int>{
        return userRepo.submitSuggestion(userID, suggestion)
    }

}