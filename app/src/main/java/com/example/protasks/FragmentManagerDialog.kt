package com.example.protasks

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.protasks.models.Board


class FragmentManagerDialog(fm: FragmentActivity,val boards:List<Board>,val t: Toolbar) :
    FragmentStateAdapter(fm) {
    val boardTab=Board_tab.newInstance(t)
    val tabTab=Task_tab.newInstance(boards)

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> boardTab
            1 -> tabTab
            else -> Board_tab.newInstance(t)
        }
    }
}