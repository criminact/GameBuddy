package com.noobcode.gamebuddy.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.noobcode.gamebuddy.CustomApplication
import com.noobcode.gamebuddy.databinding.ItemMatchBinding
import com.noobcode.gamebuddy.interfaces.MatchCallBack
import com.noobcode.gamebuddy.utils.FirebaseObject
import com.noobcode.gamebuddy.utils.ObjectModels
import com.noobcode.gamebuddy.utils.Utils

class MatchAdapter(var matchList: MutableList<ObjectModels.Chat>, var clickCallback: MatchCallBack): RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    fun updateList(tempMatches: MutableList<ObjectModels.Chat>){
        matchList.clear()
        matchList = tempMatches
        notifyDataSetChanged()
    }

    fun addMatch(tempMatch: ObjectModels.Chat){
        matchList.add(tempMatch)
        notifyItemInserted(matchList.size-1)
    }

    fun deleteMessage(tempMatch: ObjectModels.Chat){
        val itemIdx = matchList.indexOf(tempMatch)
        matchList.remove(tempMatch)
        notifyItemRemoved(itemIdx)
    }

    class MatchViewHolder(var binding: ItemMatchBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        var binding = ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        if(FirebaseObject.currentUser.uid == matchList[position].userID1){
            holder.binding.matchNameText.text = matchList[position].userID2
        }else{
            holder.binding.matchNameText.text = matchList[position].userID1
        }
        holder.binding.matchImageView.setImageDrawable(CustomApplication.ApplicationContext.resources.getDrawable(
            ObjectModels.avatarForUsers[Utils.getRandomNumber(
            0,
            ObjectModels.avatarForUsers.size - 1
        )]))

        holder.binding.root.setOnClickListener {
            if(FirebaseObject.currentUser.uid == matchList[position].userID1){
                clickCallback.matchClicked(matchList[position].userID2)
            }else{
                clickCallback.matchClicked(matchList[position].userID1)
            }
        }
    }
}