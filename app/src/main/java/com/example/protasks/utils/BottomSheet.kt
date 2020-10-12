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
    private val presenter: TaskListPresenter
) : BottomSheetDialogFragment() {
    var name: TextInputEditText? = null
    var description: TextInputEditText? = null
    var createTaskBtn: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.task_dialog, container, false)
        name = v.findViewById(R.id.taskname)
        description = v.findViewById(R.id.taskDescription)
        createTaskBtn = v.findViewById(R.id.createTaskBtn)
        createTaskBtn!!.isEnabled = false
        createTaskBtn!!.background.alpha = 150
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val notEmpty = name!!.text.toString() != ""
                if (notEmpty){
                    createTaskBtn!!.background.alpha = 255
                }else{
                    createTaskBtn!!.background.alpha = 150
                }
                createTaskBtn!!.isEnabled = notEmpty

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
        createTaskBtn!!.setOnClickListener {
            presenter.createTask(
                boardName,
                name!!.text.toString(),
                listName,
                description!!.text.toString()
            )
            dismiss()
        }
        return v
    }
}