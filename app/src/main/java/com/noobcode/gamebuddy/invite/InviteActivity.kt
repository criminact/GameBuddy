package com.noobcode.gamebuddy.invite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cioccarellia.ksprefs.KsPrefs
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.noobcode.gamebuddy.CustomApplication
import com.noobcode.gamebuddy.viewmodels.InviteActivityViewModel
import com.noobcode.gamebuddy.databinding.ActivityInviteBinding
import com.noobcode.gamebuddy.home.HomeActivity
import com.noobcode.gamebuddy.utils.FirebaseObject

class InviteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteBinding
    private lateinit var viewmodel: InviteActivityViewModel
    val prefs = KsPrefs(CustomApplication.ApplicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteBinding.inflate(layoutInflater)
        val viewModel: InviteActivityViewModel by viewModels()
        viewmodel = viewModel
        setContentView(binding.root)
        FirebaseObject.fireStore = Firebase.firestore

        hideLoadingUI()

        binding.inviteBtn.setOnClickListener {
            showLoadingUI()
            validateInviteCode()
        }
    }

    private fun showLoadingUI() {
        binding.fabProgress.visibility = View.VISIBLE
        binding.inviteBtn.isClickable = false
    }

    private fun hideLoadingUI() {
        binding.fabProgress.visibility = View.INVISIBLE
        binding.inviteBtn.isClickable = true
    }

    private fun validateInviteCode() {
        if(binding.inviteCodeEditext.text.isNullOrEmpty()){
            binding.filledTextField.error = "Invite code empty, please try again."
            hideLoadingUI()
        }else if(binding.inviteCodeEditext.text.toString().length > 5){
            binding.filledTextField.error = "Invite code seems wrong, please try again."
            hideLoadingUI()
        }
        else{
            viewmodel.checkForInviteCode(binding.inviteCodeEditext.text.toString(), FirebaseObject.currentUser.uid).observe(this, Observer {
                if(it != -99){
                    //writing now
                    if(it == 0){
                        //writing failed
                        binding.filledTextField.error = "Invite code invalid, please try again."
                        hideLoadingUI()
                    }else{
                        //writing success
                        prefs.push("isVerified", true)
                        openHome()
                    }
                }
            })
        }
    }

    private fun openHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }
}

