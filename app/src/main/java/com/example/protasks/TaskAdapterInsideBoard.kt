package com.example.protasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.protasks.models.Task
import com.example.protasks.presenters.IPresenter
import com.example.protasks.presenters.TaskListPresenter
import com.example.protasks.utils.BottomSheet
import com.woxthebox.draglistview.DragItemAdapter
import java.util.*


class TaskAdapterInsideBoard(
    list: ArrayList<Triple<Long, Task, Boolean>>,
    private val listMode: Boolean,
    private val mLayoutId: Int,
    private val mGrabHandleId: Int,
    private val mDragOnLongPress: Boolean,
    private val supportFragmentManager: FragmentManager,
    private val boardName: String,
    private val boardId: Long,
    private val presenter:TaskListPresenter
) :
    DragItemAdapter<Triple<Long, Task, Boolean>, TaskAdapterInsideBoard.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(mLayoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        val text = mItemList[position]!!.second.getTitle()
        holder.mText!!.text = text
        holder.listText!!.text = mItemList[position]!!.second.getTaskList().getTitle()
        holder.task = mItemList[position]!!.second
        if (mItemList[position]!!.third && listMode) {
            holder.cardViewTask!!.visibility = View.GONE
            holder.mText!!.visibility = View.GONE
            holder.image!!.visibility = View.GONE
            holder.listText!!.visibility = View.VISIBLE
            holder.cardView!!.visibility = View.VISIBLE
            holder.task = null
        }
        holder.itemView.tag = mItemList[position]
    }

    override fun getUniqueItemId(position: Int): Long {
        return mItemList[position]!!.first
    }

    inner class ViewHolder(itemView: View) :
        DragItemAdapter.ViewHolder(itemView, mGrabHandleId, mDragOnLongPress) {
        var mText: TextView? = null
        var listText: TextView? = null
        var cardView: CardView? = null
        var cardViewTask: CardView? = null
        var image: ImageView? = null
        var task: Task? = null
        private var optionsMenu: ImageButton? = null

        init {
            mText = itemView.findViewById<View>(R.id.text) as TextView
            listText = itemView.findViewById<View>(R.id.text2) as TextView
            cardView = itemView.findViewById<View>(R.id.cardTitle) as CardView
            cardViewTask = itemView.findViewById(R.id.card2) as CardView
            image = itemView.findViewById(R.id.imageTaskInside) as ImageView
            optionsMenu = itemView.findViewById<View>(R.id.options) as ImageButton
            optionsMenu!!.setOnClickListener {
                val bottomSheet = BottomSheet(boardName, listText!!.text.toString(), presenter, "menu_task")
                bottomSheet.task=task
                bottomSheet.show(supportFragmentManager, "bottomSheet")

            }
        }

        override fun onItemClicked(view: View) {
            if (task != null) {
                Toast.makeText(view.context, "Item clicked", Toast.LENGTH_SHORT).show()
                val dialog =
                    TaskDialogExtend(task!!, boardName, boardId, supportFragmentManager, this)
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                dialog.show(ft, "TaskExtendDialog")
            }

        }

        override fun onItemLongClicked(view: View): Boolean {
            Toast.makeText(view.context, "Item long clicked", Toast.LENGTH_SHORT).show()
            return true
        }

        fun updateTask(t: Task) {
            task = t
            mText!!.text = task!!.getTitle()
        }

    }

    init {
        itemList = list
    }
}