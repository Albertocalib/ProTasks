package com.example.protasks

import android.accessibilityservice.GestureDescription
import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.presenters.BoardPresenter
import com.example.protasks.presenters.TaskPresenter
import com.example.protasks.utils.ImageHandler
import com.example.protasks.activities.MainActivity
import com.example.protasks.models.*


class UsersPermAdapter(private val users : List<BoardUsersPermRel>?, private val presenter:BoardPresenter, private val context: Context,private val board:Board) :RecyclerView.Adapter<UsersPermAdapter.ViewHolderUsersPerm>(){

    private val imageHandler: ImageHandler = ImageHandler()
    private var parentGroup:ViewGroup?=null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersPermAdapter.ViewHolderUsersPerm {
        parentGroup=parent
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.user_element, parent, false)
        return ViewHolderUsersPerm(view)
    }

    override fun getItemCount(): Int {
        return users!!.size
    }

    override fun onBindViewHolder(holder: UsersPermAdapter.ViewHolderUsersPerm, position: Int) {
        val user = users!![position].getUser()
        val name= user!!.getName()+' '+user.getSurname()
        holder.userName.text=name
        holder.role.text=users[position].getRol().toString()
        val photo=user.getPhoto()
        if (photo!=null){
            val imageBytes = Base64.decode(photo, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.userImage.setImageBitmap(decodedImage)
        }else{
            holder.userImage.setImageBitmap(imageHandler.setTextToImage(Color.LTGRAY,name))
        }
        val rol=users[position].getRol()
        if (rol!=Rol.OWNER){
            holder.view.setOnClickListener {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                val dialogView: View =
                    LayoutInflater.from(context).inflate(R.layout.user_extend_perm, parentGroup, false)
                val username = dialogView.findViewById<TextView>(R.id.userName_perm)
                username.text=name
                val boardName = dialogView.findViewById<TextView>(R.id.board_perm)
                boardName.text=board.getName()
                val radioGroup = dialogView.findViewById<RadioGroup>(R.id.RadioGroupPerm)

                when (rol) {
                    Rol.ADMIN -> {
                        radioGroup.check(R.id.radio_admin_perm)
                    }
                    Rol.USER -> {
                        radioGroup.check(R.id.radio_guest_perm)
                    }
                    Rol.WATCHER -> {
                        radioGroup.check(R.id.radio_watcher_perm)
                    }
                    Rol.OWNER -> TODO()
                    null -> TODO()
                }

                radioGroup.setOnCheckedChangeListener { group, checkedId ->
                    val roleChecked: RadioButton = group.findViewById(checkedId)
                    presenter.updateRole(
                        roleChecked.text.toString(),
                        user.getId()!!,
                        board.getId()

                    )
                }
                builder.setView(dialogView)
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }
        }



    }

    class ViewHolderUsersPerm(v: View) : RecyclerView.ViewHolder(v) {
        var userName: TextView
        var userImage: ImageView
        var role:TextView
        var view:View=v

        init {
            userName = view.findViewById(R.id.userName)
            userImage = view.findViewById(R.id.userImage)
            role = view.findViewById(R.id.role)

        }
    }

}