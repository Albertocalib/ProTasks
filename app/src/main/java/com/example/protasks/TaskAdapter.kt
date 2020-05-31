package com.example.protasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Task


class TaskAdapter(private val tasks : List<Task>?, private val view:Int) :RecyclerView.Adapter<TaskAdapter.ViewHolderTask>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskAdapter.ViewHolderTask {
        val view: View =
            LayoutInflater.from(parent.context).inflate(view, parent, false)
        return ViewHolderTask(view)
    }

    override fun getItemCount(): Int {
        return tasks!!.size
    }

    override fun onBindViewHolder(holder: TaskAdapter.ViewHolderTask, position: Int) {
        holder.taskName.text=tasks!![position].getTitle()
        holder.listName.text=tasks[position].getTaskList().getTitle()
        if (holder.listName.text !=null){
            holder.listName.visibility=View.VISIBLE
        }

    }

    class ViewHolderTask(v: View) : RecyclerView.ViewHolder(v) {
        var taskName: TextView
        var listName: TextView
        var view:View=v

        init {
            taskName = view.findViewById(R.id.titleTask)
            listName = view.findViewById(R.id.listName)
        }
    }
    fun getTasks():List<Task>{
        return this.tasks!!
    }
}