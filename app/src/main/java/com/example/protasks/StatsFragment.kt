package com.example.protasks

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Task
import com.example.protasks.models.TaskList
import com.example.protasks.models.User
import com.example.protasks.presenters.TaskListPresenter
import com.example.protasks.utils.BottomSheet
import com.woxthebox.draglistview.BoardView
import com.woxthebox.draglistview.BoardView.BoardCallback
import com.woxthebox.draglistview.BoardView.BoardListener
import com.woxthebox.draglistview.ColumnProperties
import com.woxthebox.draglistview.DragItem
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class StatsFragment(
    private val taskLists: List<TaskList>, private val presenter: TaskListPresenter,
    private val supportFragmentManager: FragmentManager, private val boardName: String
) :
    Fragment() {
    private var recyclerView: RecyclerView?=null
    private var mColumns = 0
    private var listMap: HashMap<String, Long>? = HashMap()
    var boardId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.stats_fragment, container, false)
        recyclerView = view.findViewById(R.id.recycler_pie_chart_list)
        recyclerView!!.layoutManager = GridLayoutManager(context, 1)
        val tasks: HashMap<String, HashMap<String, Int>>? = HashMap()
        val users: ArrayList<String>? = ArrayList()
        val user:User = User("No Asignado","No asignado","No asignado","No asignado", "No asignado")
        for (list in taskLists) {
            for (t in list.getTasks()!!) {
                if (t!!.getUsers()!!.isNotEmpty()){
                    for (u in t.getUsers()!!) {
                        if (tasks!!.containsKey(u.getUsername())) {
                            if (tasks[u.getUsername()]!!.contains(list.getTitle())) {
                                tasks[u.getUsername()]!![list.getTitle()!!] = tasks[u.getUsername()]!![list.getTitle()!!]!! + 1
                            } else {
                                tasks[u.getUsername()]!![list.getTitle()!!] = 1
                            }
                        } else {
                            val h: HashMap<String, Int> = HashMap()
                            h[list.getTitle()!!] = 1
                            tasks[u.getUsername()!!] = h
                            users!!.add(u.getUsername()!!)
                        }
                    }
                }else{
                    if (tasks!!.containsKey(user.getUsername())){
                        if (tasks[user.getUsername()]!!.contains(list.getTitle())) {
                            tasks[user.getUsername()]!![list.getTitle()!!] = tasks[user.getUsername()]!![list.getTitle()!!]!! + 1
                        } else {
                            tasks[user.getUsername()]!![list.getTitle()!!] = 1
                        }
                    } else {
                        val h: HashMap<String, Int> = HashMap()
                        h[list.getTitle()!!] = 1
                        tasks[user.getUsername()!!] = h
                        users!!.add(user.getUsername()!!)
                    }
                }

            }
        }
        recyclerView!!.adapter = PieAdapter(tasks, users!!)

        return view


    }

    companion object {
        private var sCreatedItems = 0
        fun newInstance(
            taskLists: List<TaskList>,
            presenter: TaskListPresenter,
            supportFragmentManager: FragmentManager,
            boardName: String
        ): StatsFragment {
            return StatsFragment(taskLists, presenter, supportFragmentManager, boardName)
        }
    }

}