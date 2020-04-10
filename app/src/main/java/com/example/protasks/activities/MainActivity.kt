package com.example.protasks.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.BoardAdapter
import com.example.protasks.R
import com.example.protasks.models.Board
import com.example.protasks.presenters.BoardPresenter
import com.example.protasks.views.IBoardsView


class MainActivity : AppCompatActivity(), IBoardsView, View.OnClickListener {
    private var presenter: BoardPresenter? = null
    var recyclerView: RecyclerView? = null
    var linearLayoutManager: GridLayoutManager? = null
    var boardAdapter: BoardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        presenter = BoardPresenter(this)
        presenter!!.getBoards("Albertocalib_8")
        recyclerView = findViewById(R.id.recycler_board_list)
        linearLayoutManager = GridLayoutManager(this,2)
        recyclerView!!.layoutManager = linearLayoutManager

    }


    override fun getBoards(username: String) {
        TODO("Not yet implemented")
    }

    override fun setBoards(boards: List<Board>) {
        boardAdapter= BoardAdapter(boards)
        recyclerView!!.adapter=boardAdapter
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}
