package com.noobcode.gamebuddy.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.noobcode.gamebuddy.CustomApplication
import com.noobcode.gamebuddy.adapters.AnnouncementAdapter
import com.noobcode.gamebuddy.databinding.FragmentHomeBinding
import com.noobcode.gamebuddy.utils.FirebaseObject
import com.noobcode.gamebuddy.utils.ObjectModels
import com.noobcode.gamebuddy.viewmodels.HomeFragmentViewModel
import com.noobcode.gamebuddy.viewmodels.LookActivityViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewmodel: HomeFragmentViewModel
    private lateinit var announcementAdapter: AnnouncementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        FirebaseObject.fireStore = Firebase.firestore
        val viewModel: HomeFragmentViewModel by viewModels()
        viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        announcementAdapter = AnnouncementAdapter(mutableListOf())
        binding.homeAnnouncementsRecyclerview.adapter = announcementAdapter
        binding.homeAnnouncementsRecyclerview.layoutManager = LinearLayoutManager(CustomApplication.ApplicationContext)

        //get all announcements
        showLoading()
        viewmodel.loadAnnouncements().observe(viewLifecycleOwner, Observer { announcements ->
            if(announcements == null){
                //no announcements
                showEmptyAnnouncements()
            }else{
                if(announcements.size > 0) {
                    hideLoading()
                    announcementAdapter.updateList(announcements)
                }
                else{
                    hideLoading()
                    showEmptyAnnouncements()
                }
            }
        })
    }

    private fun hideLoading() {
        binding.fabProgress.visibility = View.GONE
    }

    private fun showLoading() {
        binding.fabProgress.visibility = View.VISIBLE
    }

    private fun showEmptyAnnouncements() {
        var announcement = ObjectModels.Announcement()
        announcement.title = "No Announcements"
        announcement.description = "Nothing to show here.."
        announcementAdapter.addAnnouncement(announcement)
    }

}