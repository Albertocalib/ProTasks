package com.example.protasks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView
import android.view.View
import android.widget.ArrayAdapter
import com.example.protasks.utils.SpinnerImage
import android.content.Context

class PrioritySpinnerAdapter(
    private var activity:Context,
    private var groupId: Int,
    id: Int,
    private var list: List<SpinnerImage>
) :
    ArrayAdapter<SpinnerImage?>(activity, id, list) {
    internal class ViewHolder {
        var text: TextView? = null
        var image: ImageView? = null
    }

    var inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent!!)
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val mViewHolder = ViewHolder()
        val view = inflater.inflate(groupId, parent, false)
        val textView = view!!.findViewById<View>(R.id.text) as TextView
        val imageView = view.findViewById<View>(R.id.img) as ImageView
        mViewHolder.text=textView
        mViewHolder.image=imageView
        mViewHolder.text!!.text = list[position].text
        if (list[position].color !=null) {
            mViewHolder.image!!.setImageResource(list[position].imageId!!)
            mViewHolder.image!!.setColorFilter(list[position].color!!)
        }
        return view
    }
}