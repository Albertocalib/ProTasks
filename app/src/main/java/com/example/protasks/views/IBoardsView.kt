package com.example.protasks.views

import com.example.protasks.models.Board

interface IBoardsView {
    fun getBoards(username:String)
    fun setBoards(boards:List<Board>)
}