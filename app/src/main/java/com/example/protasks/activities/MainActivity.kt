package com.example.protasks.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.protasks.BoardAdapter
import com.example.protasks.BoardAdapterMenu
import com.example.protasks.R
import com.example.protasks.models.Board
import com.example.protasks.models.User
import com.example.protasks.presenters.BoardPresenter
import com.example.protasks.views.IBoardsView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), IBoardsView, View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private var mDrawer: DrawerLayout? = null
    private var actionBar: ActionBarDrawerToggle? = null
    private var presenter: BoardPresenter? = null
    var recyclerView: RecyclerView? = null
    var recyclerView2: RecyclerView? = null
    var layoutManager: GridLayoutManager? = null
    var boardAdapter: BoardAdapter? = null
    var boardAdapterMenu: BoardAdapterMenu? = null
    var context:Context? =null
    var toolbar: Toolbar?=null
    var swipeRefresh: SwipeRefreshLayout? = null
    var userPhoto:ImageView? =null
    var userEmail:TextView?=null
    var userCompleteName:TextView?=null
    var logoutButton:ImageButton?=null
    var viewMode:ImageButton?=null
    var addBoardButton:FloatingActionButton?=null
    var searchView:SearchView?=null
    private var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        presenter = BoardPresenter(this, baseContext)
        presenter!!.getBoards()
        mDrawer = findViewById(R.id.drawer)
        toolbar = findViewById(R.id.toolbar)
        actionBar = ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.open, R.string.close)
        recyclerView = findViewById(R.id.recycler_board_list)
        mDrawer!!.addDrawerListener(actionBar!!)
        actionBar!!.syncState()
        searchView = findViewById(R.id.search_view)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        swipeRefresh!!.setOnRefreshListener {
            val text=searchView!!.query.toString()
            if (text=="") {
                presenter!!.getBoards()
            }else{
                presenter!!.filterBoards(text)
            }
            swipeRefresh!!.isRefreshing = false;
            Toast.makeText(this, "Boards Updated", Toast.LENGTH_SHORT).show()
        }
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        recyclerView2 = navigationView.findViewById(R.id.recycler_board_navigation_view)
        setLayoutManager()
        val headerView = navigationView.getHeaderView(0)
        userPhoto = headerView.findViewById(R.id.profilePic)
        userPhoto!!.setOnClickListener {
            val nagDialog= Dialog(this,android.R.style.ThemeOverlay_Material_Dark)
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            nagDialog.setCancelable(false)
            nagDialog.setContentView(R.layout.image_dialog)
            val btnClose = nagDialog.findViewById(R.id.btnIvClose) as Button
            val ivPreview = nagDialog.findViewById(R.id.iv_preview_image) as ImageView
            ivPreview.setImageDrawable(userPhoto!!.drawable)
            btnClose.setOnClickListener {
                nagDialog.dismiss()
            }
            nagDialog.show()
        }

        userCompleteName = headerView.findViewById(R.id.nameProfile)
        userEmail = headerView.findViewById(R.id.userEmailProfile)
        presenter!!.getUser()
        logoutButton = headerView.findViewById(R.id.logOutButton)
        logoutButton!!.setOnClickListener {
            logOut()
        }
        viewMode = headerView.findViewById(R.id.viewModeButton)
        addBoardButton = findViewById(R.id.button_add_board)
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                presenter!!.filterBoards(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                presenter!!.filterBoards(query)
                return true
            }

        })
    }


    override fun setBoards(boards: List<Board>) {
        boardAdapter = if (presenter!!.getViewPref()){
            BoardAdapter(boards,R.layout.board_list_mode)
        }else{
            BoardAdapter(boards,R.layout.board)
        }
        recyclerView!!.adapter=boardAdapter
        boardAdapterMenu = BoardAdapterMenu(boards,R.layout.board_list_mode_menu)
        recyclerView2!!.adapter=boardAdapterMenu

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
    override fun logOut() {
        presenter!!.removePreferences()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun showPopUp(view: View) {
        val menu = PopupMenu(this,view)
        menu.setOnMenuItemClickListener(this)
        menu.inflate(R.menu.view_mode_button)
        menu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val b=item!!.itemId==R.id.listViewMode
        presenter!!.setViewPref(b)
        recyclerView!!.adapter = null
        recyclerView!!.layoutManager = null
        presenter!!.getBoards()
        setLayoutManager()
        return true
    }
    private fun setLayoutManager(){
        layoutManager=if (presenter!!.getViewPref()) {
            GridLayoutManager(this, 1)
        }else{
            GridLayoutManager(this,2)
        }
        recyclerView!!.layoutManager = layoutManager
        recyclerView2!!.layoutManager = GridLayoutManager(this, 1)
    }
    fun createBoard(view:View?){
        //TODO
    }

}
