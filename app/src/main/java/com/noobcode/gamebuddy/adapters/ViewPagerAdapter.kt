package com.noobcode.gamebuddy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.noobcode.gamebuddy.R
import java.util.Objects


class ViewPagerAdapter(var context: Context,var featuresList: MutableList<Int>): PagerAdapter() {

    var mLayoutInflater: LayoutInflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)

    override fun getCount(): Int {
        return featuresList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View = mLayoutInflater.inflate(R.layout.item_feature, container, false)
        val imageView = itemView.findViewById<View>(R.id.image_view_feature) as ImageView
        imageView.setImageResource(featuresList[position])
        Objects.requireNonNull(container)?.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout?)
    }

}