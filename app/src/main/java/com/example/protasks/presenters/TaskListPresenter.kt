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
import com.example.protasks.views.IInsideBoardsView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class TaskListPresenter(private var view: IInsideBoardsView, private var context: Context) :
    ITaskListPresenter {
    private val retrofitInsTaskList: RetrofitInstance<TaskListRestService> =
        RetrofitInstance("api/list/", TaskListRestService::class.java)
    private val preference: Preference = Preference()
    private val image_handler: ImageHandler = ImageHandler()



    fun downloadImage(image: ImageView, context: Context) {
        image_handler.downloadImage(image, context)
    }

//    fun setImage(uri: Uri, context: Context) {
//        val imageStream = context.contentResolver.openInputStream(uri)
//        val thumbnail = BitmapFactory.decodeStream(imageStream)
//        val img = bitmap2Base64(thumbnail)
//        val username = preference.getEmail(context)
//        val user = retrofitInsUser.service.updatePhoto(img, username)
//        user!!.enqueue(object : Callback<User> {
//            override fun onFailure(call: Call<User>?, t: Throwable?) {
//                Log.v("retrofit", t.toString())
//            }
//
//            override fun onResponse(call: Call<User>?, response: Response<User>?) {
//                iBoardsView.setUser(response!!.body()!!)
//
//            }
//
//        })
//
//    }


    fun bitmap2Base64(b: Bitmap): String {
        val baos = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.PNG, 60, baos)
        val b1: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b1, Base64.NO_WRAP)
    }

//    fun createTaskList(boardName: String, listName: String) {
//        val username = preference.getEmail(context)
//        val t = TaskList()
//        t.setTitle(listName)
//        val taskList = retrofitInsTaskList.service.createList(t, boardName, username!!)
//        taskList.enqueue(object : Callback<TaskList> {
//            override fun onFailure(call: Call<TaskList>?, t: Throwable?) {
//                Log.v("retrofit", t.toString())
//            }
//
//            override fun onResponse(call: Call<TaskList>?, response: Response<TaskList>?) {
//                iBoardsView.getBoards()
//                Toast.makeText(context, "List Created", Toast.LENGTH_SHORT).show()
//
//            }
//
//        })
//
//    }

    override fun getLists(boardName: String) {
        val username = preference.getEmail(context)
        val taskLists = retrofitInsTaskList.service.getTaskListsByBoard(boardName,username!!)
        taskLists.enqueue(object : Callback<List<TaskList>> {
            override fun onFailure(call: Call<List<TaskList>>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<List<TaskList>>?, response: Response<List<TaskList>>?) {
                view.setTaskLists(response!!.body()!!)
            }

        })
    }


}