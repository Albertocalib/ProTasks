package com.example.protasks

import android.content.res.ColorStateList
import android.graphics.*
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Tag
import com.example.protasks.models.Task
import com.example.protasks.presenters.IPresenter
import com.example.protasks.utils.ImageHandler


class TagsAdapter(private val tags : List<Tag>?, private val presenter:IPresenter?, private val task: Task,private val remove:Boolean) :RecyclerView.Adapter<TagsAdapter.ViewHolderTags>(){

    private val imageHandler: ImageHandler = ImageHandler()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TagsAdapter.ViewHolderTags {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.tag_element, parent, false)
        return ViewHolderTags(view)
    }

    override fun getItemCount(): Int {
        return tags!!.size
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: TagsAdapter.ViewHolderTags, position: Int) {
        holder.tagName.text = tags!![position].getName()
        if (remove){
            holder.removeTag!!.setOnClickListener {
                presenter!!.removeTag(task.getId()!!, tags[position].getId(), true)

            }
        }else{
            holder.removeTag!!.visibility=View.GONE
        }
        val color = tags[position].getColor()
        if (color!=null){
            holder.view.backgroundTintList=ColorStateList.valueOf(Color.parseColor(color))
        }



    }

    class ViewHolderTags(v: View) : RecyclerView.ViewHolder(v) {
        var tagName: TextView
        var view:View=v
        var removeTag: ImageButton? = null

        init {
            tagName = view.findViewById(R.id.tagName)
            removeTag = view.findViewById(R.id.remove_tag)
        }
    }

}