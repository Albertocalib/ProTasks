package com.example.protasks.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.protasks.BoardFragment
import com.example.protasks.R
import com.example.protasks.models.TaskList
import com.example.protasks.presenters.TaskListPresenter
import com.example.protasks.views.IInsideBoardsView
import java.util.*

class BoardInsideActivity : AppCompatActivity(), IInsideBoardsView {
    private var lists: List<TaskList> = ArrayList()
    private var boardName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val presenter = TaskListPresenter(this, baseContext)
        boardName = intent.getStringExtra("BOARD_NAME")
        presenter.getLists(boardName!!)
        if (savedInstanceState == null) {
            showFragment(BoardFragment(lists))
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transaction =
            supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment, "fragment").commit()
    }

    override fun setTaskLists(taskList: List<TaskList>) {
        lists = taskList
        showFragment(BoardFragment(lists))
    }
}