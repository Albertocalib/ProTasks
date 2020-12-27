package com.example.protasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.protasks.models.Task
import com.woxthebox.draglistview.DragItemAdapter
import java.util.*


class TaskAdapterInsideBoard(
    list: ArrayList<Triple<Long, Task,Boolean>>,
    private val listMode: Boolean,
    private val mLayoutId: Int,
    private val mGrabHandleId: Int,
    private val mDragOnLongPress: Boolean,
    private val supportFragmentManager:FragmentManager,
    private val boardName:String,
    private val boardId:Long
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
        holder.mText.text = text
        holder.listText.text = mItemList[position]!!.second.getTaskList().getTitle()
        holder.task = mItemList[position]!!.second
        if (mItemList[position]!!.third && listMode){
            holder.cardViewTask.visibility=View.GONE
            holder.mText.visibility = View.GONE
            holder.image.visibility=View.GONE
            holder.listText.visibility = View.VISIBLE
            holder.cardView.visibility = View.VISIBLE
            holder.task=null
        }
        holder.itemView.tag = mItemList[position]
    }

    override fun getUniqueItemId(position: Int): Long {
        return mItemList[position]!!.first
    }

    inner class ViewHolder(itemView: View) :
        DragItemAdapter.ViewHolder(itemView, mGrabHandleId, mDragOnLongPress) {
        var mText: TextView = itemView.findViewById<View>(R.id.text) as TextView
        var listText: TextView = itemView.findViewById<View>(R.id.text2) as TextView
        var cardView: CardView = itemView.findViewById<View>(R.id.cardTitle) as CardView
        var cardViewTask: CardView = itemView.findViewById(R.id.card2) as CardView
        var image:ImageView = itemView.findViewById(R.id.imageTaskInside) as ImageView
        var task:Task?=null
        override fun onItemClicked(view: View) {
            if (task!=null){
                Toast.makeText(view.context, "Item clicked", Toast.LENGTH_SHORT).show()
                val dialog = TaskDialogExtend(task!!,boardName,boardId,supportFragmentManager,this)
                val ft: FragmentTransaction =  supportFragmentManager.beginTransaction()
                dialog.show(ft,"TaskExtendDialog")
            }

        }

        override fun onItemLongClicked(view: View): Boolean {
            Toast.makeText(view.context, "Item long clicked", Toast.LENGTH_SHORT).show()
            return true
        }
        fun updateTask(t:Task){
            task=t
            mText.text=task!!.getTitle()
        }

    }

    init {
        itemList = list
    }
}