package com.example.protasks.presenters

import android.content.Context
import android.util.Log
import com.example.protasks.RetrofitInstance
import com.example.protasks.models.Task
import com.example.protasks.models.User
import com.example.protasks.restServices.BoardRestService
import com.example.protasks.restServices.TaskListRestService
import com.example.protasks.restServices.TaskRestService
import com.example.protasks.restServices.UserRestService
import com.example.protasks.utils.Preference
import com.example.protasks.views.ITasksView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TaskPresenter(private var view: ITasksView, private var context: Context) :
    ITaskPresenter {
    private val retrofitInsBoard: RetrofitInstance<BoardRestService> =
        RetrofitInstance("api/board/", BoardRestService::class.java)
    private val retrofitInsUser: RetrofitInstance<UserRestService> =
        RetrofitInstance("api/user/", UserRestService::class.java)
    private val retrofitInsTaskList: RetrofitInstance<TaskListRestService> =
        RetrofitInstance("api/list/", TaskListRestService::class.java)
    private val retrofitInsTask: RetrofitInstance<TaskRestService> =
        RetrofitInstance("api/task/", TaskRestService::class.java)
    private val preference: Preference = Preference()

    override fun getTasks() {
        val username = preference.getEmail(context)
        val task = retrofitInsTask.service.getTasksByUser(username!!)
        task.enqueue(object : Callback<List<Task>> {
            override fun onFailure(call: Call<List<Task>>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<List<Task>>?, response: Response<List<Task>>?) {
                view.setTasks(response!!.body()!!)

            }

        })
    }
    override fun filterTasks(name: String) {
        val username = preference.getEmail(context)
        if (name == "") {
            getTasks()
        } else {
            val tasks = retrofitInsTask.service.getTasksFilterByName(name, username!!)
            tasks.enqueue(object : Callback<List<Task>> {
                override fun onFailure(call: Call<List<Task>>?, t: Throwable?) {
                    Log.v("retrofit", t.toString())
                }

                override fun onResponse(
                    call: Call<List<Task>>?,
                    response: Response<List<Task>>?
                ) {
                    view.setTasks(response!!.body()!!)

                }

            })
        }
    }
    override fun getUsers(id:Long) {
        val task = retrofitInsTask.service.getUsersByTask(id)
        task.enqueue(object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                view.setAssignments(response!!.body()!!)

            }

        })
    }
    override fun getUsersInBoard(boardId:Long){
        val board = retrofitInsUser.service.getUsersInBoard(boardId)
        board.enqueue(object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                view.setUsers(response!!.body()!!)

            }

        })
    }
    fun addAssignment(taskId:Long,userId:Long){
        val task = retrofitInsTask.service.addUserToTask(taskId,userId)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Log.v("retrofit", response.toString())
            }

        })
    }
    fun removeAssignment(taskId:Long,userId:Long,update:Boolean){
        val task = retrofitInsTask.service.removeUserToTask(taskId,userId)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Log.v("retrofit", response.toString())
                if(update){
                    getUsers(taskId)
                }
            }

        })
    }


}