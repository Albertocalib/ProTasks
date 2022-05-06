package com.example.protasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import android.widget.*
import androidx.fragment.app.FragmentManager

import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Message
import com.example.protasks.models.Rol
import com.example.protasks.models.Task


class TaskCommentsTab(private val t: Toolbar,
                      private var task: Task,
                      private val boardName: String,
                      private val boardId: Long,
                      private val fragmentMgr: FragmentManager,
                      private val viewHolder: TaskAdapterInsideBoard.ViewHolder?,
                      val rol: Rol?) : Fragment() {
    private var msgList: List<Message?>? = null
    private var inputText: EditText? = null
    private var send: Button? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: MessagesAdapter? = null
    private var type = 0
    val layoutManager = LinearLayoutManager(context)

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        super.onCreateView(inflater, parent, state)
        val v: View = inflater.inflate(R.layout.message_fragment, parent, false)
        msgList=task.getMessages()
        inputText = v.findViewById(R.id.input_text) as EditText?
        send = v.findViewById(R.id.send) as Button?
        recyclerView = v.findViewById(R.id.recycler_view) as RecyclerView?
        recyclerView!!.layoutManager = layoutManager
        adapter = MessagesAdapter(requireContext(),msgList!!)
        recyclerView!!.adapter = adapter
        send!!.setOnClickListener(View.OnClickListener {
            val content = inputText!!.text.toString()
            if ("" != content) {
                inputText!!.setText("")
            }
        })
        return v
    }



    companion object {
        fun newInstance(t: Toolbar,task: Task,
                        boardName: String,
                        boardId: Long,
                        fragmentMgr: FragmentManager,
                        viewHolder: TaskAdapterInsideBoard.ViewHolder?,
                        rol: Rol?): TaskCommentsTab = TaskCommentsTab(t,task,boardName,boardId,fragmentMgr,viewHolder,rol)
    }


}