package com.example.protasks.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.BoardAdapter
import com.example.protasks.R
import com.example.protasks.models.Board
import com.example.protasks.presenters.BoardPresenter
import com.example.protasks.views.IBoardsView


class MainActivity : AppCompatActivity(), IBoardsView, View.OnClickListener {
    private var mDrawer: DrawerLayout? = null
    private var actionBar: ActionBarDrawerToggle? = null
    private var presenter: BoardPresenter? = null
    var recyclerView: RecyclerView? = null
    var linearLayoutManager: GridLayoutManager? = null
    var boardAdapter: BoardAdapter? = null
    var context:Context? =null
    var toolbar: Toolbar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        context = baseContext
        presenter = BoardPresenter(this,baseContext)
        presenter!!.getBoards()
        mDrawer=findViewById(R.id.drawer)
        toolbar=findViewById(R.id.toolbar)
        actionBar = ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.open,R.string.close)
        recyclerView = findViewById(R.id.recycler_board_list)
        linearLayoutManager = GridLayoutManager(this,2)
        recyclerView!!.layoutManager = linearLayoutManager
        mDrawer!!.addDrawerListener(actionBar!!)
        actionBar!!.syncState()
    }


    override fun setBoards(boards: List<Board>) {
        boardAdapter= BoardAdapter(boards)
        recyclerView!!.adapter=boardAdapter
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}
