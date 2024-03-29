package com.example.protasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.protasks.models.Rol
import com.example.protasks.models.Task
import com.example.protasks.models.TaskList
import com.example.protasks.presenters.tasklist.ITaskListContract
import com.example.protasks.presenters.tasklist.TaskListPresenter
import com.example.protasks.utils.Preference
import com.example.protasks.utils.PreferencesManager
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.DragListView.DragListListenerAdapter
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.woxthebox.draglistview.swipe.ListSwipeItem.SwipeDirection
import java.util.*

class ListFragment(private var boardsView: ITaskListContract.ViewNormal) : Fragment() {
    private var mItemArray: ArrayList<Triple<Long, Task, Boolean>>? =
        null
    private var mDragListView: DragListView? = null
    private var mRefreshLayout: MySwipeRefreshLayout? = null
    var rol: Rol?=null
    private var taskLists: List<TaskList>?=null
    private var presenter: TaskListPresenter?=null
    private var boardName: String?=null
    private var supportFragmentManager: FragmentManager?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_layout, container, false)
        mRefreshLayout =
            view.findViewById<View>(R.id.swipe_refresh_layout) as MySwipeRefreshLayout
        mDragListView = view.findViewById<View>(R.id.drag_list_view) as DragListView
        return view
    }

    fun onItemDragEndedC(fromPosition: Int, toPosition: Int){
        mRefreshLayout!!.isEnabled = true
        if (fromPosition != toPosition) {
            val tripleEl =
                mDragListView!!.adapter.itemList[toPosition] as Triple<*, *, *>
            val tripleElBef =
                mDragListView!!.adapter.itemList[toPosition - 1] as Triple<*, *, *>
            val taskBef = tripleElBef.second as Task
            val task = tripleEl.second as Task
            if (taskBef.getTaskList().getTitle() != task.getTaskList().getTitle()) {
                presenter!!.updateTaskPosition(
                    task.getId()!!,
                    taskBef.getPosition()!!.toLong() + 1,
                    taskBef.getTaskList().getId(),
                    true
                )
            } else {
                var pos = taskBef.getPosition()!!.toLong()
                if (fromPosition >= toPosition) {
                    pos += 1
                }
                presenter!!.updateTaskPosition(
                    task.getId()!!,
                    taskBef.getPosition()!!.toLong(),
                    task.getTaskList().getId(),
                    true
                )

            }

        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        taskLists = arguments?.getParcelableArrayList("taskLists")
        boardName = arguments?.getString("boardName")
        val preference:PreferencesManager = Preference(requireContext())
        presenter = TaskListPresenter(boardsView, preference)
        supportFragmentManager = this.fragmentManager
        mDragListView!!.recyclerView.isVerticalScrollBarEnabled = true
        mDragListView!!.isDragEnabled = true
        mDragListView!!.setCanNotDragAboveTopItem(true)
        mDragListView!!.setDragListListener(object : DragListListenerAdapter() {
            override fun onItemDragStarted(position: Int) {
                mRefreshLayout!!.isEnabled = false
            }

            override fun onItemDragEnded(fromPosition: Int, toPosition: Int) {
                onItemDragEndedC(fromPosition,toPosition)
            }
        })
        mItemArray =
            ArrayList<Triple<Long, Task, Boolean>>()
        for (list in taskLists!!) {
            val tasks = list.getTasks()!!.sortedWith(compareBy { it!!.getPosition() })
            val taskFake: Task = Task("", "", list)
            taskFake.setPosition(0)
            mItemArray!!.add(
                Triple(
                    (100000..Long.MAX_VALUE).random(),
                    taskFake,
                    true
                )
            )
            for (task in tasks) {
                task!!.setTaskList(list)
                mItemArray!!.add(
                    Triple(
                        task.getId() as Long,
                        task,
                        false
                    )
                )
            }

        }
        mRefreshLayout!!.setScrollingView(mDragListView!!.recyclerView)
        mRefreshLayout!!.setColorSchemeColors(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        mRefreshLayout!!.setOnRefreshListener {
            mRefreshLayout!!.postDelayed(
                { presenter!!.getLists(boardName!!) }, 2000
            )
        }
        mDragListView!!.setSwipeListener(object : ListSwipeHelper.OnSwipeListenerAdapter() {
            override fun onItemSwipeStarted(item: ListSwipeItem) {
                mRefreshLayout!!.isEnabled = false
            }

            override fun onItemSwipeEnded(
                item: ListSwipeItem,
                swipedDirection: SwipeDirection
            ) {
                mRefreshLayout!!.isEnabled = true

                // Swipe to delete on left
                if (swipedDirection == SwipeDirection.LEFT) {
                    val adapterItem =
                        item.tag as Pair<Long, String>
                    val pos = mDragListView!!.adapter.getPositionForItem(adapterItem)
                    mDragListView!!.adapter.removeItem(pos)
                }
            }
        })
        mDragListView!!.setDragListCallback(object : DragListView.DragListCallback {
            override fun canDragItemAtPosition(dragPosition: Int): Boolean {
                val tripleEl =
                    mDragListView!!.adapter.itemList[dragPosition] as Triple<*, *, *>
                val firstElement = tripleEl.third as Boolean
                return rol != Rol.WATCHER && !firstElement
            }

            override fun canDropItemAtPosition(dropPosition: Int): Boolean {
                return true
            }
        })
        setupListRecyclerView()
    }


    fun setWatcherVisibility(){
        setupListRecyclerView()
    }


    private fun setupListRecyclerView() {
        mDragListView!!.setLayoutManager(LinearLayoutManager(context))
        var boardId:Long=0
        if (taskLists!!.isNotEmpty()){
            boardId= taskLists!![0].getBoard()!!.getId()
        }
        val listAdapter =
            TaskAdapterInsideBoard(requireContext(),mItemArray!!, true, R.layout.column_item, R.id.item_layout, true,supportFragmentManager!!,boardName!!,boardId,presenter!!,rol)
        mDragListView!!.setAdapter(listAdapter, true)
        mDragListView!!.setCanDragHorizontally(false)
    }

    companion object {
        fun instance(boardsView: ITaskListContract.ViewNormal,taskLists: List<TaskList>,boardName:String): ListFragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList("taskLists",taskLists as ArrayList<TaskList>)
            bundle.putString("boardName",boardName)
            return ListFragment(boardsView).apply { arguments = bundle }
        }
    }
}