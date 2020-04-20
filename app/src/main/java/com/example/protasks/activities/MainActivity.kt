package com.example.protasks.activities

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.protasks.BoardAdapter
import com.example.protasks.R
import com.example.protasks.models.Board
import com.example.protasks.models.User
import com.example.protasks.presenters.BoardPresenter
import com.example.protasks.views.IBoardsView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header_main.*


class MainActivity : AppCompatActivity(), IBoardsView, View.OnClickListener {
    private var mDrawer: DrawerLayout? = null
    private var actionBar: ActionBarDrawerToggle? = null
    private var presenter: BoardPresenter? = null
    var recyclerView: RecyclerView? = null
    var layoutManager: GridLayoutManager? = null
    var boardAdapter: BoardAdapter? = null
    var context:Context? =null
    var toolbar: Toolbar?=null
    var swipeRefresh: SwipeRefreshLayout? = null
    var userPhoto:ImageView? =null
    var userEmail:TextView?=null
    var userCompleteName:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        presenter = BoardPresenter(this,baseContext)
        presenter!!.getBoards()
        mDrawer=findViewById(R.id.drawer)
        toolbar=findViewById(R.id.toolbar)
        actionBar = ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.open,R.string.close)
        recyclerView = findViewById(R.id.recycler_board_list)
        layoutManager = if (presenter!!.getViewPref()) {
            GridLayoutManager(this, 1)
        }else{
            GridLayoutManager(this,2)
        }
        recyclerView!!.layoutManager = layoutManager
        mDrawer!!.addDrawerListener(actionBar!!)
        actionBar!!.syncState()
        swipeRefresh = findViewById(R.id.swipeRefresh)
        swipeRefresh!!.setOnRefreshListener {
            presenter!!.getBoards()
            swipeRefresh!!.isRefreshing = false;
            Toast.makeText(this, "Boards Updated", Toast.LENGTH_SHORT).show()
        }
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val headerView = navigationView.getHeaderView(0)
        userPhoto = headerView.findViewById(R.id.profilePic)
        userCompleteName = headerView.findViewById(R.id.nameProfile)
        userEmail = headerView.findViewById(R.id.userEmailProfile)
        presenter!!.getUser()
    }


    override fun setBoards(boards: List<Board>) {
        boardAdapter = if (presenter!!.getViewPref()){
            BoardAdapter(boards,R.layout.board_list_mode)
        }else{
            BoardAdapter(boards,R.layout.board)
        }
        recyclerView!!.adapter=boardAdapter
    }
    override fun setUser(user: User) {
        if (user.getPhoto()!=null){
            val decodedImage=presenter!!.getPhoto(user)
            userPhoto!!.setImageBitmap(decodedImage)
        }
        userEmail!!.text=user.getEmail()
        val completeName=user.getName()!! +" "+ user.getSurname()!!
        userCompleteName!!.text=completeName
    }
    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }


}
