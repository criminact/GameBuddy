package com.noobcode.gamebuddy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noobcode.gamebuddy.repo.UserRepo
import com.noobcode.gamebuddy.utils.ObjectModels

class HomeFragmentViewModel: ViewModel() {

    var userRepo = UserRepo()

    fun loadAnnouncements(): LiveData<MutableList<ObjectModels.Announcement>?>{
       return userRepo.loadAnnouncements()
    }

}