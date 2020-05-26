package com.study.doc.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.study.doc.test.databinding.ActivityTestPagerBinding
import com.study.doc.test.databinding.PagerItemBinding
import com.study.doc.test.widget.TestPagerAdapter
import java.util.*
import kotlin.collections.ArrayList

class TestPagerActivity: BaseActivity(), ViewPager.OnPageChangeListener {
    private var lastItem = 0
    private var realIndex = 0
    private var pager: ViewPager? = null
    private var reset = false
    private var listBinding: ArrayList<PagerItemBinding> = ArrayList()
    private var data = ArrayList<Int>()
    companion object {
        private val TAG = TestPagerActivity::class.java.simpleName
        private const val MAX_SETTLE_DURATION : Long = 600
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityTestPagerBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_pager)

        var colors = listOf(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark)
        var list = LinkedList<View>()
        binding.pager.addOnPageChangeListener(this)
        for (i in 1..3) {
            var viewBinding: PagerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pager_item, null, false)
            viewBinding.content.text = (i - 1).toString()
            viewBinding.root.setBackgroundResource(colors[i - 1])
            list.add(viewBinding.root)
            listBinding.add(viewBinding)
        }

        for (i in 1..10) {
            data.add(i)
        }
        binding.pager.adapter = TestPagerAdapter(this, list, data)
        binding.pager.setCurrentItem(1, false)
        binding.pager.offscreenPageLimit = 2
        realIndex = 1
        lastItem = 1
        pager = binding.pager
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        if (!reset) {
            realIndex += position - lastItem
            Log.i(TAG, "onPageSelected realIndex : $realIndex, lastItem : $lastItem")
            if (position != 1) {
                if (realIndex == 0 || realIndex == data.size) {
                    //滑动到最左/右侧时不做特殊处理
                } else {
                    //其他位置时，显示默认动画效果后重置为居中item
                    reset = true
                    pager?.postDelayed({ pager?.setCurrentItem(1, false) }, MAX_SETTLE_DURATION)
                }
            }
        } else {
            updateContent()
            reset = false
        }
        lastItem = position
    }

    private fun updateContent() {
        for (i in 1..3) {
            var itemBinding = listBinding[i - 1]
            when (i) {
                1 -> itemBinding.content.text = (realIndex - 1).toString()
                2 -> itemBinding.content.text = realIndex.toString()
                3 -> itemBinding.content.text = (realIndex + 1).toString()
            }
        }
    }
}