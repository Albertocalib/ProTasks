package com.example.protasks.presenters.board

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import com.example.protasks.models.*
import com.example.protasks.utils.ImageHandler
import com.example.protasks.utils.PreferencesManager
import java.io.ByteArrayOutputStream


class BoardPresenter(private var boardView: IBoardContract.View, private val preference: PreferencesManager) :
    IBoardContract.IBoardPresenter,IBoardContract.Model.OnFinishedListener {

    private val boardModel: BoardModel = BoardModel()
    private val imageHandler: ImageHandler = ImageHandler()

    override fun getBoards() {
        val username = preference.getEmail()
        boardModel.getBoards(this, username!!)
    }

    override fun getUser() {
        boardModel.getUser(this,preference.getEmail()!!)
    }

    override fun getViewPref(): Boolean {
        return preference.getPrefViewMode()

    }

    override fun getPhoto(u: User): Bitmap {
        val imageBytes = Base64.decode(u.getPhoto(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    override fun removePreferences() {
        preference.removePreferences()
    }

    override fun setViewPref(listMode: Boolean) {
        preference.setPrefViewMode(listMode)
    }

    override fun filterBoards(name: String) {
        boardModel.filterBoards(this,name,preference.getEmail()!!)
    }

    override fun removeTag(taskId: Long, tagId: Long, update: Boolean) {
        TODO("Not yet implemented")
    }

    override fun downloadImage(image: ImageView, context: Context) {
        imageHandler.downloadImage(image, context)
    }

    override fun setImage(uri: Uri, context: Context) {
        val imageStream = context.contentResolver.openInputStream(uri)
        val thumbnail = BitmapFactory.decodeStream(imageStream)
        val img = bitmap2Base64(thumbnail)
        val username = preference.getEmail()
        boardModel.setImage(this, img, username!!)
    }

    override fun createBoard(name: String, b: Bitmap) {
        val img = bitmap2Base64(b)
        val b1 = Board()
        b1.setPhoto(img)
        b1.setName(name)
        boardModel.createBoard(this,b1,preference.getEmail()!!)
    }

    override fun bitmap2Base64(b: Bitmap): String {
        val baos = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.PNG, 60, baos)
        val b1: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b1, Base64.NO_WRAP)
    }

    override fun createTaskList(boardName: String, listName: String) {
        val t = TaskList()
        t.setTitle(listName)
        boardModel.createTaskList(this,boardName,t, preference.getEmail()!!)
    }

    override fun createTask(boardName: String, taskName: String, listName: String, description: String) {
        val t = Task(taskName, description)
        boardModel.createTask(this,boardName,listName,t,preference.getEmail()!!)

    }

    override fun updateWIP(checked: Boolean, board: Board?, wipLimit: String, wipList: String) {
        var wipLimitConv= 0
        if (wipLimit!="") {
            wipLimitConv = wipLimit.toInt()
        }
        boardModel.updateWIP(this,checked,board!!.getId(),wipLimitConv, wipList)

    }

    override fun addUserToBoard(boardId: Long, username: String, role: Rol) {
        boardModel.addUserToBoard(this,boardId,username,role)
    }
    override fun updateRole(role: String, userId:Long, boardId: Long ) {
        val roleFormatted:Rol = when (role) {
            "Administrador" -> {
                Rol.ADMIN
            }
            "Invitado" -> {
                Rol.USER
            }
            else -> {
                Rol.WATCHER
            }
        }
        boardModel.updateRole(this,boardId,userId,roleFormatted)

    }

    override fun deleteUserFromBoard(userId:Long, boardId: Long ) {
        boardModel.deleteUserFromBoard(this,userId,boardId)

    }
    override fun getRole(userId:Long, boardId:Long){
        boardModel.getRole(this,userId,boardId)
    }

    override fun updateTime(checked: Boolean, board: Board?, cycleStart: String, cycleEnd: String, leadStart: String, leadEnd: String) {
        boardModel.updateTime(this,checked,board,cycleStart,cycleEnd,leadStart,leadEnd)
    }

    override fun onFinishedGetBoards(successful: Boolean, code: Int, boards: List<Board>?) {
        if (successful){
            boardView.setBoards(boards!! as ArrayList<Board>)
        }
    }

    override fun onFinishedGetUser(successful: Boolean, code: Int, user: User?) {
        if (successful){
            boardView.setUser(user!!)
        }
    }

    override fun onFinishedSetRole(
        successful: Boolean,
        code: Int,
        boardUsersPermRel: BoardUsersPermRel?
    ) {
        if (successful){
            boardView.setRole(boardUsersPermRel!!)
        }
    }

    override fun onFinishedCreateBoard(successful: Boolean, code: Int, board: Board?) {
        if (successful){
            boardView.addBoard(board!!)
            boardView.showToast("Board Created")
        }
    }

    override fun onFinishedCreateTaskOrList(successful: Boolean, code: Int, toastMessage: String) {
        getBoards()
        boardView.showToast(toastMessage)
    }

    override fun onFinishedSetBoard(successful: Boolean, code: Int, board: Board?) {
        if (code != 201 && code != 200) {
            boardView.showToast("El usuario indicado no existe")
        }else if (successful) {
            boardView.setBoard(board!!)
        }
    }

    override fun onFailure(t: Throwable?) {
        boardView.onResponseFailure(t)
    }


}