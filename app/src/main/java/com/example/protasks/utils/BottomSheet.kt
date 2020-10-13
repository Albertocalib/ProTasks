package com.example.protasks.utils

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.protasks.R
import com.example.protasks.presenters.TaskListPresenter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class BottomSheet(
    private val boardName: String,
    private val listName: String,
    private val presenter: TaskListPresenter,
    private val taskSheet:Boolean
) : BottomSheetDialogFragment() {
    var name: TextInputEditText? = null
    var description: TextInputEditText? = null
    var createBtn: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewId:Int
        val nameFieldId:Int
        val buttonNameId:Int
        if (taskSheet){
            viewId=R.layout.task_dialog
            nameFieldId=R.id.taskname
            buttonNameId=R.id.createTaskBtn
        }else{
            viewId=R.layout.tasklist_dialog
            nameFieldId=R.id.taskListName
            buttonNameId=R.id.createTaskListBtn
        }
        val v: View = inflater.inflate(viewId, container, false)
        name = v.findViewById(nameFieldId)
        if (taskSheet) {
            description = v.findViewById(R.id.taskDescription)
        }
        createBtn = v.findViewById(buttonNameId)
        createBtn!!.isEnabled = false
        createBtn!!.background.alpha = 150
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val notEmpty = name!!.text.toString() != ""
                if (notEmpty){
                    createBtn!!.background.alpha = 255
                }else{
                    createBtn!!.background.alpha = 150
                }
                createBtn!!.isEnabled = notEmpty

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // I don't want to do anything beforeTextChanged
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                // I don't want to do anything onTextChanged
            }
        }
        name!!.addTextChangedListener(textWatcher)
        createBtn!!.setOnClickListener {
            if (taskSheet){
                presenter.createTask(
                    boardName,
                    name!!.text.toString(),
                    listName,
                    description!!.text.toString()
                )
            }else{
                presenter.createTaskList(
                    boardName,
                    name!!.text.toString()
                )
            }

            dismiss()
        }
        return v

    }
}