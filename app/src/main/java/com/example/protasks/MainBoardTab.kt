package com.example.protasks

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.protasks.activities.BoardInsideActivity
import com.example.protasks.models.Board
import com.example.protasks.models.User
import com.example.protasks.presenters.BoardPresenter
import com.example.protasks.views.IBoardsView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainBoardTab(private val t: Toolbar,private val cont:Context) : Fragment(),IBoardsView,PopupMenu.OnMenuItemClickListener,
    BoardAdapter.OnItemClickListener {
    private var presenter: BoardPresenter? = null
    var recyclerView: RecyclerView? = null
    var layoutManager: GridLayoutManager? = null
    var boardAdapter: BoardAdapter? = null
    var swipeRefresh: SwipeRefreshLayout? = null
    var addBoardButton: FloatingActionButton? = null
    var searchView: SearchView? = null
    private var imageBoard: Bitmap? = null
    var changeViewModeButton:ImageButton?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_content, container, false)
        changeViewModeButton = t.findViewById(R.id.viewModeButton)
        changeViewModeButton!!.visibility=View.VISIBLE
        changeViewModeButton!!.setOnClickListener {
            val menu = PopupMenu(cont, it)
            menu.setOnMenuItemClickListener(this)
            menu.inflate(R.menu.view_mode_button)
            menu.show()
        }
        presenter = BoardPresenter(this, cont)
        presenter!!.getBoards()
        recyclerView = view.findViewById(R.id.recycler_board_list)
        setLayoutManager()
        searchView = view.findViewById(R.id.search_view)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        swipeRefresh!!.setOnRefreshListener {
            val text = searchView!!.query.toString()
            if (text == "") {
                presenter!!.getBoards()
            } else {
                presenter!!.filterBoards(text)
            }
            swipeRefresh!!.isRefreshing = false
            Toast.makeText(cont, "Boards Updated", Toast.LENGTH_SHORT).show()
        }
        addBoardButton = view.findViewById(R.id.button_add_board)
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
            val nagDialog2 = Dialog(cont, android.R.style.ThemeOverlay_Material_Dark)
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

            val adapter = FragmentManagerDialog(cont as FragmentActivity, boardAdapter!!.getBoards(), toolbar)

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
                        when (tabLayout.selectedTabPosition) {
                            0 -> {
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

                            }
                            1 -> {
                                presenter!!.createTaskList(
                                    adapter.listTab.boardName!!,
                                    adapter.listTab.textView!!.text.toString()
                                )

                            }
                            else -> {
                                presenter!!.createTask(
                                    adapter.taskTab.boardName!!,
                                    adapter.taskTab.textView!!.text.toString(),
                                    adapter.taskTab.lName!!,
                                    adapter.taskTab.descriptionView!!.text.toString()
                                )

                            }
                        }
                        nagDialog2.dismiss()
                    }
                }
                true
            }
            nagDialog2.show()
        }
        return view

    }

    companion object {
        fun newInstance(t: Toolbar,context:Context): MainBoardTab = MainBoardTab(t,context)
    }
    override fun setBoards(boards: List<Board>) {
        boardAdapter = if (presenter!!.getViewPref()) {
            BoardAdapter(boards, R.layout.board_list_mode,this)
        } else {
            BoardAdapter(boards, R.layout.board,this)
        }
        recyclerView!!.adapter = boardAdapter
    }

    override fun setUser(user: User) {
    }

    override fun logOut() {
        TODO("Not yet implemented")
    }

    override fun getBoards() {
        presenter!!.getBoards()
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
            GridLayoutManager(cont, 1)
        } else {
            GridLayoutManager(cont, 2)
        }
        recyclerView!!.layoutManager = layoutManager
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val imageStream = cont.contentResolver.openInputStream(data!!.data!!)
            imageBoard = BitmapFactory.decodeStream(imageStream)

        }
    }

    override fun onItemClicked(text: TextView) {
        val intent = Intent(context, BoardInsideActivity::class.java)
        intent.putExtra("BOARD_NAME", text.text)
        startActivity(intent)
    }


}