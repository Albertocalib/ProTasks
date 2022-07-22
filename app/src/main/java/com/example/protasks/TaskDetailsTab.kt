package com.example.protasks

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.*
import com.example.protasks.presenters.task.ITaskContract
import com.example.protasks.presenters.task.TaskPresenter
import com.example.protasks.utils.*
import com.example.protasks.utils.DatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class TaskDetailsTab(private val t: Toolbar,
                     private var task: Task,
                     private val boardName: String,
                     private val boardId: Long,
                     private val fragmentMgr: FragmentManager,
                     private val viewHolder: TaskAdapterInsideBoard.ViewHolder?,
                     val rol: Rol?) : Fragment(), ITaskContract.View {
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
    var addUsersButton: ImageButton? = null
    var boardUserList: List<User>? = ArrayList()
    var recyclerViewTags: RecyclerView? = null
    var layoutManagerTags: GridLayoutManager? = GridLayoutManager(context, 5)
    var tagList: List<Tag>? = null
    var addTagsButton: ImageButton? = null
    var boardTagList: List<Tag>? = ArrayList()
    var dateEnd: TextView? = null
    var attachFiles: TextView? = null
    var resultLauncher: ActivityResultLauncher<Intent>?=null
    var recyclerViewAttachments: RecyclerView? = null
    var layoutManagerAttachments = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    var viewAttachments: LinearLayout? = null
    var recyclerViewSubtasks: RecyclerView? = null
    var layoutManagerSubtasks = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    var buttonAddSubtasks: LinearLayout? = null
    var deleteSubtasks: ImageView? = null
    var subtasksSelected:ArrayList<Task> = ArrayList()
    var spinnerPriorityList: ArrayList<SpinnerImage> = ArrayList()
    private val notYetImplemented = "Not yet implemented"

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uris = HashSet<Uri>()
                val data= result.data!!.clipData
                if (data!=null) {
                    for (d: Int in 0 until data.itemCount) {
                        uris.add(data.getItemAt(d).uri)
                    }
                }else{
                    val data2 = result.data!!.data
                    uris.add(data2!!)
                }
                taskPresenter!!.addAttachedFile(uris,task.getId())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        super.onCreateView(inflater, parent, state)
        val v: View = inflater.inflate(R.layout.task_dialog_extend, parent, false)
        name = v.findViewById(R.id.taskname)
        description = v.findViewById(R.id.taskDescription)

        val preference: PreferencesManager = Preference(requireContext())
        taskPresenter = TaskPresenter(this, preference,requireContext().contentResolver)

        name!!.setText(task.getTitle())
        if (task.getDescription() != null) {
            description!!.setText(task.getDescription())
        }
        name!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (name!!.text.toString() != "") {
                    taskPresenter!!.updateTitle(task.getId()!!, name!!.text.toString())
                }
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
        })
        description!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                taskPresenter!!.updateDescription(task.getId()!!, description!!.text.toString())

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
        })
        recyclerViewAssignments = v.findViewById(R.id.recycler_assignment_tags)
        recyclerViewAssignments!!.layoutManager = layoutManager
        taskPresenter!!.getUsers(task.getId()!!)
        imageTask = v.findViewById(R.id.image_task)
        val photos = task.getPhotos()
        if (photos.isEmpty()) {
            imageTask!!.visibility = View.GONE
        } else {
            imageTask!!.setImageBitmap(photos[0])
        }

        nameBoardList = v.findViewById(R.id.list_board_name)
        val comp = if (task.getParentTask()!=null){
            boardName + " en la lista " + task.getParentTask()!!.getTaskList().getTitle()
        }else{
            boardName + " en la lista " + task.getTaskList().getTitle()
        }
        nameBoardList!!.text = comp
        moreThanThreeButton = v.findViewById(R.id.more_than_three_assignments)
        moreThanThreeText = v.findViewById(R.id.more_than_text)
        moreThanThreeText!!.setOnClickListener {

        }
        taskPresenter!!.getUsersInBoard(boardId)
        taskPresenter!!.getTagsInBoard(boardId)
        addUsersButton = v.findViewById(R.id.add_assignment)
        addUsersButton!!.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            val items = arrayOfNulls<CharSequence>(boardUserList!!.size)
            val itemsChecked = BooleanArray(items.size)
            val usersListIds = HashSet<Long>()
            usersList!!.forEach {
                usersListIds.add(it.getId()!!)
            }
            boardUserList!!.forEachIndexed { i, user ->
                val name = user.getName() + ' ' + user.getSurname()
                items[i] = name
                itemsChecked[i] = usersListIds.contains(user.getId()!!)
            }
            val builderObj = builder.setTitle("Asignar Personas")
                .setMultiChoiceItems(
                    items, itemsChecked
                ) { _, which, isChecked ->
                    if (isChecked) {
                        // Guardar indice seleccionado
                        val userId = boardUserList!![which]
                        taskPresenter!!.addAssignment(task.getId()!!, userId.getId()!!)
                    } else {
                        // Remover indice sin selección
                        val userId = boardUserList!![which]
                        taskPresenter!!.removeAssignment(task.getId()!!, userId.getId()!!, false)
                    }
                }.setPositiveButton(
                    "OK"
                ) { _, _ ->
                    taskPresenter!!.getUsers(task.getId()!!)
                }
                .setNegativeButton(
                    "CANCELAR"
                ) { _, _ ->
                    taskPresenter!!.getUsers(task.getId()!!)
                }
                .create()
            builderObj.show()
            if (boardUserList!!.size > 8) {
                builderObj.window!!.setLayout(1000, 1200)
            }

        }
        recyclerViewTags = v.findViewById(R.id.recycler_tags)
        recyclerViewTags!!.layoutManager = layoutManagerTags
        taskPresenter!!.getTags(task.getId()!!)
        addTagsButton = v.findViewById(R.id.add_tags)
        addTagsButton!!.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            val items = arrayOfNulls<CharSequence>(boardTagList!!.size)
            val itemsChecked = BooleanArray(items.size)
            val tagsListIds = HashSet<Long>()
            tagList!!.forEach {
                tagsListIds.add(it.getId())
            }
            boardTagList!!.forEachIndexed { i, tag ->
                items[i] = tag.getName()
                itemsChecked[i] = tagsListIds.contains(tag.getId())
            }
            val builderObj = builder.setTitle("Añadir Tag")
                .setMultiChoiceItems(
                    items, itemsChecked
                ) { _, which, isChecked ->
                    if (isChecked) {
                        // Guardar indice seleccionado
                        val tagId = boardTagList!![which]
                        taskPresenter!!.addTag(task.getId()!!, tagId.getId())
                    } else {
                        // Remover indice sin selección
                        val tagId = boardTagList!![which]
                        taskPresenter!!.removeTag(task.getId()!!, tagId.getId(), false)
                    }
                }.setPositiveButton(
                    "OK"
                ) { _, _ ->
                    taskPresenter!!.getTags(task.getId()!!)
                }
                .setNegativeButton(
                    "CANCELAR"
                ) { _, _ ->
                    taskPresenter!!.getTags(task.getId()!!)
                }
                .setNeutralButton("CREAR TAG") { _, _ ->
                    val bottomSheet = BottomSheet(boardName, "", taskPresenter!!, "tag")
                    bottomSheet.show(fragmentMgr, "bottomSheet")
                }
                .create()
            builderObj.show()
            if (tagsListIds.size > 8) {
                builderObj.window!!.setLayout(1000, 1200)
            }

        }
        dateEnd = v.findViewById(R.id.date_picker)
        if (task.getDateEnd() != null) {
            val style: Int = DateFormat.MEDIUM
            val df = DateFormat.getDateInstance(style, Locale.forLanguageTag("es-ES"))
            val strDate: String = df.format(task.getDateEnd()!!)
            dateEnd!!.text = strDate
        }
        dateEnd!!.setOnClickListener {
            var d = Date()
            if (task.getDateEnd() != null) {
                d = task.getDateEnd()!!
            }
            val datePicker = DatePicker(d) { day, month, year -> onDateSelected(day, month, year) }
            datePicker.show(fragmentMgr, "datePicker")
        }
        attachFiles = v.findViewById(R.id.attached)
        attachFiles!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            resultLauncher!!.launch(Intent.createChooser(intent, "Select Picture"))
        }
        viewAttachments = v.findViewById(R.id.linear_layout_attachments)
        recyclerViewAttachments = v.findViewById(R.id.recycler_attachments)
        recyclerViewAttachments!!.layoutManager = layoutManagerAttachments
        if (!task.getAttachments().isNullOrEmpty()){
            recyclerViewAttachments!!.adapter =
                AttachmentsAdapter(task.getAttachments()!!,taskPresenter,task,requireContext())
            viewAttachments!!.visibility=View.VISIBLE
        }else{
            viewAttachments!!.visibility=View.GONE
        }
        recyclerViewSubtasks= v.findViewById(R.id.recycler_subtasks)
        recyclerViewSubtasks!!.layoutManager = layoutManagerSubtasks
        if (!task.getSubtasks().isNullOrEmpty()){
           // recyclerViewSubtasks!!.adapter =
             //   SubtaskAdapter(task.getSubtasks()!!,taskPresenter,task,requireContext(),fragmentMgr,this,boardId,boardName,rol)
            recyclerViewSubtasks!!.visibility=View.VISIBLE
        }else{
            recyclerViewSubtasks!!.visibility=View.GONE
        }
        buttonAddSubtasks  = v.findViewById(R.id.button_add_subtask)
        buttonAddSubtasks!!.setOnClickListener {
            val bottomSheet = BottomSheet(boardName, "", taskPresenter!!, "subtask")
            bottomSheet.task=task
            bottomSheet.show(fragmentMgr, "bottomSheet")
        }
        deleteSubtasks = v.findViewById(R.id.delete_subtasks)
        if (recyclerViewSubtasks!!.adapter!=null){
            subtasksSelected = (recyclerViewSubtasks!!.adapter as SubtaskAdapter).getSubTasksSelected()
        }
        if (subtasksSelected.isEmpty()){
            deleteSubtasks!!.visibility=View.GONE
        }else{
            deleteSubtasks!!.visibility=View.VISIBLE
        }
        deleteSubtasks!!.setOnClickListener {
            taskPresenter!!.deleteSubtasks(subtasksSelected)
        }
        if (rol==Rol.WATCHER){
            setViewAndChildrenEnabled(v,false)
        }

        val colors = Priority.getColors()
        val priorities = Priority.getNames()
        for (i in priorities.indices){
            spinnerPriorityList.add(SpinnerImage(priorities[i],R.drawable.ic_baseline_flag_24,colors[i]))
        }
        val sp = v.findViewById(R.id.spinner_priority) as Spinner
        val adapter = PrioritySpinnerAdapter(
            requireContext(),
            R.layout.spinner_image_item, R.id.text, spinnerPriorityList
        )
        sp.adapter = adapter
        val priority = task.getPriority()
        if (priority!=null){
            sp.setSelection(priority.getIndex())
        }
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i("LISTSPINNER", "Nothing Selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                taskPresenter!!.updatePriority(task.getId()!!, Priority.getPriorityByPrintableName(sp.selectedItem.toString()))
            }

        }


        return v
    }
    private fun setViewAndChildrenEnabled(view: View, enabled: Boolean) {
        if (view!=toolbar) {
            view.isEnabled = enabled
            if (view is ViewGroup) {
                val viewGroup = view
                for (i in 0 until viewGroup.childCount) {
                    val child = viewGroup.getChildAt(i)
                    setViewAndChildrenEnabled(child, enabled)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val cal = Calendar.getInstance()
        cal[year, month] = day
        val date = cal.time
        val style: Int = DateFormat.MEDIUM
        val df = DateFormat.getDateInstance(style, Locale.forLanguageTag("es-ES"))
        val strDate: String = df.format(date)
        dateEnd!!.text = strDate
        taskPresenter!!.updateDate(task.getId()!!, date)
    }

    override fun setTasks(tasks: List<Task>) {
        TODO(notYetImplemented)
    }

    override fun setAssignments(users: List<User>) {
        usersList = users
        if (users.size > 3) {
            val usersReduced = users.subList(0, 3)
            recyclerViewAssignments!!.adapter =
                AssignmentsAdapter(usersReduced, taskPresenter!!, task)
            val more = "+" + (users.size - usersReduced.size)
            moreThanThreeText!!.text = more
        } else {
            recyclerViewAssignments!!.adapter = AssignmentsAdapter(users, taskPresenter!!, task)
            moreThanThreeButton!!.visibility = View.GONE
            val spanCount1 = if (usersList!!.isNotEmpty()) {
                usersList!!.size
            } else {
                1
            }
            layoutManager!!.spanCount = spanCount1

        }
    }

    override fun setUsers(users: List<User>) {
        boardUserList = users
    }

    override fun setTags(tags: List<Tag>) {
        tagList = tags
        recyclerViewTags!!.adapter = TagsAdapter(tagList, taskPresenter, task,true)
        if (tagList!!.size < 5) {
            val spanCount = if (tagList!!.isNotEmpty()) {
                tagList!!.size
            } else {
                1
            }
            layoutManagerTags!!.spanCount = spanCount
        }

    }

    override fun setTagsBoard(tags: List<Tag>) {
        boardTagList = tags
    }

    override fun updateTags(tag: Tag) {
        (boardTagList as ArrayList).add(tag)
        addTagsButton!!.performClick()
    }

    override fun updateTask(t: Task) {
        task = t
        if (!task.getAttachments().isNullOrEmpty()){
            recyclerViewAttachments!!.adapter =
                AttachmentsAdapter(task.getAttachments()!!,taskPresenter,task,requireContext())
            viewAttachments!!.visibility=View.VISIBLE
        }else{
            viewAttachments!!.visibility=View.GONE
        }
        recyclerViewSubtasks!!.adapter =
            SubtaskAdapter(task.getSubtasks()!!,taskPresenter,task,requireContext(),fragmentMgr,this,boardId,boardName,rol)
        if (task.getSubtasks()!!.isNotEmpty()){
            recyclerViewSubtasks!!.visibility=View.VISIBLE
        }else{
            recyclerViewSubtasks!!.visibility=View.GONE
        }
        subtasksSelected= ArrayList()
        updateVisibilityDeleteSubtasks()
        val photos = task.getPhotos()
        if (photos.isEmpty()) {
            imageTask!!.visibility = View.GONE
        } else {
            imageTask!!.visibility = View.VISIBLE
            imageTask!!.setImageBitmap(photos[0])
        }
    }

    override fun setUser(u: User) {
        TODO(notYetImplemented)
    }

    override fun addMessage(msg: Message) {
        TODO(notYetImplemented)
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResponseFailure(t: Throwable?) {
        Log.e("TASK", t!!.message!!)
        Toast.makeText(context, getString(R.string.communication_error), Toast.LENGTH_LONG).show()
    }

    fun updateVisibilityDeleteSubtasks(){
        subtasksSelected = (recyclerViewSubtasks!!.adapter as SubtaskAdapter).getSubTasksSelected()
        if (subtasksSelected.isEmpty()){
            deleteSubtasks!!.visibility=View.GONE
        }else{
            deleteSubtasks!!.visibility=View.VISIBLE
        }
    }

    companion object {
        fun newInstance(t: Toolbar,task: Task,
                        boardName: String,
                        boardId: Long,
                        fragmentMgr: FragmentManager,
                        viewHolder: TaskAdapterInsideBoard.ViewHolder?,
                        rol: Rol?): TaskDetailsTab = TaskDetailsTab(t,task,boardName,boardId,fragmentMgr,viewHolder,rol)
    }


}