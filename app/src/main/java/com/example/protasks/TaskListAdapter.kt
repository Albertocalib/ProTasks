package com.example.protasks

import android.util.Base64;
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Board
import com.example.protasks.models.Task


class TaskListAdapter(private val tasks : List<Task>?, private val view:Int) :RecyclerView.Adapter<TaskListAdapter.ViewHolderTask>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskListAdapter.ViewHolderTask {
        val view: View =
            LayoutInflater.from(parent.context).inflate(view, parent, false)
        return ViewHolderTask(view)
    }

    override fun getItemCount(): Int {
        return tasks!!.size
    }

    override fun onBindViewHolder(holder: TaskListAdapter.ViewHolderTask, position: Int) {
        holder.taskName.text=tasks!![position].getTitle()
    }

    class ViewHolderTask(v: View) : RecyclerView.ViewHolder(v) {
        var taskName: TextView
        var view:View=v

        init {
            taskName = view.findViewById(R.id.boardName)
        }
    }
    fun getTasks():List<Task>{
        return this.tasks!!
    }
}