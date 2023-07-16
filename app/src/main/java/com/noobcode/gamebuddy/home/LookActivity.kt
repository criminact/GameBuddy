package com.noobcode.gamebuddy.home

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.noobcode.gamebuddy.viewmodels.LookActivityViewModel
import com.noobcode.gamebuddy.utils.ObjectModels
import com.noobcode.gamebuddy.utils.Utils
import com.noobcode.gamebuddy.databinding.ActivityLookBinding
import com.noobcode.gamebuddy.utils.FirebaseObject

class LookActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityLookBinding
    private lateinit var viewmodel: LookActivityViewModel

    var gameSelected: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLookBinding.inflate(layoutInflater)
        val viewModel: LookActivityViewModel by viewModels()
        viewmodel = viewModel
        setContentView(binding.root)
        FirebaseObject.fireStore = Firebase.firestore

        binding.searchNowBtn.setOnClickListener {

            //look for a game -> status set then -> look for a player
            viewmodel.lookForAPlayer(gameSelected, FirebaseObject.currentUser.uid).observe(this, Observer { userID ->
                if(!userID.isNullOrBlank()){
                    //open chat activity with this userID
                    Toast.makeText(
                        baseContext,
                        "Got a player $it",
                        Toast.LENGTH_SHORT,
                    ).show()

                    //open a chat b/w the 2 players
                    viewmodel.openChatBetweenUsers(
                        Utils.getAStringFrom2Users(
                            FirebaseObject.currentUser.uid,
                            userID
                        ), FirebaseObject.currentUser.uid, userID).observe(this, Observer {
                        if(it != -99){
                            //writing now
                            if(it == 0){
                                //writing failed
                                Toast.makeText(
                                    baseContext,
                                    "can't look for a player. Services down",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }else{
                                //writing success
                                var chatIntent = Intent(this, ChatActivity::class.java)
                                var chatData = Bundle()
                                chatData.putString("userID", userID)
                                chatIntent.putExtras(chatData)
                                startActivity(chatIntent)
                            }
                        }
                    })


                }else{
                    Toast.makeText(
                        baseContext,
                        "couldn't get a player. Services down",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            })

        }

        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            R.layout.simple_spinner_item,
            ObjectModels.gameNameToInt.keys.toList()
        )

        binding.gameSpinner.adapter = ad

        binding.gameSpinner.onItemSelectedListener = this

        binding.gameSpinner.setSelection(0)

        gameSelected = ObjectModels.gameNameToInt.values.toList()[0]

    }

    override fun onResume() {
        super.onResume()
        //look for a game -> status set then -> look for a player
        setStatus(true)
    }

    override fun onPause() {
        super.onPause()
        //set status to not looking for a game anymore
        setStatus(false)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        gameSelected = ObjectModels.gameNameToInt.values.toList()[p2]
        setStatus(true)
    }

    private fun setStatus(status: Boolean) {
        viewmodel.setStatusForLookup(FirebaseObject.currentUser.uid, status, gameSelected).observe(this, Observer {
            if(it != -99){
                //writing now
                if(it == 0){
                    //writing failed
                    Toast.makeText(
                        baseContext,
                        "can't look for a player. Services down",
                        Toast.LENGTH_SHORT,
                    ).show()
                }else{
                    //writing success
                    Toast.makeText(
                        baseContext,
                        "looking for a player",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        })
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(
            baseContext,
            "please select a game",
            Toast.LENGTH_SHORT,
        ).show()
    }

}