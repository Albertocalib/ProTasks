package com.example.protasks.presenters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import com.example.protasks.RetrofitInstance
import com.example.protasks.models.Board
import com.example.protasks.models.User
import com.example.protasks.restServices.BoardRestService
import com.example.protasks.restServices.UserRestService
import com.example.protasks.utils.ImageHandler
import com.example.protasks.utils.Preference
import com.example.protasks.views.IBoardsView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardPresenter(private var iBoardsView: IBoardsView,private var context: Context):IBoardPresenter {
    private val retrofitInsBoard: RetrofitInstance<BoardRestService> = RetrofitInstance("api/board/",BoardRestService::class.java)
    private val retrofitInsUser: RetrofitInstance<UserRestService> = RetrofitInstance("api/user/",UserRestService::class.java)
    private val preference:Preference = Preference()
    private val image_handler:ImageHandler= ImageHandler()
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

    override fun getViewPref():Boolean {
        return preference.getPrefViewMode(context)!!

    }
    override fun getPhoto(u:User):Bitmap{
        val imageBytes = Base64.decode(u.getPhoto(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    override fun removePreferences() {
        preference.removePreferences(context)
    }

    override fun setViewPref(listMode:Boolean) {
        preference.setPrefViewMode(listMode,context)

    }
    override fun filterBoards(name:String) {
        val username = preference.getEmail(context)
        if (name==""){
            getBoards()
        }else {
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
    fun saveImage(image:ImageView,context:Context){
        image_handler.saveImage(image,context)
    }


}