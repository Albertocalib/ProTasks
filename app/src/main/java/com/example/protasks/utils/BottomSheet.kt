package com.example.protasks.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.example.protasks.R
import com.example.protasks.presenters.IPresenter
import com.example.protasks.presenters.TaskListPresenter
import com.example.protasks.presenters.TaskPresenter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import top.defaults.colorpicker.ColorPickerPopup


class BottomSheet(
    private val boardName: String,
    private val listName: String,
    private val presenter: IPresenter,
    private val sheetMode: String
) : BottomSheetDialogFragment() {
    var name: TextInputEditText? = null
    var description: TextInputEditText? = null
    var createBtn: Button? = null
    var selectColor: ImageButton? = null
    var color: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewId:Int
        val nameFieldId:Int
        val buttonNameId:Int
        when (sheetMode) {
            "task" -> {
                viewId=R.layout.task_dialog
                nameFieldId=R.id.taskname
                buttonNameId=R.id.createTaskBtn
            }
            "taskList" -> {
                viewId=R.layout.tasklist_dialog
                nameFieldId=R.id.taskListName
                buttonNameId=R.id.createTaskListBtn
            }
            else -> {
                viewId=R.layout.tag_dialog
                nameFieldId=R.id.tagName
                buttonNameId=R.id.createTagBtn
            }
        }
        val v: View = inflater.inflate(viewId, container, false)
        name = v.findViewById(nameFieldId)
        if (sheetMode=="task") {
            description = v.findViewById(R.id.taskDescription)
        }else if (sheetMode=="tag"){
            selectColor = v.findViewById(R.id.imageColorTag)
            val colorDrawable = selectColor!!.background as ColorDrawable?
            color = java.lang.String.format("#%06X", 0xFFFFFF and colorDrawable!!.color)
            selectColor!!.setOnClickListener {
                ColorPickerPopup.Builder(requireActivity().baseContext)
                    .initialColor(Color.GRAY)
                    .enableBrightness(true)
                    .enableAlpha(true)
                    .okTitle("Aceptar")
                    .cancelTitle("Cancelar")
                    .showIndicator(true)
                    .showValue(false)
                    .build()
                    .show(view, object : ColorPickerPopup.ColorPickerObserver() {
                        override fun onColorPicked(col: Int) {
                            color = java.lang.String.format("#%06X", 0xFFFFFF and col)
                            selectColor!!.setBackgroundColor(col)
                        }
                    })
            }

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
            when (sheetMode) {
                "task" -> {
                    (presenter as TaskListPresenter).createTask(
                        boardName,
                        name!!.text.toString(),
                        listName,
                        description!!.text.toString()
                    )
                }
                "taskList" -> {
                    (presenter as TaskListPresenter).createTaskList(
                        boardName,
                        name!!.text.toString()
                    )
                }
                else -> {
                    (presenter as TaskPresenter).createTag(boardName,name!!.text.toString(),color!!)


                }
            }

            dismiss()
        }
        return v

    }
}