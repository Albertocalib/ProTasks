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


class BoardAdapterMenu(private val boards : List<Board>?, private val view:Int) :RecyclerView.Adapter<BoardAdapterMenu.ViewHolderBoard>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BoardAdapterMenu.ViewHolderBoard {
        val view: View =
            LayoutInflater.from(parent.context).inflate(view, parent, false)
        return ViewHolderBoard(view)
    }

    override fun getItemCount(): Int {
        return boards!!.size
    }


    override fun onBindViewHolder(holder: BoardAdapterMenu.ViewHolderBoard, position: Int) {
        holder.boardName.text=boards!![position].getName()
    }

    class ViewHolderBoard(v: View) : RecyclerView.ViewHolder(v) {
        var boardName: TextView
        var view:View=v

        init {
            boardName = view.findViewById(R.id.boardNameMenu)
        }
    }
}