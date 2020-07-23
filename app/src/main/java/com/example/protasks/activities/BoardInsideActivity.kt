package com.example.protasks.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.BoardAdapterMenu
import com.example.protasks.BoardFragment
import com.example.protasks.ListFragment
import com.example.protasks.R
import com.example.protasks.models.Board
import com.example.protasks.models.TaskList
import com.example.protasks.models.User
import com.example.protasks.presenters.BoardPresenter
import com.example.protasks.presenters.TaskListPresenter
import com.example.protasks.views.IBoardsView
import com.example.protasks.views.IInsideBoardsView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.util.*

class BoardInsideActivity : AppCompatActivity(), IInsideBoardsView,IBoardsView,PopupMenu.OnMenuItemClickListener {
    private var lists: List<TaskList> = ArrayList()
    private var boardName: String? = null
    private var presenter: TaskListPresenter? = null
    private var boardPresenter: BoardPresenter? = null
    var toolbar: Toolbar? = null
    private var mDrawer: DrawerLayout? = null
    private var actionBar: ActionBarDrawerToggle? = null
    private var changeViewModeButton: ImageButton? = null
    var bottomNavView: BottomNavigationView? = null
    var fragment:Fragment?=null
    var recyclerView2: RecyclerView? = null
    var boardAdapterMenu: BoardAdapterMenu? = null
    var context: Context? = null
    var userPhoto: ImageView? = null
    var userEmail: TextView? = null
    var userCompleteName: TextView? = null
    var logoutButton: ImageButton? = null
    var viewMode: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = TaskListPresenter(this, baseContext)
        boardName = intent.getStringExtra("BOARD_NAME")
        findViewById<TextView>(R.id.boardName).text=boardName
        presenter!!.getLists(boardName!!)
        toolbar = findViewById(R.id.toolbar)
        changeViewModeButton = toolbar!!.findViewById(R.id.viewModeButton)
        changeViewModeButton!!.visibility= View.VISIBLE
        changeViewModeButton!!.setOnClickListener {
            val menu = PopupMenu(baseContext, it)
            menu.setOnMenuItemClickListener(this)
            menu.inflate(R.menu.view_mode_button)
            menu.show()
        }
        mDrawer = findViewById(R.id.drawer)
        actionBar = ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.open, R.string.close)
        mDrawer!!.addDrawerListener(actionBar!!)
        actionBar!!.syncState()
        bottomNavView =findViewById(R.id.bottom_bar_board)
        bottomNavView!!.setOnNavigationItemSelectedListener {
                item ->
            when (item.itemId) {
                R.id.board_home-> {
                    presenter!!.getLists(boardName!!)
                }
                R.id.board_notifications -> {
                    //

                }
                R.id.board_settings -> {
                    //

                }
                R.id.nav_home_main_screen ->{
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
            if (fragment!=null){
                showFragment(fragment!!)
            }
            true
        }
        if (savedInstanceState == null) {
            showFragment(BoardFragment(lists,presenter!!))
        }
        boardPresenter = BoardPresenter(this, baseContext)
        boardPresenter!!.getUser()
        boardPresenter!!.getBoards()
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        recyclerView2 = navigationView.findViewById(R.id.recycler_board_navigation_view)
        setLayoutManager()
        val headerView = navigationView.getHeaderView(0)
        userPhoto = headerView.findViewById(R.id.profilePic)
        userCompleteName = headerView.findViewById(R.id.nameProfile)
        userEmail = headerView.findViewById(R.id.userEmailProfile)
        userPhoto!!.setOnClickListener {
            val nagDialog = Dialog(this, android.R.style.ThemeOverlay_Material_Dark)
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            nagDialog.setCancelable(false)
            nagDialog.setContentView(R.layout.image_dialog)
            val btnClose = nagDialog.findViewById(R.id.btnIvClose) as Button
            val btnDownload = nagDialog.findViewById(R.id.btnDownload) as Button
            val btnChangePhoto = nagDialog.findViewById(R.id.btnChangePhoto) as Button
            val ivPreview = nagDialog.findViewById(R.id.iv_preview_image) as ImageView
            ivPreview.setImageDrawable(userPhoto!!.drawable)
            btnClose.setOnClickListener {
                nagDialog.dismiss()
            }
            btnDownload.setOnClickListener {
                presenter!!.downloadImage(ivPreview, this)
                Toast.makeText(this, "Download completed", Toast.LENGTH_SHORT).show()
            }
            btnChangePhoto.setOnClickListener {
                nagDialog.dismiss()
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(intent, 1111)
            }

            nagDialog.show()
        }

        logoutButton = headerView.findViewById(R.id.logOutButton)
        logoutButton!!.setOnClickListener {
            logOut()
        }
        viewMode = headerView.findViewById(R.id.viewModeButton)
    }

    private fun showFragment(fragment: Fragment) {
        val transaction =
            supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment, "fragment").commit()
    }

    override fun setTaskLists(taskList: List<TaskList>) {
        lists = taskList
        showFragment(BoardFragment(lists,presenter!!))
    }
    override fun setBoards(boards: List<Board>) {
        boardAdapterMenu = BoardAdapterMenu(boards, R.layout.board_list_mode_menu)
        recyclerView2!!.adapter = boardAdapterMenu
    }

    override fun setUser(user: User) {
        if (user.getPhoto() != null) {
            val decodedImage = boardPresenter!!.getPhoto(user)
            userPhoto!!.setImageBitmap(decodedImage)
        }
        userEmail!!.text = user.getEmail()
        val completeName = user.getName()!! + " " + user.getSurname()!!
        userCompleteName!!.text = completeName
    }
    

    override fun logOut() {
        boardPresenter!!.removePreferences()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun setLayoutManager() {
        recyclerView2!!.layoutManager = GridLayoutManager(this, 1)
    }

    override fun getBoards() {
        boardPresenter!!.getBoards()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
            boardPresenter!!.setImage(data!!.data!!, this)

        } else if (resultCode == Activity.RESULT_OK) {
            fragment!!.onActivityResult(requestCode,resultCode,data)

        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        fragment = if (item!!.itemId == R.id.listViewMode){
            ListFragment(lists,presenter!!)
        }else{
            BoardFragment(lists,presenter!!)
        }
        showFragment(fragment!!)
        return true
    }



}