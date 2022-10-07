package com.example.protasks.presenters.board

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.example.protasks.models.*
import com.example.protasks.presenters.IPresenter

interface IBoardContract {

    interface Model {
        interface OnFinishedListener {
            fun onFinishedGetBoards(successful: Boolean, code: Int, boards: List<Board>?)
            fun onFinishedGetUser(successful: Boolean, code: Int, user: User?)
            fun onFinishedSetRole(successful: Boolean, code: Int, boardUsersPermRel: BoardUsersPermRel?)
            fun onFinishedCreateBoard(successful: Boolean, code: Int, board: Board?)
            fun onFinishedCreateTaskOrList(successful: Boolean, code: Int, toastMessage: String)
            fun onFinishedSetBoard(successful: Boolean, code: Int, board: Board?)
            fun onFailure(t: Throwable?)
        }

        fun getBoards(onFinishedListener: OnFinishedListener?, username: String)
        fun getUser(onFinishedListener: OnFinishedListener?, username: String)
        fun filterBoards(onFinishedListener: OnFinishedListener?, name: String, username: String)
        fun setImage(onFinishedListener: OnFinishedListener?, img:String, username: String)
        fun createBoard(onFinishedListener: OnFinishedListener?, b:Board, username: String)
        fun createTaskList(onFinishedListener: OnFinishedListener?, boardName: String, taskList: TaskList, username: String)
        fun createTask(onFinishedListener: OnFinishedListener?, boardName: String, listName: String, task: Task, username: String)
        fun updateWIP(onFinishedListener: OnFinishedListener?, checked: Boolean, boardId: Long, wipLimit: Int, wipList: String)
        fun addUserToBoard(onFinishedListener: OnFinishedListener?, boardId: Long, username: String, role: Rol)
        fun updateRole(onFinishedListener: OnFinishedListener?, boardId: Long, userId: Long, role: Rol)
        fun deleteUserFromBoard(onFinishedListener: OnFinishedListener?, userId: Long, boardId: Long)
        fun getRole(onFinishedListener: OnFinishedListener?, userId: Long, boardId: Long)
        fun updateTime(
            onFinishedListener: OnFinishedListener?,
            checked: Boolean,
            board: Board?,
            cycleStart: String,
            cycleEnd: String,
            leadStart: String,
            leadEnd: String
        )
    }

    interface View {
        fun onResponseFailure(t: Throwable?)
        fun setBoards(boards:ArrayList<Board>)
        fun setUser(user:User)
        fun getBoards()
        fun setBoard(board: Board)
        fun setRole(perm:BoardUsersPermRel)
        fun showToast(message:String)
        fun addBoard(board:Board)
    }

    interface IBoardPresenter : IPresenter {
        fun getBoards()
        fun getUser()
        fun getViewPref(): Boolean
        fun getPhoto(u: User): Bitmap
        fun removePreferences()
        fun setViewPref(listMode: Boolean)
        fun filterBoards(name: String)
        fun downloadImage(image: ImageView, context: Context)
        fun setImage(uri: Uri, context: Context)
        fun createBoard(name: String, b: Bitmap)
        fun bitmap2Base64(b: Bitmap): String
        fun createTaskList(boardName: String, listName: String)
        fun createTask(boardName: String, taskName: String, listName: String, description: String)
        fun updateWIP(checked: Boolean, board: Board?, wipLimit: String, wipList: String)
        fun addUserToBoard(boardId: Long, username: String, role: Rol)
        fun updateRole(role: String, userId: Long, boardId: Long)
        fun deleteUserFromBoard(userId: Long, boardId: Long)
        fun getRole(userId: Long, boardId: Long)
        fun updateTime(
            checked: Boolean,
            board: Board?,
            cycleStart: String,
            cycleEnd: String,
            leadStart: String,
            leadEnd: String
        )

    }
}
