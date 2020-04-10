package com.example.protasks

import android.util.Base64;
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Board


class BoardAdapter(private val boards : List<Board>?) :RecyclerView.Adapter<BoardAdapter.ViewHolderBoard>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoardAdapter.ViewHolderBoard {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.board, parent, false)
        return ViewHolderBoard(view)
    }

    override fun getItemCount(): Int {
        return boards!!.size
    }

    override fun onBindViewHolder(holder: BoardAdapter.ViewHolderBoard, position: Int) {
        holder.boardName.text=boards!![position].getName()
        val imageBytes = Base64.decode(boards[position].getPhoto(), Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        holder.boardImage.setImageBitmap(decodedImage)
    }

    class ViewHolderBoard(v: View) : RecyclerView.ViewHolder(v) {
        var boardName: TextView
        var boardImage: ImageView
        var view:View=v

        init {
            boardName = view.findViewById(R.id.boardName)
            boardImage = view.findViewById(R.id.boardView)
        }
    }
}