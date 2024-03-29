package com.example.protasks

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.*
import com.example.protasks.presenters.board.BoardPresenter
import com.example.protasks.utils.BottomSheet
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.collections.HashMap
import kotlin.collections.ArrayList
import android.view.Gravity

import android.widget.Toast
import com.example.protasks.presenters.board.IBoardContract
import com.example.protasks.utils.Preference
import com.example.protasks.utils.PreferencesManager


class SettingsFragment(
    private val taskLists: List<TaskList>,
    private val boardName: String,
    private val supportFragmentManager: FragmentManager,
    private val perm:BoardUsersPermRel
) : Fragment(),IBoardContract.View {
    private var wipLimit: TextInputEditText? = null
    private var wipLabelLimit: TextInputLayout?=null
    private var wipActivated: SwitchMaterial? = null
    private var board: Board? = null
    private var spinnerListWip: Spinner? = null
    private var listsIds: HashMap<String, Long> = HashMap()
    private var addUsersButton: TextView? = null
    private var recyclerViewUsers: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = LinearLayoutManager(context)
    private var presenter: BoardPresenter? = null
    private var timeActivated: SwitchMaterial? = null
    private var spinnerListCycleStart: Spinner? = null
    private var spinnerListCycleEnd: Spinner? = null
    private var spinnerListLeadStart: Spinner? = null
    private var spinnerListLeadEnd: Spinner? = null
    private var timeStartLabel: TextView?=null
    private var timeEndLabel: TextView?=null
    private var leadLabel: TextView?=null
    private var cycleLabel: TextView?=null
    private var noElementsMessage:String = ""
    private var sameCycleLeadMessage:String = ""
    private val notYetImplemented:String = "Not yet implemented"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    private fun setWip(list:List<String>){
        if (board != null) {
            wipActivated!!.isChecked = board!!.getWipActivated()
            wipLimit!!.setText(board!!.getWipLimit().toString())
            if (board!!.getWipList() != null) {
                spinnerListWip!!.setSelection(list.indexOf(board!!.getWipList()!!))
            }
            timeActivated!!.isChecked = board!!.getTimeActivated()
            if (board!!.getCycleStartList() !=null){
                spinnerListCycleStart!!.setSelection(list.indexOf(board!!.getCycleStartList()))
            }
            if (board!!.getCycleEndList() !=null){
                spinnerListCycleEnd!!.setSelection(list.indexOf(board!!.getCycleEndList()))
            }
            if (board!!.getLeadStartList() !=null){
                spinnerListLeadStart!!.setSelection(list.indexOf(board!!.getLeadStartList()))
            }
            if (board!!.getLeadEndList() !=null){
                spinnerListLeadEnd!!.setSelection(list.indexOf(board!!.getLeadEndList()))
            }

        } else {
            wipActivated!!.isEnabled = false
            timeActivated!!.isEnabled = false
        }
        if (!wipActivated!!.isChecked) {
            wipLimit!!.visibility = View.GONE
            spinnerListWip!!.visibility = View.GONE
            wipLabelLimit!!.visibility=View.GONE


        }
    }
    private fun wipOnChangeListener(isChecked:Boolean){
        presenter!!.updateWIP(
            isChecked,
            board,
            wipLimit!!.text.toString(),
            spinnerListWip!!.selectedItem.toString()
        )
        if (isChecked) {
            wipLimit!!.visibility = View.VISIBLE
            spinnerListWip!!.visibility = View.VISIBLE
            wipLabelLimit!!.visibility=View.VISIBLE
        } else {
            wipLimit!!.visibility = View.GONE
            spinnerListWip!!.visibility = View.GONE
            wipLabelLimit!!.visibility=View.GONE
        }
    }
    private fun onItemSelectedCycleStart(list: List<String>){
        if (spinnerListCycleStart!!.selectedItem.toString()!=noElementsMessage && spinnerListCycleStart!!.selectedItem.toString()==spinnerListCycleEnd!!.selectedItem.toString()){
            if (board!!.getCycleStartList()!=null) {
                spinnerListCycleStart!!.setSelection(list.indexOf(board!!.getCycleStartList()))
            }else{
                spinnerListCycleStart!!.setSelection(0)
            }
            val toast: Toast = Toast.makeText(context, sameCycleLeadMessage, Toast.LENGTH_LONG)
            toast.show()
        }else {
            presenter!!.updateTime(
                timeActivated!!.isChecked,
                board,
                spinnerListCycleStart!!.selectedItem.toString(),
                spinnerListCycleEnd!!.selectedItem.toString(),
                spinnerListLeadStart!!.selectedItem.toString(),
                spinnerListLeadEnd!!.selectedItem.toString()
            )
        }
    }
    private fun onItemSelectedCycleEnd(list: List<String>){
        if (spinnerListCycleStart!!.selectedItem.toString()!=noElementsMessage && spinnerListCycleStart!!.selectedItem.toString()==spinnerListCycleEnd!!.selectedItem.toString()){
            if (board!!.getCycleEndList()!=null) {
                spinnerListCycleEnd!!.setSelection(list.indexOf(board!!.getCycleEndList()))
            }else{
                spinnerListCycleEnd!!.setSelection(0)
            }
            val toast: Toast = Toast.makeText(context, sameCycleLeadMessage, Toast.LENGTH_LONG)
            toast.show()
        }else {
            presenter!!.updateTime(
                timeActivated!!.isChecked,
                board,
                spinnerListCycleStart!!.selectedItem.toString(),
                spinnerListCycleEnd!!.selectedItem.toString(),
                spinnerListLeadStart!!.selectedItem.toString(),
                spinnerListLeadEnd!!.selectedItem.toString()
            )
        }
    }
    private fun onItemSelectedLeadStart(list: List<String>){
        if (spinnerListLeadStart!!.selectedItem.toString()!=noElementsMessage && spinnerListLeadStart!!.selectedItem.toString()==spinnerListLeadEnd!!.selectedItem.toString()){
            if (board!!.getLeadStartList()!=null) {
                spinnerListLeadStart!!.setSelection(list.indexOf(board!!.getLeadStartList()))
            }else{
                spinnerListLeadStart!!.setSelection(0)
            }
            val toast: Toast = Toast.makeText(context, sameCycleLeadMessage, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            toast.show()
        }else {
            presenter!!.updateTime(
                timeActivated!!.isChecked,
                board,
                spinnerListCycleStart!!.selectedItem.toString(),
                spinnerListCycleEnd!!.selectedItem.toString(),
                spinnerListLeadStart!!.selectedItem.toString(),
                spinnerListLeadEnd!!.selectedItem.toString()
            )
        }
    }
    private fun onItemSelectedLeadEnd(list: List<String>){
        if (spinnerListLeadStart!!.selectedItem.toString()!=noElementsMessage && spinnerListLeadStart!!.selectedItem.toString()==spinnerListLeadEnd!!.selectedItem.toString()){
            if (board!!.getLeadEndList()!=null) {
                spinnerListLeadEnd!!.setSelection(list.indexOf(board!!.getLeadEndList()))
            }else{
                spinnerListLeadEnd!!.setSelection(0)
            }
            val toast: Toast = Toast.makeText(context, sameCycleLeadMessage, Toast.LENGTH_LONG)
            toast.show()
        }else {
            presenter!!.updateTime(
                timeActivated!!.isChecked,
                board,
                spinnerListCycleStart!!.selectedItem.toString(),
                spinnerListCycleEnd!!.selectedItem.toString(),
                spinnerListLeadStart!!.selectedItem.toString(),
                spinnerListLeadEnd!!.selectedItem.toString()
            )
        }
    }
    private fun setVisibilityByRol(userLoginRol:Rol,list: List<String>){
        if (userLoginRol==Rol.ADMIN || userLoginRol==Rol.OWNER){
            wipActivated!!.setOnCheckedChangeListener { _, isChecked ->
                wipOnChangeListener(isChecked)
            }
            wipLimit!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val limit = wipLimit!!.text!!.toString()
                    if (limit != "") {
                        presenter!!.updateWIP(
                            wipActivated!!.isChecked,
                            board,
                            limit,
                            spinnerListWip!!.selectedItem.toString()
                        )
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                    //I dont want to do anything before text changed
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    //I dont want to do anything on text changed
                }
            })


            spinnerListWip!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.i("WIPSPINNER", noElementsMessage)
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter!!.updateWIP(
                        wipActivated!!.isChecked,
                        board,
                        wipLimit!!.text.toString(),
                        spinnerListWip!!.selectedItem.toString()
                    )

                }

            }
            addUsersButton!!.setOnClickListener {
                val bottomSheet = BottomSheet(boardName, "", presenter!!, "user")
                bottomSheet.board = board
                bottomSheet.show(supportFragmentManager, "bottomSheet")
            }
            timeActivated!!.setOnCheckedChangeListener { _, isChecked ->
                presenter!!.updateTime(
                    isChecked,
                    board,
                    spinnerListCycleStart!!.selectedItem.toString(),
                    spinnerListCycleEnd!!.selectedItem.toString(),
                    spinnerListLeadStart!!.selectedItem.toString(),
                    spinnerListLeadEnd!!.selectedItem.toString()
                )
                if (isChecked) {
                    spinnerListLeadEnd!!.visibility = View.VISIBLE
                    spinnerListCycleStart!!.visibility = View.VISIBLE
                    spinnerListCycleEnd!!.visibility =View.VISIBLE
                    spinnerListLeadStart!!.visibility = View.VISIBLE
                    leadLabel!!.visibility=View.VISIBLE
                    cycleLabel!!.visibility=View.VISIBLE
                    timeEndLabel!!.visibility=View.VISIBLE
                    timeStartLabel!!.visibility=View.VISIBLE
                } else {
                    spinnerListLeadEnd!!.visibility = View.GONE
                    spinnerListCycleStart!!.visibility = View.GONE
                    spinnerListCycleEnd!!.visibility = View.GONE
                    spinnerListLeadStart!!.visibility = View.GONE
                    leadLabel!!.visibility=View.GONE
                    cycleLabel!!.visibility=View.GONE
                    timeEndLabel!!.visibility=View.GONE
                    timeStartLabel!!.visibility=View.GONE
                }
            }
            spinnerListCycleStart!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.i("WIPSPINNER", noElementsMessage)
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    onItemSelectedCycleStart(list)
                }

            }
            spinnerListCycleEnd!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.i("WIPSPINNER", noElementsMessage)
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    onItemSelectedCycleEnd(list)

                }

            }
            spinnerListLeadStart!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.i("WIPSPINNER", noElementsMessage)
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                   onItemSelectedLeadStart(list)

                }

            }
            spinnerListLeadEnd!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.i("WIPSPINNER", noElementsMessage)
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                   onItemSelectedLeadEnd(list)

                }

            }
        }else{
            wipLimit!!.isEnabled=false
            wipActivated!!.isEnabled=false
            spinnerListWip!!.isEnabled=false
            addUsersButton!!.visibility=View.GONE
            spinnerListLeadEnd!!.isEnabled=false
            spinnerListCycleStart!!.isEnabled=false
            spinnerListCycleEnd!!.isEnabled=false
            spinnerListLeadStart!!.isEnabled=false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.settings, container, false)
        noElementsMessage = view.context.getString(R.string.no_elements)
        sameCycleLeadMessage = view.context.getString(R.string.same_cycle_lead)
        val preference:PreferencesManager = Preference(requireContext())
        presenter= BoardPresenter(this, preference)
        wipActivated = view.findViewById(R.id.wip_activated)
        wipLimit = view.findViewById(R.id.wipLimit)
        wipLabelLimit=view.findViewById(R.id.label_wip)
        spinnerListWip = view.findViewById(R.id.spinner_list_wip)
        val list = ArrayList<String>()
        list.add(noElementsMessage)
        for (e in taskLists) {
            listsIds[e.getTitle()!!] = e.getId()
            list.add(e.getTitle()!!)
        }
        val myAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_list_item_1, list
        )
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerListWip!!.adapter = myAdapter

        timeActivated = view.findViewById(R.id.cycle_lead_time_activated)
        spinnerListCycleStart = view.findViewById(R.id.spinner_list_cycle_start)
        spinnerListCycleEnd = view.findViewById(R.id.spinner_list_cycle_end)
        spinnerListLeadStart = view.findViewById(R.id.spinner_list_lead_start)
        spinnerListLeadEnd = view.findViewById(R.id.spinner_list_lead_end)

        timeStartLabel= view.findViewById(R.id.column_start_label)
        timeEndLabel= view.findViewById(R.id.column_end_label)
        leadLabel= view.findViewById(R.id.lead_time_label)
        cycleLabel= view.findViewById(R.id.cycle_time_label)
        spinnerListLeadStart!!.adapter=myAdapter
        spinnerListLeadEnd!!.adapter=myAdapter
        spinnerListCycleStart!!.adapter=myAdapter
        spinnerListCycleEnd!!.adapter=myAdapter
        if (!taskLists.isNullOrEmpty()) {
            board = taskLists[0].getBoard()
        }
        setWip(list)
        if (!timeActivated!!.isChecked){
            spinnerListLeadEnd!!.visibility = View.GONE
            spinnerListCycleStart!!.visibility = View.GONE
            spinnerListCycleEnd!!.visibility = View.GONE
            spinnerListLeadStart!!.visibility = View.GONE
            leadLabel!!.visibility=View.GONE
            cycleLabel!!.visibility=View.GONE
            timeEndLabel!!.visibility=View.GONE
            timeStartLabel!!.visibility=View.GONE
        }

        recyclerViewUsers = view.findViewById(R.id.recycler_users)
        addUsersButton = view.findViewById(R.id.add_users)
        val userLoginRol=perm.getRol()
        setVisibilityByRol(userLoginRol!!,list)

        recyclerViewUsers!!.layoutManager = layoutManager
        if (board!=null){
            setUsers(board!!.getUsers())
        }

        return view
    }

    fun setUsers(users: List<BoardUsersPermRel>) {
        recyclerViewUsers!!.adapter = UsersPermAdapter(users, presenter!!,requireContext(),board!!,perm)
    }

    companion object {
        fun newInstance(
            taskLists: List<TaskList>,
            presenter: BoardPresenter,
            boardName: String,
            supportFragmentManager: FragmentManager,
            perm:BoardUsersPermRel
        ): SettingsFragment {
            return SettingsFragment(taskLists, boardName, supportFragmentManager,perm)
        }
    }

    override fun onResponseFailure(t: Throwable?) {
        Log.e("SETTINGS", t!!.message!!)
        Toast.makeText(context, getString(R.string.communication_error), Toast.LENGTH_LONG).show()
    }

    override fun setBoards(boards: ArrayList<Board>) {
        TODO(notYetImplemented)
    }

    override fun setUser(user: User) {
        TODO(notYetImplemented)
    }


    override fun getBoards() {
        TODO(notYetImplemented)
    }

    override fun setBoard(board:Board){
        this.board=board
        setUsers(board.getUsers())
    }

    override fun setRole(perm: BoardUsersPermRel) {
        TODO(notYetImplemented)
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun addBoard(board: Board) {
        TODO(notYetImplemented)
    }
}