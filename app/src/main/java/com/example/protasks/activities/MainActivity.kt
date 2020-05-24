package com.example.protasks.activities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.protasks.BoardAdapter
import com.example.protasks.BoardAdapterMenu
import com.example.protasks.FragmentManagerDialog
import com.example.protasks.R
import com.example.protasks.models.Board
import com.example.protasks.models.User
import com.example.protasks.presenters.BoardPresenter
import com.example.protasks.views.IBoardsView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity(), IBoardsView, View.OnClickListener,
    PopupMenu.OnMenuItemClickListener {
    private var mDrawer: DrawerLayout? = null
    private var actionBar: ActionBarDrawerToggle? = null
    private var presenter: BoardPresenter? = null
    var recyclerView: RecyclerView? = null
    var recyclerView2: RecyclerView? = null
    var layoutManager: GridLayoutManager? = null
    var boardAdapter: BoardAdapter? = null
    var boardAdapterMenu: BoardAdapterMenu? = null
    var context: Context? = null
    var toolbar: Toolbar? = null
    var swipeRefresh: SwipeRefreshLayout? = null
    var userPhoto: ImageView? = null
    var userEmail: TextView? = null
    var userCompleteName: TextView? = null
    var logoutButton: ImageButton? = null
    var viewMode: ImageButton? = null
    var addBoardButton: FloatingActionButton? = null
    var searchView: SearchView? = null
    private var handler: Handler = Handler()
    private var imageBoard: Bitmap? = null

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
            val text = searchView!!.query.toString()
            if (text == "") {
                presenter!!.getBoards()
            } else {
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
        userCompleteName = headerView.findViewById(R.id.nameProfile)
        userEmail = headerView.findViewById(R.id.userEmailProfile)
        presenter!!.getUser()
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
        addBoardButton = findViewById(R.id.button_add_board)
        searchView!!.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    presenter!!.filterBoards(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    presenter!!.filterBoards(query)
                    return true
                }

            })
        addBoardButton!!.setOnClickListener {
            val nagDialog2 = Dialog(this, android.R.style.ThemeOverlay_Material_Dark)
            nagDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE)
            nagDialog2.setCancelable(false)
            nagDialog2.setContentView(R.layout.add_elements)
            val tabLayout: TabLayout = nagDialog2.findViewById(R.id.tabsDialog)
            val viewPager: ViewPager2 = nagDialog2.findViewById(R.id.view_pager)
            val toolbar: Toolbar = nagDialog2.findViewById(R.id.toolbar)
            toolbar.setNavigationOnClickListener { nagDialog2.dismiss() }
            toolbar.title = "Añadir elemento"
            toolbar.inflateMenu(R.menu.create_elements_menu)
            toolbar.menu.getItem(0).isEnabled = false

            val adapter = FragmentManagerDialog(this, boardAdapterMenu!!.getBoards(), toolbar)

            viewPager.adapter = adapter


            val mediator =
                TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
                    when (position) {
                        0 -> {
                            tab.text = "Tablero"
                        }
                        1 -> {
                            tab.text = "Lista"
                        }
                        else -> {
                            tab.text = "Tarea"
                        }
                    }
                }
            mediator.attach()
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_save -> {
                        if (tabLayout.selectedTabPosition == 0) {
                            var b: Bitmap? = null
                            if (adapter.boardTab.colorNew != null) {
                                adapter.boardTab.setTextToImage(
                                    adapter.boardTab.colorNew!!,
                                    adapter.boardTab.textView!!.text.toString()
                                )
                                b = adapter.boardTab.image
                            } else if (imageBoard != null) {
                                b = imageBoard
                            }
                            presenter!!.createBoard(
                                adapter.boardTab.textView!!.text.toString(),
                                b!!
                            )

                        } else if (tabLayout.selectedTabPosition == 1) {
                            presenter!!.createTaskList(
                                adapter.listTab.boardName!!,
                                adapter.listTab.textView!!.text.toString()
                            )

                        } else {
                            presenter!!.createTask(
                                adapter.taskTab.boardName!!,
                                adapter.taskTab.textView!!.text.toString(),
                                adapter.taskTab.lName!!,
                                adapter.taskTab.descriptionView!!.text.toString()
                            )

                        }
                        nagDialog2.dismiss()
                    }
                }
                true
            }
            nagDialog2.show()
        }

    }


    override fun setBoards(boards: List<Board>) {
        boardAdapter = if (presenter!!.getViewPref()) {
            BoardAdapter(boards, R.layout.board_list_mode)
        } else {
            BoardAdapter(boards, R.layout.board)
        }
        recyclerView!!.adapter = boardAdapter
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

    fun showPopUp(view: View) {
        val menu = PopupMenu(this, view)
        menu.setOnMenuItemClickListener(this)
        menu.inflate(R.menu.view_mode_button)
        menu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val b = item!!.itemId == R.id.listViewMode
        presenter!!.setViewPref(b)
        recyclerView!!.adapter = null
        recyclerView!!.layoutManager = null
        presenter!!.getBoards()
        setLayoutManager()
        return true
    }

    private fun setLayoutManager() {
        layoutManager = if (presenter!!.getViewPref()) {
            GridLayoutManager(this, 1)
        } else {
            GridLayoutManager(this, 2)
        }
        recyclerView!!.layoutManager = layoutManager
        recyclerView2!!.layoutManager = GridLayoutManager(this, 1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
            presenter!!.setImage(data!!.data!!, this)

        } else if (resultCode == Activity.RESULT_OK) {
            val imageStream = this.contentResolver.openInputStream(data!!.data!!)
            imageBoard = BitmapFactory.decodeStream(imageStream)
        }
    }

    override fun getBoards() {
        presenter!!.getBoards()
    }
}
