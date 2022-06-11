package com.example.protasks

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.protasks.models.Rol
import com.example.protasks.models.Tag
import com.example.protasks.models.Task
import com.example.protasks.presenters.tasklist.TaskListPresenter
import com.example.protasks.utils.BottomSheet
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.woxthebox.draglistview.DragItemAdapter
import java.util.*

class TaskAdapterInsideBoard(
    private val context: Context,
    list: ArrayList<Triple<Long, Task, Boolean>>,
    private val listMode: Boolean,
    private val mLayoutId: Int,
    private val mGrabHandleId: Int,
    private val mDragOnLongPress: Boolean,
    private val supportFragmentManager: FragmentManager,
    private val boardName: String,
    private val boardId: Long,
    private val presenter: TaskListPresenter,
    private val rol:Rol?=null
) :
    DragItemAdapter<Triple<Long, Task, Boolean>, TaskAdapterInsideBoard.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(mLayoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        val task = mItemList[position]!!.second
        val text = task.getTitle()
        holder.mText!!.text = text
        holder.listText!!.text = task.getTaskList().getTitle()
        holder.task = task
        if (mItemList[position]!!.third && listMode) {
            holder.cardViewTask!!.visibility = View.GONE
            holder.mText!!.visibility = View.GONE
            holder.image!!.visibility = View.GONE
            holder.listText!!.visibility = View.VISIBLE
            holder.cardView!!.visibility = View.VISIBLE
            holder.task = null
        }
        holder.itemView.tag = mItemList[position]
        val photos = task.getPhotos()
        if (photos.isEmpty()) {
            holder.image!!.visibility = View.GONE
        } else {
            holder.image!!.setImageBitmap(photos[0])
        }
        val tags=task.getTags()
        if (tags!!.isEmpty()){
            holder.tags!!.visibility=View.GONE
        }else {
            holder.tags!!.layoutManager = GridLayoutManager(context, tags.size)
            holder.tags!!.adapter = TagsAdapter(tags as List<Tag>?, presenter, task,false)
        }

    }

    override fun getUniqueItemId(position: Int): Long {
        return mItemList[position]!!.first
    }

    inner class ViewHolder(itemView: View) :
        DragItemAdapter.ViewHolder(itemView, mGrabHandleId, mDragOnLongPress) {
        var mText: TextView? = null
        var listText: TextView? = null
        var cardView: CardView? = null
        var cardViewTask: CardView? = null
        var image: ImageView? = null
        var task: Task? = null
        var tags:RecyclerView?=null
        private var optionsMenu: ImageButton? = null

        init {
            mText = itemView.findViewById<View>(R.id.text) as TextView
            listText = itemView.findViewById<View>(R.id.text2) as TextView
            cardView = itemView.findViewById<View>(R.id.cardTitle) as CardView
            cardViewTask = itemView.findViewById(R.id.card2) as CardView
            image = itemView.findViewById(R.id.imageTaskInside) as ImageView
            optionsMenu = itemView.findViewById<View>(R.id.options) as ImageButton
            tags = itemView.findViewById(R.id.recycler_tags_inside)
            if (rol==Rol.WATCHER){
                optionsMenu!!.visibility=View.GONE
            }
            optionsMenu!!.setOnClickListener {
                val bottomSheet = BottomSheet(boardName, listText!!.text.toString(), presenter, "menu_task")
                bottomSheet.task=task
                bottomSheet.show(supportFragmentManager, "bottomSheet")

            }
        }

        override fun onItemClicked(view: View) {
            if (task != null) {
                val nagDialog2 = Dialog(context, android.R.style.ThemeOverlay_Material_Dark)
                nagDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE)
                nagDialog2.setCancelable(false)
                nagDialog2.setContentView(R.layout.task_extend_tabs)
                val tabLayout: TabLayout = nagDialog2.findViewById(R.id.tabsDialog)
                val viewPager: ViewPager2 = nagDialog2.findViewById(R.id.view_pager)
                val toolbar: Toolbar = nagDialog2.findViewById(R.id.toolbar)
                toolbar.setNavigationOnClickListener { nagDialog2.dismiss() }

                val adapter = TaskFragmentManagerDialog(context as FragmentActivity, toolbar,task!!,boardName,boardId,supportFragmentManager,this,rol)

                viewPager.adapter = adapter
                val mediator =
                    TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->

                        when (position) {
                            0 -> {
                                tab.text = "Detalles"
                            }
                            else -> {
                                tab.text = "Comentarios"
                            }
                        }
                    }
                mediator.attach()
                nagDialog2.show()
            }

        }

        override fun onItemLongClicked(view: View): Boolean {
            return true
        }

        fun updateTask(t: Task) {
            task = t
            mText!!.text = task!!.getTitle()
        }

    }

    init {
        itemList = list
    }
}