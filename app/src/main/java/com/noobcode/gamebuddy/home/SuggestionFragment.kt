package com.noobcode.gamebuddy.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.noobcode.gamebuddy.CustomApplication
import com.noobcode.gamebuddy.R
import com.noobcode.gamebuddy.databinding.FragmentMatchBinding
import com.noobcode.gamebuddy.databinding.FragmentSuggestionBinding
import com.noobcode.gamebuddy.utils.FirebaseObject
import com.noobcode.gamebuddy.viewmodels.LookActivityViewModel

class SuggestionFragment : Fragment() {

    lateinit var binding: FragmentSuggestionBinding
    private lateinit var viewmodel: LookActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuggestionBinding.inflate(inflater, container, false)
        FirebaseObject.fireStore = Firebase.firestore
        val viewModel: LookActivityViewModel by viewModels()
        viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideLoading()

        binding.suggestionBtn.setOnClickListener {
            //submit a suggestion
            showLoading()
            if(binding.suggestionTextEdit.text.toString().isBlank()){
                Toast.makeText(
                    CustomApplication.ApplicationContext,
                    "please type something",
                    Toast.LENGTH_SHORT,
                ).show()

                hideLoading()
            }else if(binding.suggestionTextEdit.text.toString().length > 200){
                Toast.makeText(
                    CustomApplication.ApplicationContext,
                    "too many characters",
                    Toast.LENGTH_SHORT,
                ).show()

                hideLoading()
            }
            else{
                viewmodel.submitASuggestion(FirebaseObject.currentUser.uid, binding.suggestionTextEdit.text.toString()).observe(viewLifecycleOwner, Observer {
                    if(it != -99){
                        //writing now
                        if(it == 0){
                            //writing failed
                            Toast.makeText(
                                CustomApplication.ApplicationContext,
                                "can't submit a suggestion. Services down",
                                Toast.LENGTH_SHORT,
                            ).show()
                            hideLoading()
                        }else{
                            Toast.makeText(
                                CustomApplication.ApplicationContext,
                                "Suggestion submitted",
                                Toast.LENGTH_SHORT,
                            ).show()
                            hideLoading()
                            //show success icon for 2 seconds and then change the icon back
                            showSuccess()
                        }
                    }
                })
            }

        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showSuccess() {
        binding.suggestionTextEdit.setText("")
        binding.suggestionBtn.setImageDrawable(resources.getDrawable(R.drawable.tick_ic))
        Handler(Looper.getMainLooper()).postDelayed({
            binding.suggestionBtn.setImageDrawable(resources.getDrawable(R.drawable.arrow_ic))
        }, 1500)
    }

    private fun showLoading() {
        binding.fabProgress.visibility = View.VISIBLE
        binding.suggestionBtn.isClickable = false
    }

    private fun hideLoading() {
        binding.fabProgress.visibility = View.INVISIBLE
        binding.suggestionBtn.isClickable = true
    }
}