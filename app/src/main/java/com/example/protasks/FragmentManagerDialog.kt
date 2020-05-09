package com.example.protasks

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class FragmentManagerDialog(fm: FragmentActivity) :
    FragmentStateAdapter(fm) {


    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Board_tab.newInstance()
            1 -> Task_tab.newInstance()
            else -> Board_tab.newInstance()
        }
    }
}