package com.example.protasks

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.protasks.models.Board
import java.util.ArrayList

class ListTab(private val boards: List<Board>, private val t: Toolbar) : Fragment() {
    var textView: TextView? = null
    var boardName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_tasklist, container, false)
        val mySpinner: Spinner = view.findViewById(R.id.spinnerBoard)
        textView = view.findViewById(R.id.addList)
        textView!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                t.menu.getItem(0).isEnabled =(mySpinner.selectedItem.toString()!= "Ningun elemento seleccionado")&& textView!!.text.toString() != ""
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }})
        val listName = ArrayList<String>()
        listName.add("Ningun elemento seleccionado")
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
                t.menu.getItem(0).isEnabled =(mySpinner.selectedItem.toString()!= "Ningun elemento seleccionado")&& textView!!.text.toString() != ""
                boardName=mySpinner.selectedItem.toString();
            }

        }

        return view
    }

    companion object {
        fun newInstance(boards: List<Board>,t: Toolbar): ListTab = ListTab(boards,t)
    }


}