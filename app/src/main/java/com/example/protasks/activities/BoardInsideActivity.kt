package com.example.protasks.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.*
import com.example.protasks.models.*
import com.example.protasks.presenters.board.BoardPresenter
import com.example.protasks.presenters.tasklist.TaskListPresenter
import com.example.protasks.presenters.board.IBoardContract
import com.example.protasks.presenters.tasklist.ITaskListContract
import com.example.protasks.utils.BottomSheet
import com.example.protasks.utils.Preference
import com.example.protasks.utils.PreferencesManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlin.collections.ArrayList
import com.example.protasks.NavigationView as NavigationView2

class BoardInsideActivity : AppCompatActivity(), ITaskListContract.ViewNormal,IBoardContract.View,PopupMenu.OnMenuItemClickListener {
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
    var context: Context? = null
    var userPhoto: ImageView? = null
    var userEmail: TextView? = null
    var userCompleteName: TextView? = null
    private var preference:PreferencesManager?=null
    private var perms: BoardUsersPermRel? = null
    private var boardId: Long? = null
    private var filterButton:ImageButton?=null
    private var boards:ArrayList<Board> = ArrayList()
    private var navigationViewObj:NavigationView2?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preference = Preference(baseContext)
        presenter = TaskListPresenter(this, preference!!)
        val bundle = intent.getBundleExtra("BOARD_INFO")
        boardName = bundle!!.getString("BOARD_NAME")
        boardId = bundle.getLong("BOARD_ID",-1)
        findViewById<TextView>(R.id.boardName).text=boardName
        presenter!!.getLists(boardName!!)
        toolbar = findViewById(R.id.toolbar)
        changeViewModeButton = toolbar!!.findViewById(R.id.viewModeButton)
        changeViewModeButton!!.visibility= View.VISIBLE
        changeViewModeButton!!.setOnClickListener {
            val menu = PopupMenu(baseContext, it)
            menu.setOnMenuItemClickListener(this)
            menu.inflate(R.menu.view_mode_button_in)
            menu.show()
        }
        filterButton = toolbar?.findViewById(R.id.filterButton)
        filterButton?.setOnClickListener {
            val bottomSheet = BottomSheet(boardName!!,"",presenter!!,"filter")
            bottomSheet.show(supportFragmentManager, "bottomSheet")
        }
        mDrawer = findViewById(R.id.drawer)
        actionBar = ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.open, R.string.close)
        mDrawer!!.addDrawerListener(actionBar!!)
        actionBar!!.syncState()
        boardPresenter = BoardPresenter(this,preference!!)
        boardPresenter!!.getUser()
        boardPresenter!!.getBoards()
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
                    fragment = SettingsFragment(lists!!,boardName!!,supportFragmentManager,perms!!)

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
            showFragment(BoardFragment.instance(this,lists,boardName!!))
        }
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationViewObj = NavigationView2(navigationView,recyclerView2,userPhoto,this,this,userCompleteName,userEmail,boardPresenter)
    }

    private fun showFragment(fragment: Fragment) {
        val transaction =
            supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment, "fragment").commit()
    }

    override fun setTaskLists(taskList: List<TaskList>) {
        lists = taskList
        fragment = if (preference!!.getModeView()){
            ListFragment.instance(this,lists,boardName!!)
        }else{
            BoardFragment.instance(this,lists,boardName!!)

        }
        showFragment(fragment!!)
    }

    override fun onResponseFailure(t: Throwable?) {
        Log.e("BOARDINSIDEACTIVITY", t!!.message!!)
        Toast.makeText(context, getString(R.string.communication_error), Toast.LENGTH_LONG).show()
    }

    override fun setBoards(boards: ArrayList<Board>) {
        this.boards = boards
        navigationViewObj!!.recyclerView2!!.adapter = BoardAdapterMenu(boards, R.layout.board_list_mode_menu)
    }

    override fun setUser(user: User) {
        if (user.getPhoto() != null) {
            navigationViewObj!!.userPhoto!!.setImageBitmap(boardPresenter!!.getPhoto(user))
        }
        val email = user.getEmail()
        navigationViewObj!!.userEmail!!.text = email
        navigationViewObj!!.userCompleteName!!.text = user.getCompleteName()
        boardPresenter!!.getRole(user.getId()!!,boardId!!)
    }
    override fun setRole(perm:BoardUsersPermRel){
        perms=perm
        if (fragment!=null){
            if (preference!!.getModeView()) {
                (fragment as ListFragment).rol = perm.getRol()
                (fragment as ListFragment).setWatcherVisibility()
            } else {
                (fragment as BoardFragment).rol = perm.getRol()
                (fragment as BoardFragment).setWatcherVisibility()
            }
        }
    }

    override fun showToast(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun addBoard(board: Board) {
        boards.add(board)
        setBoards(boards)
    }

    override fun getBoards() {
        boardPresenter!!.getBoards()
    }

    override fun setBoard(board: Board) {
        TODO("Not yet implemented")
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
        when {
            item!!.itemId==R.id.listViewMode -> {
                fragment = ListFragment.instance(this,lists,boardName!!)
                preference!!.setModeView(true)
                if (perms!=null){
                    (fragment as ListFragment).rol=perms!!.getRol()
                }
            }
            item.itemId==R.id.BoardViewMode -> {
                fragment = BoardFragment.instance(this,lists,boardName!!)
                preference!!.setModeView(false)
                if (perms!=null){
                    (fragment as BoardFragment).rol=perms!!.getRol()
                }
            }
            else -> {
                fragment = StatsFragment(lists)
            }
        }
        showFragment(fragment!!)
        return true
    }
    override fun updateTasks(listsUpdated:List<TaskList>){
        lists=listsUpdated
        if (preference!!.getModeView()){
            fragment=ListFragment.instance(this,lists,boardName!!)
            if (perms!=null){
                (fragment as ListFragment).rol=perms!!.getRol()
            }
        }else{
            fragment=BoardFragment.instance(this,lists,boardName!!)
            if (perms!=null){
                (fragment as BoardFragment).rol=perms!!.getRol()
            }

        }
        showFragment(fragment!!)
    }



}