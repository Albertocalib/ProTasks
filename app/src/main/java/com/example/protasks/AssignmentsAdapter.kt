package com.example.protasks

import android.graphics.*
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.Board
import com.example.protasks.models.User
import com.example.protasks.utils.ImageHandler


class AssignmentsAdapter(private val assignments : List<User>?) :RecyclerView.Adapter<AssignmentsAdapter.ViewHolderAssignment>(){

    private val imageHandler: ImageHandler = ImageHandler()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssignmentsAdapter.ViewHolderAssignment {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.assignment_element, parent, false)
        return ViewHolderAssignment(view)
    }

    override fun getItemCount(): Int {
        return assignments!!.size
    }

    override fun onBindViewHolder(holder: AssignmentsAdapter.ViewHolderAssignment, position: Int) {
        val name= assignments!![position].getName()+' '+assignments[position].getSurname()
        holder.userName.text=name
        val photo=assignments[position].getPhoto()
        if (photo!=null){
            val imageBytes = Base64.decode(photo, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.userImage.setImageBitmap(decodedImage)
        }else{
            holder.userImage.setImageBitmap(imageHandler.setTextToImage(Color.LTGRAY,name))
        }

    }

    class ViewHolderAssignment(v: View) : RecyclerView.ViewHolder(v) {
        var userName: TextView
        var userImage: ImageView
        var view:View=v

        init {
            userName = view.findViewById(R.id.userName)
            userImage = view.findViewById(R.id.userImage)
        }
    }
    fun getUsers():List<User>{
        return this.assignments!!
    }

}