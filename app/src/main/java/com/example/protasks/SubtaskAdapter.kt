package com.example.protasks

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Rol
import com.example.protasks.models.Task
import com.example.protasks.presenters.TaskPresenter


class SubtaskAdapter(
        private val subtasks: List<Task?>?,
        private val taskPresenter: TaskPresenter?,
        private val task: Task?,
        private val context: Context,
        private val supportFragmentManager: FragmentManager,
        private val taskDialogExtend: TaskDialogExtend,
        private val boardId: Long,
        private val boardName: String,
        private val rol: Rol?
) : RecyclerView.Adapter<SubtaskAdapter.ViewHolderSubtask>() {
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolderSubtask {
        val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.subtask, parent, false)
        return ViewHolderSubtask(view)
    }

    fun getSubTasksSelected(): ArrayList<Task> {
        val subtasks_sel = ArrayList<Task>()
        for (elem in subtasks!!) {
            if (elem!!.isSelected()) {
                subtasks_sel.add(elem)
            }
        }
        return subtasks_sel

    }

    override fun getItemCount(): Int {
        return subtasks!!.size
    }

    override fun onBindViewHolder(holder: ViewHolderSubtask, position: Int) {
        val subtask = subtasks!![position]
        holder.title.text = subtask!!.getTitle()!!
        holder.subTask = subtask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //TODO Poner el color según el estado de la tarea
            holder.checkbox.buttonTintList = ColorStateList.valueOf(Color.BLUE)
        }

        holder.layout.setOnClickListener {
            if (subtask.isSelected()) {
                subtask.setSelected(!subtask.isSelected())
                it.setBackgroundColor(if (subtask.isSelected()) ContextCompat.getColor(this.context,
                        R.color.colorPrimary
                ) else Color.TRANSPARENT)
                taskDialogExtend.updateVisibilityDeleteSubtasks()
            } else {
                subtask.setParentTask(task!!)
                val dialog =
                        TaskDialogExtend(
                            subtask,
                            boardName,
                            boardId,
                            supportFragmentManager,
                            null,
                            rol
                        )
                val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                dialog.show(ft, "TaskExtendDialog")
            }
        }
        holder.layout.setOnLongClickListener {
            subtask.setSelected(!subtask.isSelected())
            it.setBackgroundColor(if (subtask.isSelected()) ContextCompat.getColor(this.context,
                    R.color.colorPrimary
            ) else Color.TRANSPARENT)
            taskDialogExtend.updateVisibilityDeleteSubtasks()
            true
        }


    }

    class ViewHolderSubtask(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.subtask_title)
        var subTask: Task? = null
        var checkbox: CheckBox = v.findViewById(R.id.checkBox)
        val layout: LinearLayout = v.findViewById(R.id.linear_layout_subtasks)

    }

}