package com.example.protasks

import android.util.Base64
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Board


class BoardAdapter(private val boards : List<Board>?,private val view:Int, val itemClickListener: OnItemClickListener) :RecyclerView.Adapter<BoardAdapter.ViewHolderBoard>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoardAdapter.ViewHolderBoard {
        val view: View =
            LayoutInflater.from(parent.context).inflate(view, parent, false)
        return ViewHolderBoard(view,itemClickListener)
    }

    override fun getItemCount(): Int {
        return boards!!.size
    }

    override fun onBindViewHolder(holder: BoardAdapter.ViewHolderBoard, position: Int) {
        holder.boardName.text=boards!![position].getName()
        val imageBytes = Base64.decode(boards[position].getPhoto(), Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.boardImage.setImageBitmap(decodedImage)
        holder.boardId=boards[position].getId()
    }

    class ViewHolderBoard(v: View,clickListener: OnItemClickListener) : RecyclerView.ViewHolder(v) {
        var boardName: TextView
        var boardImage: ImageView
        var boardId:Long?=null
        var view:View=v

        init {
            boardName = view.findViewById(R.id.boardName)
            boardImage = view.findViewById(R.id.boardView)
            view.setOnClickListener {
                clickListener.onItemClicked(boardId,boardName.text.toString())
            }
        }
    }
    fun getBoards():List<Board>{
        return this.boards!!
    }
    interface OnItemClickListener{
        fun onItemClicked(boardId: Long?,boardName: String)
    }
}