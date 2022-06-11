package com.example.protasks

import android.os.Bundle
import android.util.Log
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
import com.example.protasks.presenters.message.IMessageContract
import com.example.protasks.presenters.message.MessagePresenter
import com.example.protasks.utils.Preference
import com.example.protasks.utils.PreferencesManager


class TaskCommentsTab(private var task: Task,
                      val rol: Rol?) : Fragment(),IMessageContract.View {
    private var msgList: ArrayList<Message?>? = null
    private var inputText: EditText? = null
    private var send: Button? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: MessagesAdapter? = null
    val layoutManager = LinearLayoutManager(context)
    var presenter: MessagePresenter? = null
    private var user:User?=null

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View {
        super.onCreateView(inflater, parent, state)
        val v: View = inflater.inflate(R.layout.message_fragment, parent, false)
        msgList= task.getMessages() as ArrayList<Message?>?
        val preference:PreferencesManager = Preference(requireContext())
        presenter = MessagePresenter(this,preference)
        presenter!!.getUser()
        inputText = v.findViewById(R.id.input_text) as EditText?
        send = v.findViewById(R.id.send) as Button?
        recyclerView = v.findViewById(R.id.recycler_view) as RecyclerView?
        recyclerView!!.layoutManager = layoutManager
        adapter = MessagesAdapter(msgList!!, preference)
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
        fun newInstance(task: Task,
                        rol: Rol?): TaskCommentsTab = TaskCommentsTab(task,rol)
    }

    override fun onResponseFailure(t: Throwable?) {
        Log.e("MESSAGE", t!!.message!!)
        Toast.makeText(context, getString(R.string.communication_error), Toast.LENGTH_LONG).show()
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


}