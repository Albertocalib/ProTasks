package com.example.protasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.util.Pair
import com.example.protasks.models.Board
import com.example.protasks.models.Task
import com.woxthebox.draglistview.DragItemAdapter
import java.util.*

internal class TaskAdapterInsideBoard(
    list: ArrayList<Triple<Long, Task,Boolean>>,
    private val listMode: Boolean,
    private val mLayoutId: Int,
    private val mGrabHandleId: Int,
    private val mDragOnLongPress: Boolean
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
        if (mItemList[position]!!.third && listMode){
            holder.cardViewTask.visibility=View.GONE
            holder.mText.visibility = View.GONE
            holder.image.visibility=View.GONE
            holder.listText.visibility = View.VISIBLE
            holder.cardView.visibility = View.VISIBLE
        }
        holder.itemView.tag = mItemList[position]
    }

    override fun getUniqueItemId(position: Int): Long {
        return mItemList[position]!!.first
    }

    internal inner class ViewHolder(itemView: View) :
        DragItemAdapter.ViewHolder(itemView, mGrabHandleId, mDragOnLongPress) {
        var mText: TextView
        var listText: TextView
        var cardView: CardView
        var cardViewTask: CardView
        var image:ImageView
        override fun onItemClicked(view: View) {
            Toast.makeText(view.context, "Item clicked", Toast.LENGTH_SHORT).show()
        }

        override fun onItemLongClicked(view: View): Boolean {
            Toast.makeText(view.context, "Item long clicked", Toast.LENGTH_SHORT).show()
            return true
        }

        init {
            mText = itemView.findViewById<View>(R.id.text) as TextView
            listText = itemView.findViewById<View>(R.id.text2) as TextView
            cardView = itemView.findViewById<View>(R.id.cardTitle) as CardView
            cardViewTask = itemView.findViewById(R.id.card2) as CardView
            image =itemView.findViewById(R.id.imageTaskInside) as ImageView

        }
    }

    init {
        itemList = list
    }
}