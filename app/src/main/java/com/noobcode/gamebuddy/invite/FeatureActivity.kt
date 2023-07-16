package com.noobcode.gamebuddy.invite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.noobcode.gamebuddy.home.HomeActivity
import com.noobcode.gamebuddy.R
import com.noobcode.gamebuddy.adapters.ViewPagerAdapter
import com.noobcode.gamebuddy.databinding.ActivityFeatureBinding

class FeatureActivity : AppCompatActivity() {

    var featureList = mutableListOf(
        R.drawable.feature1,
        R.drawable.feature2
    )
    lateinit var binding: ActivityFeatureBinding
    lateinit var featureAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        featureAdapter = ViewPagerAdapter(this, featureList)
        binding.featureViewPager.adapter = featureAdapter

        binding.pageIndicatorView.count = 2
        binding.pageIndicatorView.selection = 0

        binding.featureViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) { /*empty*/
            }

            override fun onPageSelected(position: Int) {

                if(position == 0){
                    binding.featureTitle.setText(R.string.feature_title1)
                    binding.featureDesc.setText(R.string.feature_desc1)
                }else if(position == 1){
                    binding.featureTitle.setText(R.string.feature_title2)
                    binding.featureDesc.setText(R.string.feature_desc2)
                }

                binding.pageIndicatorView.selection = position
            }

            override fun onPageScrollStateChanged(state: Int) { /*empty*/
            }
        })


        binding.aheadFeatureBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

    }
}