package com.example.protasks.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.protasks.*
import com.example.protasks.models.Board
import com.example.protasks.models.BoardUsersPermRel
import com.example.protasks.models.User
import com.example.protasks.presenters.BoardPresenter
import com.example.protasks.utils.Preference
import com.example.protasks.views.IBoardsView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), View.OnClickListener,IBoardsView {

    private var actionBar: ActionBarDrawerToggle? = null
    var recyclerView2: RecyclerView? = null
    var boardAdapterMenu: BoardAdapterMenu? = null
    var context: Context? = null
    var toolbar: Toolbar? = null
    var userPhoto: ImageView? = null
    var userEmail: TextView? = null
    var userCompleteName: TextView? = null
    var logoutButton: ImageButton? = null
    var viewMode: ImageButton? = null
    private var mDrawer: DrawerLayout? = null
    private var presenter: BoardPresenter? = null
    var bottomNavView:BottomNavigationView? = null
    var fragment:Fragment?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        presenter = BoardPresenter(this, Preference(baseContext))
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


    override fun setBoards(boards: List<Board>) {
        boardAdapterMenu = BoardAdapterMenu(boards, R.layout.board_list_mode_menu)
        recyclerView2!!.adapter = boardAdapterMenu
    }

    override fun setUser(user: User) {
        if (user.getPhoto() != null) {
            val decodedImage = presenter!!.getPhoto(user)
            userPhoto!!.setImageBitmap(decodedImage)
        }
        userEmail!!.text = user.getEmail()
        val completeName = user.getName()!! + " " + user.getSurname()!!
        userCompleteName!!.text = completeName
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

    private fun setLayoutManager() {
        recyclerView2!!.layoutManager = GridLayoutManager(this, 1)
    }

    override fun getBoards() {
        presenter!!.getBoards()
    }

    override fun setBoard(board: Board) {
        TODO("Not yet implemented")
    }

    override fun setRole(perm: BoardUsersPermRel) {
        TODO("Not yet implemented")
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
