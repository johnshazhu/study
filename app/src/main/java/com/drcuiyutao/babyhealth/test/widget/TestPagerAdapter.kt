package com.drcuiyutao.babyhealth.test.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class TestPagerAdapter(context: Context, views: List<View>, data: List<Int>): PagerAdapter() {
    private val maxCount = 3
    private var list: List<View>
    private var data: List<Int>
    private var context: Context? = null

    init {
        this.context = context
        this.list = views
        this.data = data
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(list[position])
        return list[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        var size: Int = list.size
        return if (size < maxCount) size else maxCount
    }
}