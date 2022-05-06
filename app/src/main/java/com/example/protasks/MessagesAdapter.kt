package com.example.protasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Task
import com.example.protasks.presenters.TaskPresenter

import android.widget.LinearLayout
import com.example.protasks.models.Message
import com.example.protasks.utils.Preference


class MessagesAdapter(
    private val context: Context,
    private val msgList: List<Message?>
) : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {
    private val preference= Preference()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var leftLayout: LinearLayout = itemView.findViewById<View>(R.id.left_layout) as LinearLayout
        var rightLayout: LinearLayout =
            itemView.findViewById<View>(R.id.right_layout) as LinearLayout
        var leftMsg: TextView = itemView.findViewById<View>(R.id.left_msg) as TextView
        var rightMsg: TextView = itemView.findViewById<View>(R.id.right_msg) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg: Message = msgList[position]!!
        val username = preference.getEmail(context)
        if (msg.getUser()!!.getEmail() != username) {
            holder.leftLayout.visibility = View.VISIBLE
            holder.rightLayout.visibility = View.GONE
            holder.leftMsg.text = msg.getBody()
        } else{
            holder.rightLayout.visibility = View.VISIBLE
            holder.leftLayout.visibility = View.GONE
            holder.rightMsg.text = msg.getBody()
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }


}