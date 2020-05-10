package com.example.protasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.protasks.models.Board
import java.util.ArrayList

class Task_tab(private val boards: List<Board>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_tasks, container, false)
        val mySpinner: Spinner = view.findViewById(R.id.spinner1)
        val listName = ArrayList<String>()
        for (e in boards) {
            listName.add(e.getName()!!)
        }
        val myAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_list_item_1, listName
        )
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = myAdapter
        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

        }

        return view
    }

    companion object {
        fun newInstance(boards: List<Board>): Task_tab = Task_tab(boards)
    }


}