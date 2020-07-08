package com.example.protasks

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.protasks.models.Board
import androidx.appcompat.widget.Toolbar
import java.util.ArrayList

class TaskTab(private val boards: List<Board>, private val t: Toolbar) : Fragment() {
    var textView: TextView? = null
    var boardName: String? = null
    var lName: String? = null
    var descriptionView: TextView? = null
    private val anyElementSelected="Ningun elemento seleccionado"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_tasks, container, false)
        textView = view.findViewById(R.id.taskname)
        descriptionView = view.findViewById(R.id.taskDescription)
        val mySpinner: Spinner = view.findViewById(R.id.spinnerBoardTask)
        val listName = ArrayList<String>()
        listName.add(anyElementSelected)
        for (e in boards) {
            listName.add(e.getName()!!)
        }
        val myAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_list_item_1, listName
        )
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = myAdapter

        val mySpinnerList: Spinner = view.findViewById(R.id.spinnerTaskList)
        val listNamesTaskList = ArrayList<String>()
        listNamesTaskList.add(anyElementSelected)
        val myAdapterTaskList = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_list_item_1, listNamesTaskList
        )
        myAdapterTaskList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinnerList.adapter = myAdapterTaskList

        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i("TASKSPINNER", "Nothing Selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                boardName = mySpinner.selectedItem.toString()
                val listNamesTaskL = getTaskListByBoard(0.coerceAtLeast(position - 1))
                myAdapterTaskList.clear()
                myAdapterTaskList.addAll(listNamesTaskL)
                myAdapterTaskList.notifyDataSetChanged()
                t.menu.getItem(0).isEnabled =
                    (mySpinner.selectedItem.toString() != anyElementSelected) && textView!!.text.toString() != "" && mySpinnerList.selectedItem.toString() != anyElementSelected
            }

        }
        mySpinnerList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i("TASKSPINNER2", "Nothing Selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                lName = mySpinnerList.selectedItem.toString()
                t.menu.getItem(0).isEnabled =
                    (mySpinner.selectedItem.toString() != anyElementSelected) && textView!!.text.toString() != "" && mySpinnerList.selectedItem.toString() != anyElementSelected
                            && descriptionView!!.text.toString() != ""
            }

        }
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                t.menu.getItem(0).isEnabled =
                    (mySpinner.selectedItem.toString() != anyElementSelected) && textView!!.text.toString() != "" && mySpinnerList.selectedItem.toString() != anyElementSelected
                            && descriptionView!!.text.toString() != ""
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
        textView!!.addTextChangedListener(textWatcher)
        descriptionView!!.addTextChangedListener(textWatcher)

        return view
    }

    fun getTaskListByBoard(position: Int): ArrayList<String> {
        val taskList = boards[position].getTaskLists()!!
        val listName = ArrayList<String>()
        listName.add(anyElementSelected)
        for (list in taskList) {
            listName.add(list!!.getTitle()!!)
        }
        return listName
    }

    companion object {
        fun newInstance(boards: List<Board>, t: Toolbar): TaskTab = TaskTab(boards, t)
    }


}