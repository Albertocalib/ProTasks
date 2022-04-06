package com.example.protasks

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Board
import com.example.protasks.models.Task
import com.example.protasks.models.TaskList
import com.example.protasks.presenters.TaskListPresenter
import com.example.protasks.utils.BottomSheet
import com.woxthebox.draglistview.BoardView
import com.woxthebox.draglistview.BoardView.BoardCallback
import com.woxthebox.draglistview.BoardView.BoardListener
import com.woxthebox.draglistview.ColumnProperties
import com.woxthebox.draglistview.DragItem
import java.util.*
import kotlin.collections.HashMap
import android.widget.Toast
import com.example.protasks.models.Rol
import kotlinx.android.synthetic.main.user_element.*
import java.lang.Exception


class BoardFragment : Fragment() {
    private var taskLists: List<TaskList>? = null
    private var presenter:TaskListPresenter? = null
    private var supportFragmentManager:FragmentManager? = null
    private var boardName:String? = null
    private var mBoardView: BoardView? = null
    private var mColumns = 0
    private var listMap :HashMap<String,Long>? = HashMap()
    var board: Board?=null
    var boardId:Long=0
    private var toast:Toast?=null
    var rol:Rol?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.board_layout, container, false)
        mBoardView = view.findViewById(R.id.board_view)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        taskLists = arguments?.getParcelableArrayList("taskLists")
        boardName = arguments?.getString("boardName")
        presenter = TaskListPresenter(null,requireContext())
        supportFragmentManager = this.fragmentManager
        mBoardView!!.setSnapToColumnsWhenScrolling(true)
        mBoardView!!.setSnapToColumnWhenDragging(true)
        mBoardView!!.setSnapDragItemToTouch(true)
        mBoardView!!.setSnapToColumnInLandscape(false)
        mBoardView!!.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER)
        mBoardView!!.setBoardListener(object : BoardListener {
            override fun onItemDragStarted(column: Int, row: Int) {
                //Toast.makeText(getContext(), "Start - column: " + column + " row: " + row, Toast.LENGTH_SHORT).show();
            }

            override fun onItemDragEnded(
                fromColumn: Int,
                fromRow: Int,
                toColumn: Int,
                toRow: Int
            ) {
                val toColumnName = mBoardView!!.getHeaderView(toColumn).findViewById<TextView>(R.id.text).text
                if (fromColumn != toColumn || fromRow != toRow) {
                    val task =
                        mBoardView!!.getAdapter(toColumn).itemList[toRow] as Triple<*, *,*>
                    val id= listMap!![toColumnName]
                    presenter!!.updateTaskPosition(task.first!! as Long,toRow.toLong()+1,id!!,false)
                    if (taskLists!!.size>toColumn) {
                        val itemCount1 =
                            mBoardView!!.getHeaderView(fromColumn)
                                .findViewById<TextView>(R.id.item_count)
                        itemCount1.text = mBoardView!!.getAdapter(fromColumn).itemCount.toString()
                        val itemCount2 =
                            mBoardView!!.getHeaderView(toColumn)
                                .findViewById<TextView>(R.id.item_count)
                        itemCount2.text = mBoardView!!.getAdapter(toColumn).itemCount.toString()
                    }

                }
            }

            override fun onItemChangedPosition(
                oldColumn: Int,
                oldRow: Int,
                newColumn: Int,
                newRow: Int
            ) {
                //Toast.makeText(mBoardView.getContext(), "Position changed - column: " + newColumn + " row: " + newRow, Toast.LENGTH_SHORT).show();
            }

            override fun onItemChangedColumn(oldColumn: Int, newColumn: Int) {

            }

            override fun onFocusedColumnChanged(
                oldColumn: Int,
                newColumn: Int
            ) {
                //Toast.makeText(getContext(), "Focused column changed from " + oldColumn + " to " + newColumn, Toast.LENGTH_SHORT).show();
            }

            override fun onColumnDragStarted(position: Int) {
                return
                //Toast.makeText(getContext(), "Column drag started from " + position, Toast.LENGTH_SHORT).show();
            }

            override fun onColumnDragChangedPosition(
                oldPosition: Int,
                newPosition: Int
            ) {
                //Toast.makeText(getContext(), "Column changed from " + oldPosition + " to " + newPosition, Toast.LENGTH_SHORT).show();
            }

            override fun onColumnDragEnded(position: Int) {
                val columnName = mBoardView!!.getHeaderView(position).findViewById<TextView>(R.id.text).text
                val id= listMap!![columnName]
                presenter!!.updateTaskListPosition(id!!, position.toLong()+1)
            }
        })
        mBoardView!!.setBoardCallback(object : BoardCallback {
            override fun canDragItemAtPosition(
                column: Int,
                dragPosition: Int
            ): Boolean {
                // Add logic here to prevent an item to be dragged
                if (rol===Rol.WATCHER){
                    return false
                }
                return true
            }

            override fun canDropItemAtPosition(
                oldColumn: Int,
                oldRow: Int,
                newColumn: Int,
                newRow: Int
            ): Boolean {
                // Add logic here to prevent an item to be dropped
                if (taskLists!!.size==newColumn){
                    return false
                }
                val columnName = mBoardView!!.getHeaderView(newColumn).findViewById<TextView>(R.id.text).text
                if (oldColumn!=newColumn && board!=null && board!!.getWipActivated() &&
                    columnName==board!!.getWipList() && mBoardView!!.getAdapter(newColumn).itemCount>=board!!.getWipLimit()){
                    mBoardView!!.getHeaderView(newColumn).setBackgroundColor(Color.RED)
                    if (toast==null){
                        toast = Toast.makeText(context, "WIP Superado, no puedes añadir más tareas a esta columna", Toast.LENGTH_SHORT)
                    }
                    if (!toast!!.view!!.isShown){
                        toast!!.show()
                    }
                    Handler().postDelayed({
                        mBoardView!!.getHeaderView(newColumn).setBackgroundColor(Color.TRANSPARENT)
                    }, 1000)
                    return false
                }
                return true
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        resetBoard()
    }

    private fun resetBoard() {
        mBoardView!!.clearBoard()
        mBoardView!!.setCustomDragItem(MyDragItem(activity, R.layout.column_item))
        mBoardView!!.setCustomColumnDragItem(
            MyColumnDragItem(
                activity,
                R.layout.column_drag_layout
            )
        )
        for (list in taskLists!!) {
            addColumn(list)
        }
        if (rol!==Rol.WATCHER){
            addColumnCreateColumn()
        }

    }

    private fun addColumn(list: TaskList) {
        val mItemArray =
            ArrayList<Triple<Long, Task,Boolean>>()
        val tasks=list.getTasks()!!.sortedWith(compareBy { it!!.getPosition() })
        for (task in tasks) {
            task!!.setTaskList(list)
            mItemArray.add(
                Triple(
                    task.getId() as Long,
                    task,
                    false
                )
            )
        }
        if (taskLists!!.isNotEmpty()){
            board= taskLists!![0].getBoard()!!
            boardId=board!!.getId()
        }
        val listAdapter =
            TaskAdapterInsideBoard(mItemArray, false,R.layout.column_item, R.id.item_layout, true,supportFragmentManager!!,boardName!!,boardId,presenter!!,rol)
        val header =
            View.inflate(activity, R.layout.column_header, null)
        (header.findViewById<View>(R.id.text) as TextView).text = list.getTitle()
        (header.findViewById<View>(R.id.item_count) as TextView).text ="""${list.getTasks()!!.size}"""
        listMap!![list.getTitle()!!] = list.getId()
        val buttonAddTask= (header.findViewById<View>(R.id.btnAddTask) as Button)
        buttonAddTask.setOnClickListener { v ->
            val columnName = (header.findViewById<TextView>(R.id.text)).text
            val bottomSheet = BottomSheet(boardName!!,columnName.toString(),presenter!!,"task")
            bottomSheet.show(supportFragmentManager!!, "bottomSheet")
            (header.findViewById<View>(R.id.item_count) as TextView).text =
                mItemArray.size.toString()
        }
        val buttonOptions = (header.findViewById<View>(R.id.options) as ImageButton)
        buttonOptions.setOnClickListener { v ->
            val columnName = (header.findViewById<TextView>(R.id.text)).text
            val bottomSheet = BottomSheet(boardName!!,columnName.toString(),presenter!!,"menu")
            bottomSheet.show(supportFragmentManager!!, "bottomSheet")
        }

        val layoutManager = LinearLayoutManager(context)
        var columnProperties = ColumnProperties.Builder.newBuilder(listAdapter)
            .setLayoutManager(layoutManager)
            .setHasFixedItemSize(false)
            .setColumnBackgroundColor(Color.TRANSPARENT)
            .setItemsSectionBackgroundColor(Color.TRANSPARENT)
            .setHeader(header)
        if (rol===Rol.WATCHER){
            buttonAddTask.visibility=View.INVISIBLE
            buttonOptions.visibility=View.INVISIBLE
        }else{
            columnProperties = columnProperties.setColumnDragView(header)
        }
        mBoardView!!.addColumn(columnProperties.build())
        mColumns++
    }
    fun setWatcherVisibility(){
        resetBoard()
    }
    private fun addColumnCreateColumn() {
        val mItemArray =
            ArrayList<Triple<Long, Task,Boolean>>()
        var board:Board?=null
        var boardId:Long=0
        if (taskLists!!.isNotEmpty()){
            board= taskLists!![0].getBoard()!!
            boardId=board.getId()
        }
        val listAdapter =
            TaskAdapterInsideBoard(mItemArray, false,R.layout.column_item, R.id.item_layout_add_tasklist, false,supportFragmentManager!!,boardName!!,boardId,presenter!!,rol)
        val header =
            View.inflate(activity, R.layout.column_add_tasklist_item, null)
        val buttonAddTaskList= (header.findViewById<View>(R.id.btAddTaskList) as Button)
        buttonAddTaskList.setOnClickListener {
            val bottomSheet = BottomSheet(boardName!!,"",presenter!!,"taskList")
            bottomSheet.show(supportFragmentManager!!, "bottomSheet")
        }
        val layoutManager = LinearLayoutManager(context)
        val columnProperties = ColumnProperties.Builder.newBuilder(listAdapter)
            .setLayoutManager(layoutManager)
            .setHasFixedItemSize(false)
            .setColumnBackgroundColor(Color.TRANSPARENT)
            .setItemsSectionBackgroundColor(Color.TRANSPARENT)
            .setHeader(header)
            .setColumnDragView(header)
            .build()
        mBoardView!!.addColumn(columnProperties)
        mColumns++
    }

    private class MyColumnDragItem internal constructor(
        context: Context?,
        layoutId: Int
    ) :
        DragItem(context, layoutId) {
        override fun onBindDragView(
            clickedView: View,
            dragView: View
        ) {
            val clickedLayout = clickedView as LinearLayout
            val clickedHeader = clickedLayout.getChildAt(0)
            val clickedRecyclerView = clickedLayout.getChildAt(1) as RecyclerView
            val dragHeader =
                dragView.findViewById<View>(R.id.drag_header)
            val dragScrollView =
                dragView.findViewById<ScrollView>(R.id.drag_scroll_view)
            val dragLayout = dragView.findViewById<LinearLayout>(R.id.drag_list)
            val clickedColumnBackground = clickedLayout.background
            if (clickedColumnBackground != null) {
                ViewCompat.setBackground(dragView, clickedColumnBackground)
            }
            val clickedRecyclerBackground = clickedRecyclerView.background
            if (clickedRecyclerBackground != null) {
                ViewCompat.setBackground(dragLayout, clickedRecyclerBackground)
            }
            dragLayout.removeAllViews()
            (dragHeader.findViewById<View>(R.id.text) as TextView).text =
                (clickedHeader.findViewById<View>(
                    R.id.text
                ) as TextView).text
            (dragHeader.findViewById<View>(R.id.item_count) as TextView).text =
                (clickedHeader.findViewById<View>(
                    R.id.item_count
                ) as TextView).text
            for (i in 0 until clickedRecyclerView.childCount) {
                val view =
                    View.inflate(dragView.context, R.layout.column_item, null)
                (view.findViewById<View>(R.id.text) as TextView).text =
                    (clickedRecyclerView.getChildAt(
                        i
                    ).findViewById<View>(R.id.text) as TextView).text
                dragLayout.addView(view)
                if (i == 0) {
                    dragScrollView.scrollY = -clickedRecyclerView.getChildAt(i).top
                }
            }
            dragView.pivotY = 0f
            dragView.pivotX = clickedView.getMeasuredWidth() / 2.toFloat()
        }

        override fun onStartDragAnimation(dragView: View) {
            super.onStartDragAnimation(dragView)
            dragView.animate().scaleX(0.9f).scaleY(0.9f).start()
        }

        override fun onEndDragAnimation(dragView: View) {
            super.onEndDragAnimation(dragView)
            dragView.animate().scaleX(1f).scaleY(1f).start()
        }

        init {
            setSnapToTouch(false)
        }
    }

    private class MyDragItem internal constructor(
        context: Context?,
        layoutId: Int
    ) :
        DragItem(context, layoutId) {
        override fun onBindDragView(
            clickedView: View,
            dragView: View
        ) {
            val text =
                (clickedView.findViewById<View>(R.id.text) as TextView).text
            (dragView.findViewById<View>(R.id.text) as TextView).text = text
            val dragCard: CardView = dragView.findViewById(R.id.card2)
            val clickedCard: CardView = clickedView.findViewById(R.id.card2)
            dragCard.maxCardElevation = 40f
            dragCard.cardElevation = clickedCard.cardElevation
            // I know the dragView is a FrameLayout and that is why I can use setForeground below api level 23
            dragCard.foreground = clickedView.resources.getDrawable(R.drawable.card_view_drag_foreground)
        }

        override fun onMeasureDragView(
            clickedView: View,
            dragView: View
        ) {
            val dragCard: CardView = dragView.findViewById(R.id.card2)
            val clickedCard: CardView = clickedView.findViewById(R.id.card2)
            val widthDiff =
                dragCard.paddingLeft - clickedCard.paddingLeft + dragCard.paddingRight -
                        clickedCard.paddingRight
            val heightDiff =
                dragCard.paddingTop - clickedCard.paddingTop + dragCard.paddingBottom -
                        clickedCard.paddingBottom
            val width = clickedView.measuredWidth + widthDiff
            val height = clickedView.measuredHeight + heightDiff
            dragView.layoutParams = FrameLayout.LayoutParams(width, height)
            val widthSpec = View.MeasureSpec.makeMeasureSpec(
                width,
                View.MeasureSpec.EXACTLY
            )
            val heightSpec = View.MeasureSpec.makeMeasureSpec(
                height,
                View.MeasureSpec.EXACTLY
            )
            dragView.measure(widthSpec, heightSpec)
        }

        override fun onStartDragAnimation(dragView: View) {
            val dragCard: CardView = dragView.findViewById(R.id.card2)
            val anim =
                ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.cardElevation, 40f)
            anim.interpolator = DecelerateInterpolator()
            anim.duration = ANIMATION_DURATION.toLong()
            anim.start()
        }

        override fun onEndDragAnimation(dragView: View) {
            val dragCard: CardView = dragView.findViewById(R.id.card2)
            val anim =
                ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.cardElevation, 6f)
            anim.interpolator = DecelerateInterpolator()
            anim.duration = ANIMATION_DURATION.toLong()
            anim.start()
        }
    }

    companion object {
        fun instance(taskLists: List<TaskList>,boardName: String): BoardFragment {
            val bundle = Bundle()
            bundle.putParcelableArrayList("taskLists",taskLists as ArrayList<TaskList>)
            bundle.putString("boardName",boardName)
            return BoardFragment().apply { arguments = bundle }

        }
    }

}