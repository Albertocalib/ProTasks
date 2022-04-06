package com.example.protasks

import com.example.protasks.utils.XAxisValueFormatter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Task
import com.example.protasks.models.TaskList
import com.example.protasks.models.User
import com.example.protasks.presenters.TaskListPresenter
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Description
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

import com.github.mikephil.charting.components.XAxis.XAxisPosition

import com.github.mikephil.charting.data.BarData

import com.github.mikephil.charting.data.BarDataSet

import com.github.mikephil.charting.data.BarEntry
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream


class StatsFragment(
    private val taskLists: List<TaskList>, private val presenter: TaskListPresenter,
    private val supportFragmentManager: FragmentManager, private val boardName: String
) :
    Fragment() {
    private var recyclerView: RecyclerView? = null
    private var mColumns = 0
    private var listMap: HashMap<String, Long>? = HashMap()
    var boardId: Long = 0
    private var chartLead: HorizontalBarChart? = null
    private var chartCycle: HorizontalBarChart? = null
    private var tasks: ArrayList<Task>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.stats_fragment, container, false)
        recyclerView = view.findViewById(R.id.recycler_pie_chart_list)
        recyclerView!!.layoutManager = GridLayoutManager(context, 1)
        val tasks_dict: HashMap<String, HashMap<String, Int>>? = HashMap()
        val users: ArrayList<String>? = ArrayList()
        val user: User =
            User("No Asignado", "No asignado", "No asignado", "No asignado", "No asignado")
        for (list in taskLists) {
            for (t in list.getTasks()!!) {
                tasks!!.add(t!!)
                if (t!!.getUsers()!!.isNotEmpty()) {
                    for (u in t.getUsers()!!) {
                        if (tasks_dict!!.containsKey(u.getUsername())) {
                            if (tasks_dict[u.getUsername()]!!.contains(list.getTitle())) {
                                tasks_dict[u.getUsername()]!![list.getTitle()!!] =
                                    tasks_dict[u.getUsername()]!![list.getTitle()!!]!! + 1
                            } else {
                                tasks_dict[u.getUsername()]!![list.getTitle()!!] = 1
                            }
                        } else {
                            val h: HashMap<String, Int> = HashMap()
                            h[list.getTitle()!!] = 1
                            tasks_dict[u.getUsername()!!] = h
                            users!!.add(u.getUsername()!!)
                        }
                    }
                } else {
                    if (tasks_dict!!.containsKey(user.getUsername())) {
                        if (tasks_dict[user.getUsername()]!!.contains(list.getTitle())) {
                            tasks_dict[user.getUsername()]!![list.getTitle()!!] =
                                tasks_dict[user.getUsername()]!![list.getTitle()!!]!! + 1
                        } else {
                            tasks_dict[user.getUsername()]!![list.getTitle()!!] = 1
                        }
                    } else {
                        val h: HashMap<String, Int> = HashMap()
                        h[list.getTitle()!!] = 1
                        tasks_dict[user.getUsername()!!] = h
                        users!!.add(user.getUsername()!!)
                    }
                }

            }
        }
        recyclerView!!.adapter = PieAdapter(tasks_dict, users!!)
        chartCycle = view.findViewById(R.id.chartcycle)
        setHorizontalGraph(chartCycle!!,"cycle")
        chartLead = view.findViewById(R.id.chartlead)
        setHorizontalGraph(chartLead!!,"lead")


        return view


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setHorizontalGraph(chart:HorizontalBarChart,type:String) {
        val colorList = arrayListOf(Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW,Color.MAGENTA)
        chart.setDrawBarShadow(false)
        val description = Description()
        description.text = ""
        chart.description = description
        chart.legend.isEnabled = false
        chart.setPinchZoom(false)
        chart.setDrawValueAboveBar(false)
        chart.setScaleEnabled(false)

        val boarddate = taskLists[0].getBoard()!!.getCreatedDate()
        val dates = datesBetweenTwoDates(boarddate!!,Date())


        val xAxis = chart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.isEnabled = true
        xAxis.setDrawAxisLine(false)
        val taskNames = ArrayList<String>()
        //Set bar entries and add necessary formatting
        val entries = ArrayList<BarEntry>()
        val colors: ArrayList<Int> = ArrayList()
        tasks!!.forEach { item ->
            val start = if (type=="cycle") item.getDateStartCycleTime() else item.getDateStartLeadTime()
            var end =  if (type=="cycle") item.getDateEndCycleTime() else item.getDateEndLeadTime()
            if (start==null){
                return@forEach
            }
            taskNames.add(item.getTitle()!!)
            if (end==null){
                end=Date()
            }
            val startNumDays = numDaysBetweenTwoDates(boarddate,start)
            var numDays = numDaysBetweenTwoDates(start,end)
            if (numDays==0f){
                numDays = 0.1F
            }
            if (startNumDays>0)
                colors.add(Color.TRANSPARENT)
            val color = colorList.removeAt(0)
            colors.add(color)
            colorList.add(color)
            val entry = BarEntry(entries.size.toFloat(), floatArrayOf(startNumDays,numDays))
            entries.add(entry)
        }
        xAxis.labelCount = taskNames.size
        xAxis.granularity =1f
        xAxis.valueFormatter = XAxisValueFormatter(taskNames.toTypedArray())
        xAxis.setDrawLabels(true)
        val yLeft = chart.axisLeft

        yLeft.axisMaximum = dates!!.size.toFloat() - 1f
        yLeft.axisMinimum = 0f
        yLeft.isEnabled = false

        yLeft.labelCount = dates.size
        yLeft.granularity = 1f


        val yRight = chart.axisRight
        yRight.setDrawAxisLine(true)
        yRight.setDrawGridLines(true)
        yRight.isEnabled = true
        yRight.axisMinimum = 0f
        yRight.axisMaximum = dates.size.toFloat() - 1
        yRight.labelCount = dates.size
        yRight.granularity = 1f
        yRight.valueFormatter = XAxisValueFormatter(dates.toTypedArray())


        val barDataSet = BarDataSet(entries, "Bar Data Set")
        barDataSet.colors = colors
        chart.setDrawBarShadow(false)
        barDataSet.isHighlightEnabled=false
        barDataSet.setDrawValues(false)
        barDataSet.barShadowColor = Color.TRANSPARENT
        val data = BarData(barDataSet)
        data.barWidth = 0.9f

        chart.data = data
        chart.minimumHeight = entries.size * 200

        chart.invalidate()

        //Add animation to the graph
        chart.animateY(500)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun datesBetweenTwoDates(startDate: Date, endDate:Date): MutableList<String>? {
        val createdLocalDate= convertDate(startDate)
        val endLocalDate = convertDate(endDate)
        val numOfDaysBetween = ChronoUnit.DAYS.between(createdLocalDate, endLocalDate)
        return IntStream.iterate(0) { i -> i + 1 }
            .limit(numOfDaysBetween + 1)
            .mapToObj { i -> createdLocalDate.plusDays(i.toLong()).toString() }
            .collect(Collectors.toList())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun numDaysBetweenTwoDates(startDate: Date, endDate:Date): Float {
        val createdLocalDate= convertDate(startDate)
        val endLocalDate = convertDate(endDate)
        return ChronoUnit.DAYS.between(createdLocalDate, endLocalDate).toFloat()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDate(date:Date): LocalDate{
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    companion object {
        private var sCreatedItems = 0
        fun newInstance(
            taskLists: List<TaskList>,
            presenter: TaskListPresenter,
            supportFragmentManager: FragmentManager,
            boardName: String
        ): StatsFragment {
            return StatsFragment(taskLists, presenter, supportFragmentManager, boardName)
        }
    }

}