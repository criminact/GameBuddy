package com.noobcode.gamebuddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.noobcode.gamebuddy.CustomApplication
import com.noobcode.gamebuddy.databinding.ItemAnouncementsBinding
import com.noobcode.gamebuddy.databinding.ItemChatSelfBinding
import com.noobcode.gamebuddy.databinding.ItemChatThemBinding
import com.noobcode.gamebuddy.utils.ObjectModels

class AnnouncementAdapter(var announcements: MutableList<ObjectModels.Announcement>): RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder>()  {

    fun updateList(tempAnnouncements: MutableList<ObjectModels.Announcement>){
        announcements.clear()
        announcements = tempAnnouncements
        notifyDataSetChanged()
    }

    fun addAnnouncement(tempAnnouncements: ObjectModels.Announcement){
        announcements.add(tempAnnouncements)
        notifyItemInserted(announcements.size-1)
    }

    fun deleteAnnouncement(tempAnnouncements: ObjectModels.Announcement){
        val itemIdx = announcements.indexOf(tempAnnouncements)
        announcements.remove(tempAnnouncements)
        notifyItemRemoved(itemIdx)
    }

    class AnnouncementViewHolder(var binding: ItemAnouncementsBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementViewHolder {
        var binding = ItemAnouncementsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnnouncementViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return announcements.size
    }

    override fun onBindViewHolder(holder: AnnouncementViewHolder, position: Int) {
        holder.binding.anouncementTitle.text = announcements[position].title
        holder.binding.anouncementDesc.text = announcements[position].description
        if(!announcements[position].image_url.isNullOrEmpty()){
            Glide.with(CustomApplication.ApplicationContext).load(announcements[position].image_url).into(holder.binding.anouncementImage)
        }else{
            holder.binding.anouncementImage.visibility = View.GONE
        }
    }
}