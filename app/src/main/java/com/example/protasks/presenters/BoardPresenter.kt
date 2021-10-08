package com.example.protasks.presenters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.protasks.RetrofitInstance
import com.example.protasks.models.Board
import com.example.protasks.models.Task
import com.example.protasks.models.TaskList
import com.example.protasks.models.User
import com.example.protasks.restServices.BoardRestService
import com.example.protasks.restServices.TaskListRestService
import com.example.protasks.restServices.TaskRestService
import com.example.protasks.restServices.UserRestService
import com.example.protasks.utils.ImageHandler
import com.example.protasks.utils.Preference
import com.example.protasks.views.IBoardsView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class BoardPresenter(private var iBoardsView: IBoardsView, private var context: Context) :
    IBoardPresenter {
    private val retrofitInsBoard: RetrofitInstance<BoardRestService> =
        RetrofitInstance("api/board/", BoardRestService::class.java)
    private val retrofitInsUser: RetrofitInstance<UserRestService> =
        RetrofitInstance("api/user/", UserRestService::class.java)
    private val retrofitInsTaskList: RetrofitInstance<TaskListRestService> =
        RetrofitInstance("api/list/", TaskListRestService::class.java)
    private val retrofitInsTask: RetrofitInstance<TaskRestService> =
        RetrofitInstance("api/task/", TaskRestService::class.java)
    private val preference: Preference = Preference()
    private val image_handler: ImageHandler = ImageHandler()
    override fun getBoards() {
        val username = preference.getEmail(context)
        val boards = retrofitInsBoard.service.getBoardsByUser(username!!)
        boards.enqueue(object : Callback<List<Board>> {
            override fun onFailure(call: Call<List<Board>>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<List<Board>>?, response: Response<List<Board>>?) {
                iBoardsView.setBoards(response!!.body()!!)

            }

        })
    }

    override fun getUser() {
        val username = preference.getEmail(context)
        val user = retrofitInsUser.service.getUser(username!!)
        user.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                iBoardsView.setUser(response!!.body()!!)

            }

        })
    }

    override fun getViewPref(): Boolean {
        return preference.getPrefViewMode(context)!!

    }

    override fun getPhoto(u: User): Bitmap {
        val imageBytes = Base64.decode(u.getPhoto(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    override fun removePreferences() {
        preference.removePreferences(context)
    }

    override fun setViewPref(listMode: Boolean) {
        preference.setPrefViewMode(listMode, context)

    }

    override fun filterBoards(name: String) {
        val username = preference.getEmail(context)
        if (name == "") {
            getBoards()
        } else {
            val boards = retrofitInsBoard.service.getBoardsFilterByName(name, username!!)
            boards.enqueue(object : Callback<List<Board>> {
                override fun onFailure(call: Call<List<Board>>?, t: Throwable?) {
                    Log.v("retrofit", t.toString())
                }

                override fun onResponse(
                    call: Call<List<Board>>?,
                    response: Response<List<Board>>?
                ) {
                    iBoardsView.setBoards(response!!.body()!!)

                }

            })
        }
    }

    fun downloadImage(image: ImageView, context: Context) {
        image_handler.downloadImage(image, context)
    }

    fun setImage(uri: Uri, context: Context) {
        val imageStream = context.contentResolver.openInputStream(uri)
        val thumbnail = BitmapFactory.decodeStream(imageStream)
        val img = bitmap2Base64(thumbnail)
        val username = preference.getEmail(context)
        val user = retrofitInsUser.service.updatePhoto(img, username)
        user!!.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                iBoardsView.setUser(response!!.body()!!)

            }

        })

    }

    fun createBoard(name: String, b: Bitmap) {
        val img = bitmap2Base64(b)
        val b1 = Board()
        b1.setPhoto(img)
        b1.setName(name)
        val username = preference.getEmail(context)
        val board = retrofitInsBoard.service.createBoard(b1, username!!)
        board.enqueue(object : Callback<Board> {
            override fun onFailure(call: Call<Board>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Board>?, response: Response<Board>?) {
                iBoardsView.getBoards()
            }

        })
    }

    fun bitmap2Base64(b: Bitmap): String {
        val baos = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.PNG, 60, baos)
        val b1: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b1, Base64.NO_WRAP)
    }

    fun createTaskList(boardName: String, listName: String) {
        val username = preference.getEmail(context)
        val t = TaskList()
        t.setTitle(listName)
        val taskList = retrofitInsTaskList.service.createList(t, boardName, username!!)
        taskList.enqueue(object : Callback<TaskList> {
            override fun onFailure(call: Call<TaskList>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<TaskList>?, response: Response<TaskList>?) {
                iBoardsView.getBoards()
                Toast.makeText(context, "List Created", Toast.LENGTH_SHORT).show()

            }

        })

    }
    fun createTask(boardName: String,taskName:String,listName: String,description:String){
        val username = preference.getEmail(context)
        val t = Task(taskName,description)
        val task = retrofitInsTask.service.createTask(t, boardName, listName,username!!)
        task.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                iBoardsView.getBoards()
                Toast.makeText(context, "Task Created", Toast.LENGTH_SHORT).show()

            }

        })

    }

    fun updateWIP(checked: Boolean, board: Board?, wipLimit:String,wipList:String) {
        val wipLimitConv = wipLimit.toInt()
        val boardRes = retrofitInsBoard.service.updateWIP(board!!.getId(),checked,wipLimitConv,wipList)
        boardRes.enqueue(object : Callback<Board> {
            override fun onFailure(call: Call<Board>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Board>?, response: Response<Board>?) {
                //Nada
            }

        })

    }


}