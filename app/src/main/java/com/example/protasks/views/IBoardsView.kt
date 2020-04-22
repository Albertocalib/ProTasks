package com.example.protasks.views

import com.example.protasks.models.Board
import com.example.protasks.models.User

interface IBoardsView {
    fun setBoards(boards:List<Board>)
    fun setUser(user:User)
    fun logOut()
}