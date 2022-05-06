package com.example.protasks.presenters

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.protasks.utils.RetrofitInstance
import com.example.protasks.models.*
import com.example.protasks.restServices.*
import com.example.protasks.utils.BottomSheet
import com.example.protasks.utils.ImageHandler
import com.example.protasks.utils.Preference
import com.example.protasks.views.IInsideBoardsView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class TaskListPresenter(private var view: IInsideBoardsView?, private var context: Context) :
    ITaskListPresenter {
    private val retrofitInsTaskList: RetrofitInstance<TaskListRestService> =
        RetrofitInstance("api/list/", TaskListRestService::class.java)
    private val retrofitInsTask: RetrofitInstance<TaskRestService> =
        RetrofitInstance("api/task/", TaskRestService::class.java)

    private val retrofitInsBoard: RetrofitInstance<BoardRestService> =
        RetrofitInstance("api/board/", BoardRestService::class.java)
    private val preference: Preference = Preference()
    private val image_handler: ImageHandler = ImageHandler()
    private val retrofitInsTag: RetrofitInstance<TagRestService> =
        RetrofitInstance("api/tag/", TagRestService::class.java)




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
                Toast.makeText(context, "List Created", Toast.LENGTH_SHORT).show()
                getLists(boardName)

            }

        })

    }

    override fun getLists(boardName: String) {
        val username = preference.getEmail(context)
        val taskLists = retrofitInsTaskList.service.getTaskListsByBoard(boardName,username!!)
        taskLists.enqueue(object : Callback<List<TaskList>> {
            override fun onFailure(call: Call<List<TaskList>>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<List<TaskList>>?, response: Response<List<TaskList>>?) {
                if (view!=null) {
                    view!!.setTaskLists(response!!.body()!!)
                }
            }

        })
    }
    override fun updateTaskListPosition(id:Long,position: Long) {
        val taskList = retrofitInsTaskList.service.updateTaskListPosition(id,position)
        taskList.enqueue(object : Callback<TaskList> {
            override fun onFailure(call: Call<TaskList>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<TaskList>?, response: Response<TaskList>?) {
                Log.v("retrofit", response.toString())
            }

        })
    }
    override fun updateTaskPosition(id:Long,newPosition:Long,newTaskList:Long,listMode:Boolean){
        val task = retrofitInsTask.service.updateTaskPosition(id,newPosition,newTaskList)
        task.enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Task>?, response: Response<Task>?) {
                if (listMode){
                    updateLists(newTaskList)
                }
            }

        })
    }
    fun updateLists(id:Long){
        val taskList = retrofitInsTaskList.service.getTaskBoardListsByListId(id)
        taskList.enqueue(object : Callback<List<TaskList>> {
            override fun onFailure(call: Call<List<TaskList>>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<List<TaskList>>?, response: Response<List<TaskList>>?) {
                if (view!=null){
                    view!!.updateTasks(response!!.body()!!)
                }
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
                Toast.makeText(context, "Task Created", Toast.LENGTH_SHORT).show()
                getLists(boardName)
            }

        })

    }
    override fun deleteTaskList(boardName: String,listName: String){
        val username = preference.getEmail(context)
        val taskList = retrofitInsTaskList.service.deleteTaskList(boardName, listName,username!!)
        taskList.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Toast.makeText(context, "Tasklist deleted", Toast.LENGTH_SHORT).show()
                getLists(boardName)
            }

        })
    }
    fun getBoards(view: BottomSheet) {
        val username = preference.getEmail(context)
        val boards = retrofitInsBoard.service.getBoardsByUser(username!!)
        boards.enqueue(object : Callback<List<Board>> {
            override fun onFailure(call: Call<List<Board>>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<List<Board>>?, response: Response<List<Board>>?) {
                view.setBoard(response!!.body()!!)

            }

        })
    }

    fun moveTaskList(board: Board, listName: String, boardDestId: Long) {
        val username = preference.getEmail(context)
        val taskList = retrofitInsTaskList.service.moveTaskList(board.getName()!!, listName, boardDestId,username!!)
        taskList.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Toast.makeText(context, "Tasklist moved", Toast.LENGTH_SHORT).show()
                getLists(board.getName()!!)
            }

        })

    }

    fun copyTaskList(board: Board, listName: String, boardDestId: Long) {
        val username = preference.getEmail(context)
        val taskList = retrofitInsTaskList.service.copyTaskList(board.getName()!!, listName, boardDestId,username!!)
        taskList.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Toast.makeText(context, "Tasklist copied", Toast.LENGTH_SHORT).show()
                if (board.getId()==boardDestId){
                    getLists(board.getName()!!)
                }
            }

        })
    }
    fun deleteTask(taskId:Long,boardName: String){
        val task = retrofitInsTask.service.deleteTask(taskId)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
                getLists(boardName)
            }

        })
    }

    fun copyTask(board: Board, listName: String, taskId: Long?, boardDestId: Long) {
        val username = preference.getEmail(context)
        val task = retrofitInsTask.service.copyTask(taskId!!,listName,boardDestId,username!!)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Toast.makeText(context, "Task copied", Toast.LENGTH_SHORT).show()
                if (board.getId()==boardDestId){
                    getLists(board.getName()!!)
                }
            }

        })
    }
    fun moveTask(board: Board, listName: String, taskId: Long?, boardDestId: Long) {
        val username = preference.getEmail(context)
        val task = retrofitInsTask.service.moveTask(taskId!!,listName,boardDestId,username!!)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Toast.makeText(context, "Task moved", Toast.LENGTH_SHORT).show()
                if (board.getId()==boardDestId){
                    getLists(board.getName()!!)
                }
            }

        })
    }

    override fun removeTag(taskId: Long, tagId: Long,update:Boolean) {
        val task = retrofitInsTag.service.removeTagToTask(tagId,taskId)
        task.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                Log.v("retrofit", response.toString())

            }

        })
    }

}