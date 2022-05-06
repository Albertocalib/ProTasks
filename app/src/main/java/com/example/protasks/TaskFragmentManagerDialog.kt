package com.example.protasks

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.protasks.models.Board
import com.example.protasks.models.Rol
import com.example.protasks.models.Task


class TaskFragmentManagerDialog(fm: FragmentActivity, t: Toolbar,
                                task: Task,
                                boardName: String,
                                boardId: Long,
                                fragmentMgr: FragmentManager,
                                viewHolder: TaskAdapterInsideBoard.ViewHolder?,
                                val rol: Rol?) :
    FragmentStateAdapter(fm) {
    private val detailsTab=TaskDetailsTab.newInstance(t,task,boardName,boardId,fragmentMgr,viewHolder,rol)
    private val commentsTab=TaskCommentsTab.newInstance(t,task,boardName,boardId,fragmentMgr,viewHolder,rol)

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> detailsTab
            else -> commentsTab
        }
    }
}