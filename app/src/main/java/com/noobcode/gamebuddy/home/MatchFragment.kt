package com.noobcode.gamebuddy.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.noobcode.gamebuddy.CustomApplication
import com.noobcode.gamebuddy.R
import com.noobcode.gamebuddy.databinding.FragmentMatchBinding
import com.noobcode.gamebuddy.utils.FirebaseObject
import com.noobcode.gamebuddy.utils.ObjectModels
import com.noobcode.gamebuddy.utils.Utils
import com.noobcode.gamebuddy.viewmodels.LookActivityViewModel


class MatchFragment : Fragment()  {

    lateinit var binding: FragmentMatchBinding
    private lateinit var viewmodel: LookActivityViewModel
    var gameSelected: Int = 99

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchBinding.inflate(inflater, container, false)
        FirebaseObject.fireStore = Firebase.firestore
        val viewModel: LookActivityViewModel by viewModels()
        viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideLoading()

        binding.searchNowBtn.setOnClickListener {
            showLoading()
            //look for a game -> status set then -> look for a player
            if(gameSelected >= ObjectModels.gameNameToInt.keys.size){
                Toast.makeText(
                    CustomApplication.ApplicationContext,
                    "please select a game",
                    Toast.LENGTH_SHORT,
                ).show()

                hideLoading()

            }else{
                viewmodel.lookForAPlayer(gameSelected, FirebaseObject.currentUser.uid).observe(viewLifecycleOwner, Observer { userID ->
                    if(!userID.isNullOrBlank()){
                        //open chat activity with this userID
                        Toast.makeText(
                            CustomApplication.ApplicationContext,
                            "Got a player $it",
                            Toast.LENGTH_SHORT,
                        ).show()

                        //open a chat b/w the 2 players
                        viewmodel.openChatBetweenUsers(
                            Utils.getAStringFrom2Users(
                                FirebaseObject.currentUser.uid,
                                userID
                            ), FirebaseObject.currentUser.uid, userID).observe(viewLifecycleOwner, Observer {
                            if(it != -99){
                                //writing now
                                if(it == 0){
                                    //writing failed
                                    Toast.makeText(
                                        CustomApplication.ApplicationContext,
                                        "can't look for a player. Services down",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }else{
                                    hideLoading()
                                    //writing success
                                    var chatIntent = Intent(CustomApplication.ApplicationContext, ChatActivity::class.java)
                                    var chatData = Bundle()
                                    chatData.putString("userID", userID)
                                    chatIntent.putExtras(chatData)
                                    startActivity(chatIntent)
                                }
                            }
                        })


                    }else{
                        Toast.makeText(
                            CustomApplication.ApplicationContext,
                            "couldn't get a player. Services down",
                            Toast.LENGTH_SHORT,
                        ).show()

                        hideLoading()
                    }
                })
            }
        }

        val adapter = ArrayAdapter(CustomApplication.ApplicationContext, R.layout.layout_item, ObjectModels.gameNameToInt.keys.toList())
        (binding.menuAutoComplete as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.menuAutoComplete.onItemClickListener = OnItemClickListener { parent, arg1, pos, id ->
            gameSelected = ObjectModels.gameNameToInt.values.toList()[pos]
            setStatus(true)
        }
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

    private fun setStatus(status: Boolean) {
        viewmodel.setStatusForLookup(FirebaseObject.currentUser.uid, status, gameSelected).observe(this, Observer {
            if(it != -99){
                //writing now
                if(it == 0){
                    //writing failed
                    Toast.makeText(
                        CustomApplication.ApplicationContext,
                        "can't look for a player. Services down",
                        Toast.LENGTH_SHORT,
                    ).show()
                }else{
                    //writing success
                    Toast.makeText(
                        CustomApplication.ApplicationContext,
                        "looking for a player",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        })
    }

    private fun showLoading() {
        binding.fabProgress.visibility = View.VISIBLE
        binding.searchNowBtn.isClickable = false
    }

    private fun hideLoading() {
        binding.fabProgress.visibility = View.INVISIBLE
        binding.searchNowBtn.isClickable = true
    }

}