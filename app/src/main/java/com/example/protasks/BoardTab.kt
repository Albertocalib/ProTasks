package com.example.protasks

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.core.view.get
import androidx.fragment.app.Fragment
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver


class BoardTab(private val t: Toolbar) : Fragment() {
    var image: Bitmap? = null
    var textView: TextView? = null
    var colorNew:Int?=null
    var selectPhoto:Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_elements_to_board, container, false)
        textView = view.findViewById(R.id.boardNameForm)
        textView!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                t.menu.getItem(0).isEnabled =(colorNew != null || selectPhoto)&& textView!!.text.toString() != ""
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }})
        val spinner: Spinner = view.findViewById(R.id.spinnerBoards)
        val list: Array<String> =
            resources.getStringArray(R.array.elements_add_board)
        val myAdapter = ArrayAdapter(requireActivity().baseContext,
            android.R.layout.simple_list_item_1, list)
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=myAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinner[0].isEnabled=false
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    2 -> {
                        ColorPickerPopup.Builder(requireActivity().baseContext)
                            .initialColor(Color.RED)
                            .enableBrightness(true)
                            .enableAlpha(true)
                            .okTitle("Aceptar")
                            .cancelTitle("Cancelar")
                            .showIndicator(true)
                            .showValue(false)
                            .build()
                            .show(view, object : ColorPickerObserver() {
                                override fun onColorPicked(color: Int) {
                                    val text=textView!!.text.toString()
                                    t.menu.getItem(0).isEnabled = text!=""
                                    colorNew=color
                                    list[2]="Color seleccionado correctamente"
                                    myAdapter.notifyDataSetChanged()
                                }
                            })
                    }
                    1 -> {
                        val text=textView!!.text.toString()
                        t.menu.getItem(0).isEnabled = text!=""
                        selectPhoto=true
                        list[1]="Imagen seleccionada correctamente"
                        myAdapter.notifyDataSetChanged()
                        val intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(intent, 2222)
                    }
                    else -> {
                        spinner[0].isEnabled=false
                        list[0] =""
                        myAdapter.notifyDataSetChanged()
                    }
                }
            }

        }

        return view

    }

    companion object {
        fun newInstance(t: Toolbar): BoardTab = BoardTab(t)
    }


}