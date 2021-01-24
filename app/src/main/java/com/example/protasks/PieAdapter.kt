package com.example.protasks

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate


class PieAdapter(
    private val tasks: HashMap<String, HashMap<String, Int>>?,
    private val users: ArrayList<String>
) : RecyclerView.Adapter<PieAdapter.ViewHolderPie>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderPie {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.pie_chart, parent, false)
        return ViewHolderPie(view)
    }

    override fun getItemCount(): Int {
        return tasks!!.size
    }

    override fun onBindViewHolder(holder: ViewHolderPie, position: Int) {
        val t = tasks!![users[position]]
        holder.userName.text = users[position]
        val pieEntries: MutableList<PieEntry> = ArrayList()
        val legendEntries: MutableList<LegendEntry> = ArrayList()
        var cont=0
        val max = ColorTemplate.JOYFUL_COLORS.size
        for ((key, value) in t!!.entries) {
            cont %= max
            pieEntries.add(PieEntry(value.toFloat(), key))
            val le=LegendEntry()
            le.label= "$value TAREA EN $key"
            le.formColor = ColorTemplate.JOYFUL_COLORS[cont]
            cont++
            legendEntries.add(le)
        }
        val dataSet = PieDataSet(pieEntries,"")
        dataSet.sliceSpace = 3F
        dataSet.selectionShift = 5F
        dataSet.colors = ColorTemplate.JOYFUL_COLORS.asList()
        dataSet.setDrawValues(true)
        dataSet.valueFormatter = PercentFormatter()
        val pieData = PieData(dataSet)
        pieData.setValueTextSize(25F)
        pieData.setValueTextColor(Color.YELLOW)
        holder.pieChart.data = pieData
        val l: Legend = holder.pieChart.legend
        l.form = Legend.LegendForm.SQUARE
        l.orientation=Legend.LegendOrientation.VERTICAL
        l.setCustom(legendEntries)
        l.textSize = 13F
        holder.pieChart.invalidate()


    }

    class ViewHolderPie(v: View) : RecyclerView.ViewHolder(v) {
        var pieChart: PieChart = v.findViewById(R.id.pie_chart)
        var userName: TextView = v.findViewById(R.id.userName)

        init {
            pieChart.setUsePercentValues(true)
            pieChart.description.isEnabled = false
            pieChart.setExtraOffsets(5F, 10F, 5F, 10F)
            pieChart.dragDecelerationFrictionCoef = 0.95F
            pieChart.isDrawHoleEnabled = true
            pieChart.transparentCircleRadius = 41F
            pieChart.setEntryLabelTextSize(25F)
            pieChart.centerText="TAREAS"
            pieChart.setCenterTextSize(25F)

        }

    }
}