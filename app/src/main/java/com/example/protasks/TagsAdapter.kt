package com.example.protasks

import android.graphics.*
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Board
import com.example.protasks.models.Tag
import com.example.protasks.models.Task
import com.example.protasks.models.User
import com.example.protasks.presenters.TaskPresenter
import com.example.protasks.utils.ImageHandler


class TagsAdapter(private val tags : List<Tag>?, private val taskPresenter:TaskPresenter, private val task: Task) :RecyclerView.Adapter<TagsAdapter.ViewHolderTags>(){

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

    override fun onBindViewHolder(holder: TagsAdapter.ViewHolderTags, position: Int) {
        holder.tagName.text=tags!![position].getName()
        holder.removeTag!!.setOnClickListener {
            taskPresenter.removeTag(task.getId()!!,tags[position].getId(),true)
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