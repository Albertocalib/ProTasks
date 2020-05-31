package com.example.protasks.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class BoardUsersPermRel {

    @Expose
    @SerializedName("Id")
    private var id: BoardUsersPermId? = null

    @Expose
    @SerializedName("board")
    private var board: Board? = null

    @Expose
    @SerializedName("user")
    private var user: User? = null

    @Expose
    @SerializedName("rol")
    private var rol: Rol? = null

    fun BoardUsersPermRel() {}

    fun BoardUsersPermRel(
        board: Board,
        user: User,
        rol: Rol?
    ) {
        this.board = board
        this.user = user
        this.rol = rol
        id = BoardUsersPermId(board.getId(), user.getId())
    }

    fun getId(): BoardUsersPermId? {
        return id
    }

    fun setId(id: BoardUsersPermId?) {
        this.id = id
    }

    fun getBoard(): Board? {
        return board
    }

    fun setBoard(board: Board?) {
        this.board = board
    }

    fun getUser(): User? {
        return user
    }

    fun setUser(user: User?) {
        this.user = user
    }

    fun getRol(): Rol? {
        return rol
    }

    fun setRol(rol: Rol?) {
        this.rol = rol
    }

}