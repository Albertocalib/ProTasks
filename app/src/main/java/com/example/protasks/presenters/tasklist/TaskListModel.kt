package com.example.protasks.presenters.tasklist

import android.util.Log
import com.example.protasks.models.*
import com.example.protasks.restServices.*
import com.example.protasks.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TaskListModel :
    ITaskListContract.Model {
    private val retrofitInsTaskList: RetrofitInstance<TaskListRestService> =
        RetrofitInstance("api/list/", TaskListRestService::class.java)
    private val retrofitInsTask: RetrofitInstance<TaskRestService> =
        RetrofitInstance("api/task/", TaskRestService::class.java)

    private val retrofitInsBoard: RetrofitInstance<BoardRestService> =
        RetrofitInstance("api/board/", BoardRestService::class.java)
    private val retrofitInsTag: RetrofitInstance<TagRestService> =
        RetrofitInstance("api/tag/", TagRestService::class.java)



    override fun removeTag(onFinishedListener: ITaskListContract.Model.OnFinishedListener,taskId: Long, tagId: Long,update:Boolean) {
        val task = retrofitInsTag.service.removeTagToTask(tagId,taskId)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Log.v("retrofit", response.toString())

            }

        })
    }


    override fun getLists(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        boardName: String,
        username: String,
        message: String
    ) {
        val taskLists = retrofitInsTaskList.service.getTaskListsByBoard(boardName,username)
        taskLists.enqueue(object : Callback<List<TaskList>> {
            override fun onFailure(call: Call<List<TaskList>>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<List<TaskList>>?, response: Response<List<TaskList>>?) {
                onFinishedListener.onFinishedSetLists(response!!.isSuccessful,response.code(),response.body(),message)
            }

        })
    }

    override fun updateTaskListPosition(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        id: Long,
        position: Long
    ) {
        val taskList = retrofitInsTaskList.service.updateTaskListPosition(id,position)
        taskList.enqueue(object : Callback<TaskList> {
            override fun onFailure(call: Call<TaskList>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<TaskList>?, response: Response<TaskList>?) {
                Log.v("retrofit", response.toString())
            }

        })
    }

    override fun updateTaskPosition(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        id: Long,
        newPosition: Long,
        newTaskList: Long,
        listMode: Boolean
    ) {
        val task = retrofitInsTask.service.updateTaskPosition(id,newPosition,newTaskList)
        task.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                if (listMode){
                    updateLists(onFinishedListener,newTaskList)
                }
            }

        })
    }

    override fun deleteTaskList(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        boardName: String,
        listName: String,
        username: String
    ) {
        val taskList = retrofitInsTaskList.service.deleteTaskList(boardName, listName,username)
        taskList.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                getLists(onFinishedListener,boardName,username,"Tasklist deleted")
            }

        })
    }

    override fun createTaskList(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        boardName: String,
        listName: String,
        username: String
    ) {
        val t = TaskList()
        t.setTitle(listName)
        val taskList = retrofitInsTaskList.service.createList(t, boardName, username)
        taskList.enqueue(object : Callback<TaskList> {
            override fun onFailure(call: Call<TaskList>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<TaskList>?, response: Response<TaskList>?) {
                getLists(onFinishedListener,boardName,username,"List created")

            }

        })
    }

    override fun updateLists(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        id: Long
    ) {
        val taskList = retrofitInsTaskList.service.getTaskBoardListsByListId(id)
        taskList.enqueue(object : Callback<List<TaskList>> {
            override fun onFailure(call: Call<List<TaskList>>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<List<TaskList>>?, response: Response<List<TaskList>>?) {
                onFinishedListener.onFinishedUpdateTasks(response!!.isSuccessful,response.code(),response.body())
            }

        })
    }

    override fun createTask(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        task: Task,
        boardName: String,
        listName: String,
        description: String,
        username: String
    ) {
        val taskR = retrofitInsTask.service.createTask(task, boardName, listName,username)
        taskR.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                getLists(onFinishedListener,boardName,username,"Task created")
            }

        })
    }

    override fun copyTask(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        board: Board,
        listName: String,
        taskId: Long?,
        boardDestId: Long,
        username: String
    ) {
        val task = retrofitInsTask.service.copyTask(taskId!!,listName,boardDestId,username)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                if (board.getId()==boardDestId){
                    getLists(onFinishedListener,board.getName()!!,username,"Task copied")
                }
            }

        })
    }

    override fun moveTask(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        board: Board,
        listName: String,
        taskId: Long?,
        boardDestId: Long,
        username: String
    ) {
        val task = retrofitInsTask.service.moveTask(taskId!!,listName,boardDestId,username)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                if (board.getId()==boardDestId){
                    getLists(onFinishedListener,board.getName()!!,username,"Task moved")
                }
            }

        })
    }

    override fun deleteTask(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        taskId: Long,
        boardName: String,
        username: String
    ) {
        val task = retrofitInsTask.service.deleteTask(taskId)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                getLists(onFinishedListener,boardName,username,"Task deleted")
            }

        })
    }

    override fun copyTaskList(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        board: Board,
        listName: String,
        boardDestId: Long,
        username: String
    ) {
        val taskList = retrofitInsTaskList.service.copyTaskList(board.getName()!!, listName, boardDestId,username)
        taskList.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                if (board.getId()==boardDestId){
                    getLists(onFinishedListener,board.getName()!!,username,"Tasklist copied")
                }
            }

        })
    }

    override fun moveTaskList(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        board: Board,
        listName: String,
        boardDestId: Long,
        username: String
    ) {
        val taskList = retrofitInsTaskList.service.moveTaskList(board.getName()!!, listName, boardDestId,username)
        taskList.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                getLists(onFinishedListener,board.getName()!!,username,"Tasklist moved")
            }

        })
    }

    override fun getBoards(
        onFinishedListener: ITaskListContract.Model.OnFinishedListener,
        view: ITaskListContract.ViewBottomSheet,
        username: String
    ) {
        val boards = retrofitInsBoard.service.getBoardsByUser(username)
        boards.enqueue(object : Callback<List<Board>> {
            override fun onFailure(call: Call<List<Board>>?, t: Throwable?) {
                onFinishedListener.onFailure(t)
            }

            override fun onResponse(call: Call<List<Board>>?, response: Response<List<Board>>?) {
                onFinishedListener.onFinishedSetBoard(response!!.isSuccessful,response.code(),response.body())

            }

        })
    }

}