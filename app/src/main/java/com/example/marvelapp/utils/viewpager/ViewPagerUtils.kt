package com.example.marvelapp.utils.viewpager

import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2

class ViewPagerUtils: IViewPagerUtils {
    override fun updatePagerHeightForChild(view: View, pager: ViewPager2, isExpandable: Boolean) {
        view.post {
            val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                view.width, View.MeasureSpec.EXACTLY
            )
            val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                0, View.MeasureSpec.UNSPECIFIED
            )
            view.measure(widthMeasureSpec, heightMeasureSpec)
            if (pager.layoutParams.height != view.measuredHeight) {
                pager.layoutParams = (pager.layoutParams).also {
                    it.height = view.measuredHeight
                }
                if (isExpandable) {
                    pager.layoutParams = (pager.layoutParams).also {
                        it.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                }
            }
        }
    }
}