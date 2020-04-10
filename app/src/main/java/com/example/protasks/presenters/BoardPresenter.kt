package com.example.protasks.presenters

import android.os.Handler
import android.util.Log
import com.example.protasks.RetrofitInstance
import com.example.protasks.models.Board
import com.example.protasks.restServices.BoardRestService
import com.example.protasks.views.IBoardsView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardPresenter(private var iBoardsView: IBoardsView):IBoardPresenter {
    private val retrofitIns: RetrofitInstance<BoardRestService> = RetrofitInstance("api/board/",BoardRestService::class.java)

    override fun getBoards(username: String) {

        val boards = retrofitIns.service.getBoardsByUser(username)
        boards.enqueue(object : Callback<List<Board>> {
            override fun onFailure(call: Call<List<Board>>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<List<Board>>?, response: Response<List<Board>>?) {
                iBoardsView.setBoards(response!!.body()!!)

            }

        })
    }


}