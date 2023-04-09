package com.example.protasks.presenters.board

import com.example.protasks.models.*
import com.example.protasks.utils.RetrofitInstance
import com.example.protasks.restServices.UserRestService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.protasks.restServices.BoardRestService
import com.example.protasks.restServices.TaskListRestService
import com.example.protasks.restServices.TaskRestService


class BoardModel:IBoardContract.Model{
    private val retrofitInsBoard: RetrofitInstance<BoardRestService> =
        RetrofitInstance("api/board/", BoardRestService::class.java)
    private val retrofitInsUser: RetrofitInstance<UserRestService> =
        RetrofitInstance("api/user/", UserRestService::class.java)
    private val retrofitInsTaskList: RetrofitInstance<TaskListRestService> =
        RetrofitInstance("api/list/", TaskListRestService::class.java)
    private val retrofitInsTask: RetrofitInstance<TaskRestService> =
        RetrofitInstance("api/task/", TaskRestService::class.java)

    override fun getBoards(onFinishedListener: IBoardContract.Model.OnFinishedListener?,username: String) {
        val boards = retrofitInsBoard.service.getBoardsByUser(username)
        boards.enqueue(object : Callback<List<Board>> {
            override fun onFailure(call: Call<List<Board>>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<List<Board>>?, response: Response<List<Board>>?) {
                onFinishedListener!!.onFinishedGetBoards(response!!.isSuccessful,response.code(), response.body())
            }

        })
    }

