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
import com.example.protasks.models.*
import com.example.protasks.presenters.MessagePresenter
import com.example.protasks.utils.Preference
import com.example.protasks.views.ITasksView


class TaskCommentsTab(private val t: Toolbar,
                      private var task: Task,
                      private val boardName: String,
                      private val boardId: Long,
                      private val fragmentMgr: FragmentManager,
                      private val viewHolder: TaskAdapterInsideBoard.ViewHolder?,
                      val rol: Rol?) : Fragment(),ITasksView {
    private var msgList: ArrayList<Message?>? = null
    private var inputText: EditText? = null
    private var send: Button? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: MessagesAdapter? = null
    val layoutManager = LinearLayoutManager(context)
    var presenter:MessagePresenter? =null
    private var user:User?=null

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        super.onCreateView(inflater, parent, state)
        val v: View = inflater.inflate(R.layout.message_fragment, parent, false)
        msgList= task.getMessages() as ArrayList<Message?>?
        presenter=MessagePresenter(this,Preference(requireContext()))
        presenter!!.getUser()
        inputText = v.findViewById(R.id.input_text) as EditText?
        send = v.findViewById(R.id.send) as Button?
        recyclerView = v.findViewById(R.id.recycler_view) as RecyclerView?
        recyclerView!!.layoutManager = layoutManager
        adapter = MessagesAdapter(msgList!!, Preference(requireContext()))
        recyclerView!!.adapter = adapter
        send!!.setOnClickListener {
            val content = inputText!!.text.toString()
            if ("" != content) {
                val u=User("","","","","")
                u.setId(user!!.getId()!!)
                val t=Task("","",null)
                t.setId(task.getId()!!)
                val msg = Message(content, u, t)
                inputText!!.setText("")
                presenter!!.createMessage(msg)
            }
        }
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

    override fun setTasks(tasks: List<Task>) {
        TODO("Not yet implemented")
    }

    override fun setAssignments(users: List<User>) {
        TODO("Not yet implemented")
    }

    override fun setUsers(users: List<User>) {
        TODO("Not yet implemented")
    }

    override fun setTags(tags: List<Tag>) {
        TODO("Not yet implemented")
    }

    override fun setTagsBoard(tags: List<Tag>) {
        TODO("Not yet implemented")
    }

    override fun updateTags(tag: Tag) {
        TODO("Not yet implemented")
    }

    override fun updateTask(t: Task) {
        TODO("Not yet implemented")
    }

    override fun setUser(u: User) {
        user=u
    }

    override fun addMessage(msg: Message) {
        msg.setUser(user!!)
        msg.setTask(task)
        msgList!!.add(msg)
        adapter!!.notifyItemInserted(msgList!!.size - 1)
        recyclerView!!.scrollToPosition(msgList!!.size - 1)
        task.addMessage(msg)
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}