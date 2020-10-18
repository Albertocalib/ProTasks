package com.example.protasks

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.example.protasks.models.Task
import com.google.android.material.textfield.TextInputEditText


class TaskDialogExtend(private val task:Task) : DialogFragment() {
    var name: TextInputEditText? = null
    var description: TextInputEditText? = null
    var toolbar:Toolbar?=null
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
        toolbar =v.findViewById(R.id.toolbar)
        toolbar!!.setNavigationOnClickListener { dismiss() }


        name!!.setText(task.getTitle())
        if (task.getDescription()!=null){
            description!!.setText(task.getDescription())
        }
        return v
    }
}