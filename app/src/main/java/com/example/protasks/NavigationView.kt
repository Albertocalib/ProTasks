package com.example.protasks

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.activities.LoginActivity
import com.example.protasks.presenters.board.BoardPresenter
import com.google.android.material.navigation.NavigationView


class NavigationView(
    navigationView : NavigationView, var recyclerView2: RecyclerView?, var userPhoto: ImageView?, private var context: Context, private var activity: Activity,
    var userCompleteName:TextView?, var userEmail:TextView?, var boardPresenter:BoardPresenter?){

    var logoutButton: ImageButton? = null
    var viewMode: ImageButton? = null
    init {
        recyclerView2 = navigationView.findViewById(R.id.recycler_board_navigation_view)
        setLayoutManager()
        val headerView = navigationView.getHeaderView(0)
        userPhoto = headerView.findViewById(R.id.profilePic)
        userCompleteName = headerView.findViewById(R.id.nameProfile)
        userEmail = headerView.findViewById(R.id.userEmailProfile)
        userPhoto!!.setOnClickListener {
            val nagDialog = Dialog(context, android.R.style.ThemeOverlay_Material_Dark)
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            nagDialog.setCancelable(false)
            nagDialog.setContentView(R.layout.image_dialog)
            val btnClose = nagDialog.findViewById(R.id.btnIvClose) as Button
            val btnDownload = nagDialog.findViewById(R.id.btnDownload) as Button
            val btnChangePhoto = nagDialog.findViewById(R.id.btnChangePhoto) as Button
            val ivPreview = nagDialog.findViewById(R.id.iv_preview_image) as ImageView
            ivPreview.setImageDrawable(userPhoto!!.drawable)
            btnClose.setOnClickListener {
                nagDialog.dismiss()
            }
            btnDownload.setOnClickListener {
                boardPresenter!!.downloadImage(ivPreview, context)
                Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show()
            }
            btnChangePhoto.setOnClickListener {
                nagDialog.dismiss()
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                activity.startActivityForResult(intent, 1111)
            }

            nagDialog.show()
        }

        logoutButton = headerView.findViewById(R.id.logOutButton)
        logoutButton!!.setOnClickListener {
            logOut()
        }
        viewMode = headerView.findViewById(R.id.viewModeButton)


    }
    private fun setLayoutManager() {
        recyclerView2!!.layoutManager = GridLayoutManager(context, 1)
    }

    private fun logOut() {
        boardPresenter!!.removePreferences()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
    }

}