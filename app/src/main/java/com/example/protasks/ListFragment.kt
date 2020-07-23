/*
  Copyright 2014 Magnus Woxblom
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.example.protasks

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.protasks.models.TaskList
import com.example.protasks.presenters.TaskListPresenter
import com.woxthebox.draglistview.DragItem
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.DragListView.DragListListenerAdapter
import com.woxthebox.draglistview.swipe.ListSwipeHelper.OnSwipeListenerAdapter
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.woxthebox.draglistview.swipe.ListSwipeItem.SwipeDirection
import java.util.*

class ListFragment(private val taskLists: List<TaskList>, private val presenter: TaskListPresenter) : Fragment() {
    private var mItemArray: ArrayList<Pair<Long, String>>? =
        null
    private var mDragListView: DragListView? = null
    private var mRefreshLayout: MySwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.list_layout, container, false)
        mRefreshLayout =
            view.findViewById<View>(R.id.swipe_refresh_layout) as MySwipeRefreshLayout
        mDragListView = view.findViewById<View>(R.id.drag_list_view) as DragListView
        mDragListView!!.recyclerView.isVerticalScrollBarEnabled = true
        mDragListView!!.isDragEnabled = true
        mDragListView!!.setDragListListener(object : DragListListenerAdapter() {
            override fun onItemDragStarted(position: Int) {
                mRefreshLayout!!.isEnabled = false
                //Toast.makeText(mDragListView.getContext(), "Start - position: " + position, Toast.LENGTH_SHORT).show();
            }

            override fun onItemDragEnded(fromPosition: Int, toPosition: Int) {
                mRefreshLayout!!.isEnabled = true
                if (fromPosition != toPosition) {
                    //Toast.makeText(mDragListView.getContext(), "End - position: " + toPosition, Toast.LENGTH_SHORT).show();
                }
            }
        })
        mItemArray =
            ArrayList<Pair<Long, String>>()
        for (list in taskLists){
            val tasks=list.getTasks()!!.sortedWith(compareBy { it!!.getPosition() })
            for (task in tasks) {
                mItemArray!!.add(
                    Pair(
                        task!!.getId() as Long,
                        task.getTitle()
                    )
                )
            }

        }
        mRefreshLayout!!.setScrollingView(mDragListView!!.recyclerView)
        mRefreshLayout!!.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        mRefreshLayout!!.setOnRefreshListener(OnRefreshListener {
            mRefreshLayout!!.postDelayed(
                Runnable { mRefreshLayout!!.setRefreshing(false) }, 2000
            )
        })
        mDragListView!!.setSwipeListener(object : OnSwipeListenerAdapter() {
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
        setupListRecyclerView()
        return view
    }


    private fun setupListRecyclerView() {
        mDragListView!!.setLayoutManager(LinearLayoutManager(context))
        val listAdapter =
            TaskAdapterInsideBoard(mItemArray!!, R.layout.column_item, R.id.item_layout, true)
        mDragListView!!.setAdapter(listAdapter, true)
        mDragListView!!.setCanDragHorizontally(false)
        mDragListView!!.setCustomDragItem(
            MyDragItem(
                context,
                R.layout.column_item
            )
        )
    }


    private class MyDragItem internal constructor(
        context: Context?,
        layoutId: Int
    ) :
        DragItem(context, layoutId) {
        override fun onBindDragView(
            clickedView: View,
            dragView: View
        ) {
            val text =
                (clickedView.findViewById<View>(R.id.text) as TextView).text
            (dragView.findViewById<View>(R.id.text) as TextView).text = text
            dragView.findViewById<View>(R.id.item_layout)
                .setBackgroundColor(dragView.resources.getColor(R.color.colorPrimary))
        }
    }

    companion object {
        fun newInstance(taskLists: List<TaskList>,presenter:TaskListPresenter): ListFragment {
            return ListFragment(taskLists,presenter)
        }
    }
}