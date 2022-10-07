package com.example.protasks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.activities.BoardInsideActivity
import com.example.protasks.models.*
import com.example.protasks.presenters.board.BoardPresenter
import com.example.protasks.presenters.task.TaskPresenter
import com.example.protasks.presenters.board.IBoardContract
import com.example.protasks.presenters.task.ITaskContract
import com.example.protasks.utils.Preference
import com.example.protasks.utils.PreferencesManager

class SearchTab(private val cont:Context) : Fragment(),IBoardContract.View,ITaskContract.View,BoardAdapter.OnItemClickListener {
    private var presenter: BoardPresenter? = null
    var recyclerViewBoard: RecyclerView? = null
    var recyclerViewTask: RecyclerView? = null
    var searchView: SearchView? = null
    private var taskPresenter: TaskPresenter?=null
    private var boards:ArrayList<Board>?=ArrayList()
    private var notYetImplemented = "Not Yet Implemented"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.search_view, container, false)
        val preference: PreferencesManager = Preference(requireContext())
        presenter = BoardPresenter(this, preference)
        presenter!!.getBoards()
        taskPresenter= TaskPresenter(this, preference,requireContext().contentResolver)
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

    override fun onResponseFailure(t: Throwable?) {
        Log.e("SEARCHTAB", t!!.message!!)
        Toast.makeText(context, getString(R.string.communication_error), Toast.LENGTH_LONG).show()
    }

    override fun setBoards(boards: ArrayList<Board>) {
        this.boards = boards
        recyclerViewBoard!!.adapter = BoardAdapter(boards, R.layout.board,this)
    }

    override fun setUser(user: User) {
        TODO(notYetImplemented)
    }

    override fun addMessage(msg: Message) {
        TODO(notYetImplemented)
    }


    override fun getBoards() {
        TODO(notYetImplemented)
    }

    override fun setBoard(board: Board) {
        TODO(notYetImplemented)
    }

    override fun setRole(perm: BoardUsersPermRel) {
        TODO(notYetImplemented)
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun addBoard(board: Board) {
        boards!!.add(board)
        setBoards(boards!!)
    }

    override fun setTasks(tasks: List<Task>) {
        recyclerViewTask!!.adapter = TaskAdapter(tasks, R.layout.task)
    }

    override fun setAssignments(users: List<User>) {
        TODO(notYetImplemented)
    }

    override fun setUsers(users: List<User>) {
        TODO(notYetImplemented)
    }

    override fun setTags(tags: List<Tag>) {
        TODO(notYetImplemented)
    }

    override fun setTagsBoard(tags: List<Tag>) {
        TODO(notYetImplemented)
    }

    override fun updateTags(tag: Tag) {
        TODO(notYetImplemented)
    }

    override fun updateTask(t: Task) {
        TODO(notYetImplemented)
    }

    override fun onItemClicked(boardId: Long?, boardName: String) {
        val intent = Intent(context, BoardInsideActivity::class.java)
        intent.putExtra("BOARD_NAME", boardName)
        intent.putExtra("BOARD_ID",boardId)
        startActivity(intent)
    }


}