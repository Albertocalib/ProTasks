package com.example.protasks.models

interface ITask {
    fun getTitle(): String?

    fun getDescription(): String?

    fun getPosition(): Int?

    fun getId(): Int?

    fun setTitle(title:String)

    fun setDescription(description:String)

    fun setPosition(position:Int)


}