    override fun getUser(onFinishedListener: IBoardContract.Model.OnFinishedListener?, username: String) {
        val user = retrofitInsUser.service.getUser(username)
        user.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                onFinishedListener!!.onFinishedGetUser(response!!.isSuccessful,response.code(),response.body()!!)
            }

        })
    }

    override fun filterBoards(
        onFinishedListener: IBoardContract.Model.OnFinishedListener?,
        name: String, username: String
    ) {
        if (name == "") {
            getBoards(onFinishedListener,username)
        } else {
            val boards = retrofitInsBoard.service.getBoardsFilterByName(name, username)
            boards.enqueue(object : Callback<List<Board>> {
                override fun onFailure(call: Call<List<Board>>?, t: Throwable?) {
                    onFinishedListener!!.onFailure(t)
                }

                override fun onResponse(
                    call: Call<List<Board>>?,
                    response: Response<List<Board>>?
                ) {
                    onFinishedListener!!.onFinishedGetBoards(response!!.isSuccessful,response.code(),response.body()!!)
                }

            })
        }
    }

    override fun setImage(
        onFinishedListener: IBoardContract.Model.OnFinishedListener?,
        img: String, username:String
    ) {
        val user = retrofitInsUser.service.updatePhoto(img, username)
        user!!.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                onFinishedListener!!.onFinishedGetUser(response.isSuccessful,response.code(),response.body()!!)

            }

        })
    }

    override fun createBoard(
        onFinishedListener: IBoardContract.Model.OnFinishedListener?,
        b: Board,
        username: String
    ) {
        val board = retrofitInsBoard.service.createBoard(b, username)
        board.enqueue(object : Callback<Board> {
            override fun onFailure(call: Call<Board>, t: Throwable) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<Board>, response: Response<Board>) {
                onFinishedListener!!.onFinishedCreateBoard(response.isSuccessful,response.code(),response.body()!!)
            }

        })
    }

    override fun createTaskList(
        onFinishedListener: IBoardContract.Model.OnFinishedListener?,
        boardName: String,
        taskList: TaskList,
        username: String
    ) {
        val tl = retrofitInsTaskList.service.createList(taskList, boardName, username)
        tl.enqueue(object : Callback<TaskList> {
            override fun onFailure(call: Call<TaskList>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<TaskList>?, response: Response<TaskList>?) {
                onFinishedListener!!.onFinishedCreateTaskOrList(response!!.isSuccessful, response.code(), "List Created")

            }

        })
    }

    override fun createTask(
        onFinishedListener: IBoardContract.Model.OnFinishedListener?,
        boardName: String,
        listName: String,
        task: Task,
        username: String
    ) {
        val taskR = retrofitInsTask.service.createTask(task, boardName, listName, username)
        taskR.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                onFinishedListener!!.onFinishedCreateTaskOrList(response!!.isSuccessful, response.code(), "Task Created")
            }

        })
    }

    override fun updateWIP(
        onFinishedListener: IBoardContract.Model.OnFinishedListener?,
        checked: Boolean,
        boardId: Long,
        wipLimit: Int,
        wipList: String
    ) {
        val boardRes =
            retrofitInsBoard.service.updateWIP(boardId, checked, wipLimit, wipList)
        boardRes.enqueue(object : Callback<Board> {
            override fun onFailure(call: Call<Board>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<Board>?, response: Response<Board>?) {
                onFinishedListener!!.onFinishedSetBoard(response!!.isSuccessful,response.code(),response.body()!!)
            }

        })
    }

    override fun addUserToBoard(
        onFinishedListener: IBoardContract.Model.OnFinishedListener?,
        boardId: Long,
        username: String,
        role: Rol
    ) {
        val boardRes = retrofitInsBoard.service.addUserToBoard(boardId, username, role)
        boardRes.enqueue(object : Callback<Board> {
            override fun onFailure(call: Call<Board>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<Board>?, response: Response<Board>?) {
                onFinishedListener!!.onFinishedSetBoard(response!!.isSuccessful,response.code(),response.body()!!)

            }

        })
    }

    override fun updateRole(
        onFinishedListener: IBoardContract.Model.OnFinishedListener?,
        boardId: Long,
        userId: Long,
        role: Rol
    ) {
        val boardRes =
            retrofitInsBoard.service.updateRole(boardId, userId, role)
        boardRes.enqueue(object : Callback<Board> {
            override fun onFailure(call: Call<Board>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<Board>?, response: Response<Board>?) {
                onFinishedListener!!.onFinishedSetBoard(response!!.isSuccessful,response.code(),response.body()!!)
            }

        })
    }

    override fun deleteUserFromBoard(
        onFinishedListener: IBoardContract.Model.OnFinishedListener?,
        userId: Long,
        boardId: Long
    ) {
        val boardRes =
            retrofitInsBoard.service.deleteUserFromBoard(boardId, userId)
        boardRes.enqueue(object : Callback<Board> {
            override fun onFailure(call: Call<Board>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<Board>?, response: Response<Board>?) {
                onFinishedListener!!.onFinishedSetBoard(response!!.isSuccessful,response.code(),response.body()!!)
            }

        })
    }

    override fun getRole(
        onFinishedListener: IBoardContract.Model.OnFinishedListener?,
        userId: Long,
        boardId: Long
    ) {
        val board = retrofitInsBoard.service.getRol(boardId,userId)
        board.enqueue(object : Callback<BoardUsersPermRel> {
            override fun onFailure(call: Call<BoardUsersPermRel>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<BoardUsersPermRel>?, response: Response<BoardUsersPermRel>?) {
                onFinishedListener!!.onFinishedSetRole(response!!.isSuccessful,response.code(),response.body()!!)
            }

        })
    }

    override fun updateTime(
        onFinishedListener: IBoardContract.Model.OnFinishedListener?,
        checked: Boolean,
        board: Board?,
        cycleStart: String,
        cycleEnd: String,
        leadStart: String,
        leadEnd: String
    ) {
        val boardRes =
            retrofitInsBoard.service.updateTime(board!!.getId(), checked, cycleStart, cycleEnd, leadStart, leadEnd)
        boardRes.enqueue(object : Callback<Board> {
            override fun onFailure(call: Call<Board>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<Board>?, response: Response<Board>?) {
                onFinishedListener!!.onFinishedSetBoard(response!!.isSuccessful,response.code(),response.body()!!)
            }

        })
    }

}