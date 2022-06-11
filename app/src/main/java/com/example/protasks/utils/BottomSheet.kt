package com.example.protasks.utils

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.protasks.R
import com.example.protasks.models.Board
import com.example.protasks.models.Rol
import com.example.protasks.models.Task
import com.example.protasks.presenters.board.BoardPresenter
import com.example.protasks.presenters.IPresenter
import com.example.protasks.presenters.tasklist.TaskListPresenter
import com.example.protasks.presenters.task.TaskPresenter
import com.example.protasks.presenters.tasklist.ITaskListContract
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import top.defaults.colorpicker.ColorPickerPopup
import java.util.ArrayList


class BottomSheet(
    private val boardName: String,
    private val listName: String,
    private val presenter: IPresenter,
    private val sheetMode: String
) : BottomSheetDialogFragment(),ITaskListContract.ViewBottomSheet {
    var name: TextInputEditText? = null
    var description: TextInputEditText? = null
    var createBtn: Button? = null
    var selectColor: ImageButton? = null
    var color: String? = null
    var boards: HashMap<String, Board>? = HashMap()
    var spinnerBoards: Spinner? = null
    var spinnerLists: Spinner? = null
    var task: Task? = null
    var board:Board? = null
    var radioGroup:RadioGroup?=null
    private val anyElementSelected=getString(R.string.no_elements)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewId: Int
        var nameFieldId = 0
        var buttonNameId = 0
        when (sheetMode) {
            "task","subtask" -> {
                viewId = R.layout.task_dialog
                nameFieldId = R.id.taskname
                buttonNameId = R.id.createTaskBtn
            }
            "taskList" -> {
                viewId = R.layout.tasklist_dialog
                nameFieldId = R.id.taskListName
                buttonNameId = R.id.createTaskListBtn
            }
            "menu", "menu_task" -> {
                viewId = R.layout.options_task_tasklist_dialog
            }"user"->{
                viewId = R.layout.user_dialog
                nameFieldId = R.id.usernameOrEmail
                buttonNameId = R.id.AddUserButton
            }"filter"->{
                viewId = R.layout.tag_dialog
                nameFieldId = R.id.tagName
                buttonNameId = R.id.createTagBtn
            }
            else -> {
                viewId = R.layout.tag_dialog
                nameFieldId = R.id.tagName
                buttonNameId = R.id.createTagBtn
            }
        }
        val v: View = inflater.inflate(viewId, container, false)
        if (sheetMode.startsWith("menu")) {
            val delOpt = v.findViewById<TextView>(R.id.delete)
            delOpt.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("¿Estás seguro que desea eliminarlo?")
                    .setCancelable(false)
                    .setPositiveButton("Aceptar") { dialog, id ->
                        dismiss()
                        dialog.dismiss()
                        if (sheetMode == "menu") {
                            (presenter as TaskListPresenter).deleteTaskList(
                                boardName,
                                listName
                            )
                        } else {
                            (presenter as TaskListPresenter).deleteTask(
                                task!!.getId()!!,boardName
                            )
                        }
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        // Dismiss the dialog
                        dismiss()
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
            val moveOpt = v.findViewById<TextView>(R.id.move)
            moveOpt.setOnClickListener {
                if (sheetMode == "menu") {
                    dialogCopyOrMoveTaskList("move", inflater)
                } else {
                    dialogCopyOrMoveTask("move", inflater)
                }
            }
            val copyOpt = v.findViewById<TextView>(R.id.copy)
            copyOpt.setOnClickListener {
                if (sheetMode == "menu") {
                    dialogCopyOrMoveTaskList("copy", inflater)
                } else {
                    dialogCopyOrMoveTask("copy", inflater)
                }
            }
        } else {
            name = v.findViewById(nameFieldId)
            if (sheetMode == "task" || sheetMode == "subtask")  {
                description = v.findViewById(R.id.taskDescription)
            } else if (sheetMode == "tag") {
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

            } else if (sheetMode == "user"){
                radioGroup = v.findViewById(R.id.RadioGroup)
            }
            createBtn = v.findViewById(buttonNameId)
            createBtn!!.isEnabled = false
            createBtn!!.background.alpha = 150
            val textWatcher: TextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val notEmpty = name!!.text.toString() != ""
                    if (notEmpty) {
                        createBtn!!.background.alpha = 255
                    } else {
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
                    "subtask"->{
                        (presenter as TaskPresenter).createSubTask(task!!,
                                name!!.text.toString(),
                                description!!.text.toString()
                        )
                    }
                    "user"->{
                        val idChecked = radioGroup!!.checkedRadioButtonId
                        val radioChecked = v.findViewById<RadioButton>(idChecked)
                        val role:Rol = when (radioChecked.text.toString()) {
                            "Administrador" -> {
                                Rol.ADMIN
                            }
                            "Invitado" -> {
                                Rol.USER
                            }
                            else -> {
                                Rol.WATCHER
                            }
                        }
                        (presenter as BoardPresenter).addUserToBoard(
                            board!!.getId(),
                            name!!.text.toString(),
                            role

                        )
                    }
                    else -> {
                        (presenter as TaskPresenter).createTag(
                            boardName,
                            name!!.text.toString(),
                            color!!
                        )


                    }
                }

                dismiss()
            }
        }
        return v

    }

    override fun setBoard(list: List<Board>) {
        val boardListName = ArrayList<String>()
        boardListName.add(anyElementSelected)
        for (e in list) {
            boardListName.add(e.getName()!!)
            boards!![e.getName()!!] = e
        }
        val myAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_list_item_1, boardListName
        )
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBoards!!.adapter = myAdapter
    }

    fun dialogCopyOrMoveTaskList(type: String, inflater: LayoutInflater) {
        (presenter as TaskListPresenter).getBoards(this)
        val builder = AlertDialog.Builder(context)
        val myView: View =
            inflater.inflate(R.layout.move_tasklist_dialog, null)
        builder.setView(myView)
        spinnerBoards =
            myView.findViewById<View>(R.id.spinnerBoardsToMove) as Spinner


        builder.setMessage("Elija el tablero de destino")
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dismiss()
                dialog.dismiss()
                val nameSelected = spinnerBoards!!.selectedItem.toString()
                if (nameSelected != anyElementSelected) {
                    if (type == "move" && nameSelected != boardName) {
                        presenter.moveTaskList(
                            boards!![boardName]!!,
                            listName,
                            boards!![nameSelected]!!.getId()
                        )
                    }
                    if (type == "copy") {
                        presenter.copyTaskList(
                            boards!![boardName]!!,
                            listName,
                            boards!![nameSelected]!!.getId()
                        )
                    }
                }

            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                // Dismiss the dialog
                dismiss()
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    fun dialogCopyOrMoveTask(type: String, inflater: LayoutInflater) {

        (presenter as TaskListPresenter).getBoards(this)
        val builder = AlertDialog.Builder(context)
        val myView: View =
            inflater.inflate(R.layout.move_task_dialog, null)
        builder.setView(myView)
        spinnerBoards =
            myView.findViewById<View>(R.id.spinnerBoardTask) as Spinner
        spinnerLists =
            myView.findViewById<View>(R.id.spinnerTaskList) as Spinner
        val listNamesTaskList = ArrayList<String>()
        listNamesTaskList.add(anyElementSelected)
        val myAdapterTaskList = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_list_item_1, listNamesTaskList
        )
        myAdapterTaskList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLists!!.adapter = myAdapterTaskList
        spinnerBoards!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i("TASKSPINNER", "Nothing Selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val listNamesTaskL = getTaskListByBoardSpinner(spinnerBoards!!.selectedItem.toString())
                myAdapterTaskList.clear()
                myAdapterTaskList.addAll(listNamesTaskL)
                myAdapterTaskList.notifyDataSetChanged()
            }

        }

        builder.setMessage("Elija el tablero de destino")
            .setCancelable(false)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dismiss()
                dialog.dismiss()
                val nameSelected = spinnerBoards!!.selectedItem.toString()
                val nameSelectedList = spinnerLists!!.selectedItem.toString()
                if (nameSelected != anyElementSelected && nameSelectedList!= anyElementSelected ) {
                    if (type == "move" && (nameSelected != boardName || nameSelectedList!=listName)) {
                        presenter.moveTask(
                            boards!![boardName]!!,
                            nameSelectedList,
                            task!!.getId(),
                            boards!![nameSelected]!!.getId()
                        )
                    }
                    if (type == "copy") {
                        presenter.copyTask(
                            boards!![boardName]!!,
                            nameSelectedList,
                            task!!.getId(),
                            boards!![nameSelected]!!.getId()
                        )
                    }
                }

            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                // Dismiss the dialog
                dismiss()
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()






    }
    fun getTaskListByBoardSpinner(boardName: String): ArrayList<String> {
        val listName = ArrayList<String>()
        listName.add(anyElementSelected)
        if (boardName!=anyElementSelected) {
            val taskList = boards?.get(boardName)?.getTaskLists()!!
            for (list in taskList) {
                listName.add(list!!.getTitle()!!)
            }
        }
        return listName
    }

    override fun onResponseFailure(t: Throwable?) {
        Log.e("BOTTOMSHEET", t!!.message!!)
        Toast.makeText(context, getString(R.string.communication_error), Toast.LENGTH_LONG).show()

    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}