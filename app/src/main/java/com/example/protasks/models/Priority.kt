package com.example.protasks.models

import android.graphics.Color
import java.lang.Exception


enum class Priority {
    NO_PRIORITY("No Priority",-1,0),
    LOW("Low",Color.GRAY,1),
    NORMAL("Normal",Color.GREEN,2),
    HIGH("High",Color.YELLOW,3),
    URGENT("Urgent",Color.RED,4);

    fun getIndex(): Int {
        return this.index!!
    }

    // custom properties with default values
    private var printableName : String? = null
    private var color: Int? = null
    private var index: Int? =null

    constructor()

    // custom constructors
    constructor(
        printableName: String,
        color: Int,
        index:Int
    ) {
        this.color = color
        this.printableName = printableName
        this.index=index
    }

    companion object{
        fun getColors():IntArray{
            val values = values()
            val colors = IntArray(values.size)
            values.forEachIndexed { index, element ->
                colors[index] = element.color!!
            }
            return colors
        }

        fun getNames():Array<String>{
            val values = values()
            val names=Array(values().size) { "" }
            values.forEachIndexed { index, element ->
                names[index] = element.printableName!!
            }
            return names
        }
        fun getPriorityByPrintableName(printableName: String):Priority{
            for (p in values()){
                if (p.printableName==printableName){
                    return p
                }
            }
            throw Exception("There is no Priority with this printable name")
        }

    }

}

