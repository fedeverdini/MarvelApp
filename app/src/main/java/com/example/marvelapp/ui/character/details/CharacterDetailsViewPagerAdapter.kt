package com.example.marvelapp.ui.character.details

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CharacterDetailsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var detailsPageList = mutableListOf<CharacterDetailsObjectFragment>()

    fun getFragment(position: Int): Fragment {
        return detailsPageList[position]
    }

    fun addPage(fragment: CharacterDetailsObjectFragment) {
        detailsPageList.add(fragment)
    }

    override fun getItemCount(): Int = detailsPageList.size

    override fun createFragment(position: Int): Fragment {
        return detailsPageList[position]
    }

    fun getTabName(position: Int): String {
        return detailsPageList[position].tabName
    }
}
