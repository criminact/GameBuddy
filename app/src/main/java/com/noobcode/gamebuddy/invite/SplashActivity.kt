package com.noobcode.gamebuddy.invite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.cioccarellia.ksprefs.KsPrefs
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.noobcode.gamebuddy.CustomApplication
import com.noobcode.gamebuddy.R
import com.noobcode.gamebuddy.databinding.ActivitySplashBinding
import com.noobcode.gamebuddy.home.HomeActivity
import com.noobcode.gamebuddy.utils.FirebaseObject
import com.noobcode.gamebuddy.viewmodels.InviteActivityViewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    val prefs = KsPrefs(CustomApplication.ApplicationContext)
    private lateinit var viewmodel: InviteActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val viewModel: InviteActivityViewModel by viewModels()
        viewmodel = viewModel
        setContentView(binding.root)
        FirebaseObject.fireStore = Firebase.firestore
    }

    override fun onStart() {
        super.onStart()

        val currUser = FirebaseObject.auth.currentUser

        if(currUser == null){
            FirebaseObject.auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = FirebaseObject.auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)
                    }
                }
        }else{
            FirebaseObject.currentUser = currUser
            updateUI(currUser)
        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser == null){
            Toast.makeText(
                baseContext,
                "Authentication failed.",
                Toast.LENGTH_SHORT,
            ).show()
        }else{
            //check sp if user verified?
            //if yes then go to home
            var isVerified: Boolean = prefs.pull(key = "isVerified", fallback = false)
            //if not then check from db
            if(isVerified){
                openHome()
            }else{
                viewmodel.checkIfUserVerified(firebaseUser.uid).observe(this, Observer {
                    if(it != -99){
                        //writing now
                        if(it == 0){
                            //writing failed
                            openInvite()
                        }else{
                            //writing success
                            prefs.push("isVerified", true)
                            openHome()
                        }
                    }
                })
            }
        }
    }

    private fun openInvite() {
        startActivity(Intent(this, InviteActivity::class.java))
    }

    private fun openHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }
}