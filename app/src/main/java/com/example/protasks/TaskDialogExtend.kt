package com.example.protasks

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Task
import com.example.protasks.models.User
import com.example.protasks.presenters.TaskPresenter
import com.example.protasks.views.ITasksView
import com.google.android.material.textfield.TextInputEditText


class TaskDialogExtend(private val task: Task, private val boardName: String) : DialogFragment(),
    ITasksView {
    var name: TextInputEditText? = null
    var description: TextInputEditText? = null
    var toolbar: Toolbar? = null
    var recyclerViewAssignments: RecyclerView? = null
    var layoutManager: GridLayoutManager? = GridLayoutManager(context, 3)
    var taskPresenter: TaskPresenter? = null
    var imageTask: ImageView? = null
    var nameBoardList: TextView? = null
    var moreThanThreeButton: CardView? = null
    var moreThanThreeText: TextView? = null
    var usersList: List<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog = dialog!!
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.setLayout(width, height)
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        super.onCreateView(inflater, parent, state)
        val v: View = inflater.inflate(R.layout.task_dialog_extend, parent, false)
        name = v.findViewById(R.id.taskname)
        description = v.findViewById(R.id.taskDescription)
        toolbar = v.findViewById(R.id.toolbar)
        toolbar!!.setNavigationOnClickListener { dismiss() }
        taskPresenter = TaskPresenter(this, requireContext())

        name!!.setText(task.getTitle())
        if (task.getDescription() != null) {
            description!!.setText(task.getDescription())
        }
        recyclerViewAssignments = v.findViewById(R.id.recycler_assignment_tags)
        recyclerViewAssignments!!.layoutManager = layoutManager
        taskPresenter!!.getUsers(task.getId()!!)
        imageTask = v.findViewById(R.id.image_task)
        val photos = task.getPhotos()
        if (photos == null || photos.isEmpty()) {
            imageTask!!.visibility = View.GONE
        } else {
            val imageBytes = Base64.decode(photos[0], Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imageTask!!.setImageBitmap(decodedImage)
        }

        nameBoardList = v.findViewById(R.id.list_board_name)
        val comp = boardName + " en la lista " + task.getTaskList().getTitle()
        nameBoardList!!.text = comp
        moreThanThreeButton = v.findViewById(R.id.more_than_three_assignments)
        moreThanThreeText = v.findViewById(R.id.more_than_text)
        moreThanThreeText!!.setOnClickListener {

        }
        return v
    }

    override fun setTasks(tasks: List<Task>) {
        TODO("Not yet implemented")
    }

    override fun setAssignments(users: List<User>) {
        usersList = users
        if (users.size > 3) {
            val usersReduced = users.subList(0, 3)
            recyclerViewAssignments!!.adapter = AssignmentsAdapter(usersReduced)
            val more = "+" + (users.size - usersReduced.size)
            moreThanThreeText!!.text = more
        } else {
            recyclerViewAssignments!!.adapter = AssignmentsAdapter(users)
            moreThanThreeButton!!.visibility = View.GONE

        }
    }
}