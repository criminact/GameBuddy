package com.noobcode.gamebuddy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noobcode.gamebuddy.repo.UserRepo

class InviteActivityViewModel: ViewModel() {

    var userRepo = UserRepo()

    fun checkForInviteCode(code: String, userID: String): LiveData<Int>{
        return userRepo.checkInviteCode(code, userID)
    }

    fun checkIfUserVerified(userID: String): LiveData<Int>{
        return userRepo.isUserVerified(userID)
    }

}