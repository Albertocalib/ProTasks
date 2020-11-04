package com.example.protasks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.activities.BoardInsideActivity
import com.example.protasks.models.Board
import com.example.protasks.models.Task
import com.example.protasks.models.User
import com.example.protasks.presenters.BoardPresenter
import com.example.protasks.presenters.TaskPresenter
import com.example.protasks.views.IBoardsView
import com.example.protasks.views.ITasksView


class SearchTab(private val cont:Context) : Fragment(),IBoardsView,ITasksView,BoardAdapter.OnItemClickListener {
    private var presenter: BoardPresenter? = null
    var recyclerViewBoard: RecyclerView? = null
    var recyclerViewTask: RecyclerView? = null
    var searchView: SearchView? = null
    private var taskPresenter: TaskPresenter?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.search_view, container, false)
        presenter = BoardPresenter(this, cont)
        presenter!!.getBoards()
        taskPresenter= TaskPresenter(this,cont)
        taskPresenter!!.getTasks()
        recyclerViewTask = view.findViewById(R.id.recycler_task_search)
        recyclerViewBoard = view.findViewById(R.id.recycler_board_search)
        recyclerViewBoard!!.layoutManager = GridLayoutManager(cont,2)
        recyclerViewTask!!.layoutManager = LinearLayoutManager(cont)

        searchView = view.findViewById(R.id.search_view_all)

        searchView!!.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    presenter!!.filterBoards(newText)
                    taskPresenter!!.filterTasks(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    presenter!!.filterBoards(query)
                    taskPresenter!!.filterTasks(query)
                    return true
                }

            })

        return view

    }

    companion object {
        fun newInstance(context:Context): SearchTab = SearchTab(context)
    }
    override fun setBoards(boards: List<Board>) {
        recyclerViewBoard!!.adapter = BoardAdapter(boards, R.layout.board,this)
    }

    override fun setUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun logOut() {
        TODO("Not yet implemented")
    }

    override fun getBoards() {
        TODO("Not yet implemented")
    }

    override fun setTasks(tasks: List<Task>) {
        recyclerViewTask!!.adapter = TaskAdapter(tasks, R.layout.task)
    }

    override fun setAssignments(users: List<User>) {
        TODO("Not yet implemented")
    }

    override fun setUsers(users: List<User>) {
        TODO("Not yet implemented")
    }

    override fun onItemClicked(text: TextView) {
        val intent = Intent(context, BoardInsideActivity::class.java)
        intent.putExtra("BOARD_NAME", text.text)
        startActivity(intent)
    }


}