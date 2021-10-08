/*
  Copyright 2014 Magnus Woxblom
  <p/>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p/>
  http://www.apache.org/licenses/LICENSE-2.0
  <p/>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
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
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.protasks.models.Board
import com.example.protasks.models.Task
import com.example.protasks.models.TaskList
import com.example.protasks.presenters.BoardPresenter
import com.example.protasks.presenters.TaskListPresenter
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.woxthebox.draglistview.DragListView
import com.woxthebox.draglistview.DragListView.DragListListenerAdapter
import com.woxthebox.draglistview.swipe.ListSwipeHelper
import com.woxthebox.draglistview.swipe.ListSwipeItem
import com.woxthebox.draglistview.swipe.ListSwipeItem.SwipeDirection
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.ArrayList

class SettingsFragment(
    private val taskLists: List<TaskList>,
    private val presenter: BoardPresenter,
    private val boardName: String,
    private val supportFragmentManager: FragmentManager
) : Fragment() {
    private var wipLimit: TextInputEditText? = null
    private var wipActivated: SwitchMaterial? = null
    private var board: Board? = null
    private var spinnerListWip: Spinner? = null
    private var listsIds: HashMap<String, Long> = HashMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.settings, container, false)
        wipActivated = view.findViewById(R.id.wip_activated)
        wipLimit = view.findViewById(R.id.wipLimit)
        spinnerListWip = view.findViewById(R.id.spinner_list_wip)
        val list = ArrayList<String>()
        list.add("Ningún elemento seleccionado")
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
        if (!taskLists.isNullOrEmpty()) {
            board = taskLists[0].getBoard()
        }
        if (board != null) {
            wipActivated!!.isChecked = board!!.getWipActivated()
            wipLimit!!.setText(board!!.getWipLimit().toString())
            if (board!!.getWipList() != null) {
                spinnerListWip!!.setSelection(list.indexOf(board!!.getWipList()!!))
            }
        } else {
            wipActivated!!.isEnabled = false
        }
        if (!wipActivated!!.isChecked) {
            wipLimit!!.visibility = View.GONE
            spinnerListWip!!.visibility = View.GONE

        }
        wipActivated!!.setOnCheckedChangeListener { _, isChecked ->
            presenter.updateWIP(
                isChecked,
                board,
                wipLimit!!.text.toString(),
                spinnerListWip!!.selectedItem.toString()
            )
            if (isChecked) {
                wipLimit!!.visibility = View.VISIBLE
                spinnerListWip!!.visibility = View.VISIBLE
            } else {
                wipLimit!!.visibility = View.GONE
                spinnerListWip!!.visibility = View.GONE
            }
        }
        wipLimit!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val limit = wipLimit!!.text!!.toString()
                if (limit != "") {
                    presenter.updateWIP(
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
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })


        spinnerListWip!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i("WIPSPINNER", "Nothing Selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                presenter.updateWIP(
                    wipActivated!!.isChecked,
                    board,
                    wipLimit!!.text.toString(),
                    spinnerListWip!!.selectedItem.toString()
                )

            }

        }
        return view
    }

    companion object {
        fun newInstance(
            taskLists: List<TaskList>,
            presenter: BoardPresenter,
            boardName: String,
            supportFragmentManager: FragmentManager
        ): SettingsFragment {
            return SettingsFragment(taskLists, presenter, boardName, supportFragmentManager)
        }
    }
}