package com.example.protasks.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BoardUsersPermId(
    @Expose
    @SerializedName("boardId")
    private var boardId: Long?, @Expose
    @SerializedName("userId")
    private var userId: Long?
) {

    fun getBoardId(): Long? {
        return boardId
    }

    fun setBoardId(boardId: Long?) {
        this.boardId = boardId
    }

    fun getUserId(): Long? {
        return userId
    }

    fun setUserId(userId: Long?) {
        this.userId = userId
    }

}