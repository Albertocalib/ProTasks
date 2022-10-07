package com.example.protasks.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.*
import com.example.protasks.models.Board
import com.example.protasks.models.BoardUsersPermRel
import com.example.protasks.models.User
import com.example.protasks.presenters.board.BoardPresenter
import com.example.protasks.presenters.board.IBoardContract
import com.example.protasks.utils.Preference
import com.example.protasks.utils.PreferencesManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), View.OnClickListener,IBoardContract.View {

    private var actionBar: ActionBarDrawerToggle? = null
    var recyclerView2: RecyclerView? = null
    var boardAdapterMenu: BoardAdapterMenu? = null
    var context: Context? = null
    var toolbar: Toolbar? = null
    var userPhoto: ImageView? = null
    var userEmail: TextView? = null
    var userCompleteName: TextView? = null
    private var mDrawer: DrawerLayout? = null
    private var presenter: BoardPresenter? = null
    var bottomNavView:BottomNavigationView? = null
    var fragment:Fragment?=null
    private var boards:ArrayList<Board> = ArrayList()
    private val notYetImplemented = "Not yet implemented"
    private var navigationViewObj:com.example.protasks.NavigationView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        val preference:PreferencesManager = Preference(baseContext)
        presenter = BoardPresenter(this, preference)
        presenter!!.getBoards()
        toolbar = findViewById(R.id.toolbar)
        val changeViewModeButton:ImageButton = toolbar!!.findViewById(R.id.viewModeButton)
        changeViewModeButton.visibility=View.INVISIBLE
        mDrawer = findViewById(R.id.drawer)
        actionBar = ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.open, R.string.close)
        mDrawer!!.addDrawerListener(actionBar!!)
        actionBar!!.syncState()
        presenter!!.getUser()
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationViewObj=NavigationView(navigationView,recyclerView2,userPhoto,this,this,userCompleteName,userEmail,presenter)
        bottomNavView =findViewById(R.id.app_bar)
        bottomNavView!!.setOnNavigationItemSelectedListener {
                item ->
            when (item.itemId) {
                R.id.nav_home-> {
                    fragment=MainBoardTab.newInstance(toolbar!!,this)
                }
                R.id.nav_search -> {
                    fragment=SearchTab.newInstance(this)

                }
                R.id.nav_notifications -> {
                    fragment=MainBoardTab.newInstance(toolbar!!,this)

                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_main_screen,fragment!!).commit()
            true
        }
        fragment=MainBoardTab.newInstance(toolbar!!,this)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_main_screen,fragment!!).commit()



    }

    override fun onResponseFailure(t: Throwable?) {
        Log.e("MAIN", t!!.message!!)
        Toast.makeText(context, getString(R.string.communication_error), Toast.LENGTH_LONG).show()
    }


    override fun setBoards(boards: ArrayList<Board>) {
        this.boards = boards
        boardAdapterMenu = BoardAdapterMenu(boards, R.layout.board_list_mode_menu)
        navigationViewObj!!.recyclerView2!!.adapter = boardAdapterMenu
    }

    override fun setUser(user: User) {
        if (user.getPhoto() != null) {
            val decodedImage = presenter!!.getPhoto(user)
            navigationViewObj!!.userPhoto!!.setImageBitmap(decodedImage)
        }
        navigationViewObj!!.userEmail!!.text = user.getEmail()
        val completeName = user.getName()!! + " " + user.getSurname()!!
        navigationViewObj!!.userCompleteName!!.text = completeName
    }

    override fun onClick(v: View?) {
        TODO(notYetImplemented)
    }

    override fun getBoards() {
        presenter!!.getBoards()
    }

    override fun setBoard(board: Board) {
        TODO(notYetImplemented)
    }

    override fun setRole(perm: BoardUsersPermRel) {
        TODO(notYetImplemented)
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun addBoard(board: Board) {
        boards.add(board)
        setBoards(boards)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
            presenter!!.setImage(data!!.data!!, this)

        } else if (resultCode == Activity.RESULT_OK) {
            fragment!!.onActivityResult(requestCode,resultCode,data)

        }
    }

}
