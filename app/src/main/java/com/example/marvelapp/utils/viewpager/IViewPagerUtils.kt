package com.example.marvelapp.utils.viewpager

import android.view.View
import androidx.viewpager2.widget.ViewPager2

interface IViewPagerUtils {
    fun updatePagerHeightForChild(view: View, pager: ViewPager2, isExpandable: Boolean = false)
}