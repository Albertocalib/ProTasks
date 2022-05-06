package com.example.protasks

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.protasks.models.File
import com.example.protasks.models.Task
import com.example.protasks.presenters.TaskPresenter
import com.example.protasks.utils.ImageHandler
import java.util.*
import kotlin.collections.HashSet


class AttachmentsAdapter(
    private val attachments: List<File?>?,
    private val taskPresenter: TaskPresenter?,
    private val task: Task?,
    private val context:Context
) : RecyclerView.Adapter<AttachmentsAdapter.ViewHolderAttachment>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderAttachment {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.attachment, parent, false)
        return ViewHolderAttachment(view)
    }

    override fun getItemCount(): Int {
        return attachments!!.size
    }

    override fun onBindViewHolder(holder: ViewHolderAttachment, position: Int) {
        val attachment = attachments!![position]
        holder.file = attachment
        holder.name.text=attachment!!.getName()
        var type = ""
        if (attachment.getType()!=null) {
            type = attachment.getType()!!.lowercase(Locale.ROOT)
        }
        if (type=="pdf"){
            holder.image.setImageResource(R.drawable.pdf_logo)
        }else if (type=="zip" || type=="rar") {
            holder.image.setImageResource(R.drawable.zip_logo)
        }else if (EXTENSIONS_WORD.contains(type)){
            holder.image.setImageResource(R.drawable.word_logo)
        }else if (EXTENSIONS_EXCEL.contains(type)){
            holder.image.setImageResource(R.drawable.excel_logo)
        }else if (EXTENSIONS_IMAGES.contains(type)){
            val imageBytes = Base64.decode(attachment.getContent(), Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.image.setImageBitmap(decodedImage)
        }
        holder.deleteIcon.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("¿Estás seguro que desea eliminarlo?")
                .setCancelable(false)
                .setPositiveButton("Aceptar") { dialog, _ ->
                    dialog.dismiss()
                    taskPresenter!!.removeAttachedFile(attachment,task!!)
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
        holder.image.setOnClickListener {
            val imageHandler = ImageHandler()
            imageHandler.downloadFile(attachment,context)
        }


    }

    class ViewHolderAttachment(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.attachmentName)
        var image: ImageView = v.findViewById(R.id.attachmentImage)
        val deleteIcon: ImageButton = v.findViewById(R.id.remove_attachment)
        var file: File? =null

    }

    companion object {
        val EXTENSIONS_WORD:HashSet<String> = HashSet(listOf("docx","docm","dotx","dotm","odt","ott","txt"))
        val EXTENSIONS_EXCEL:HashSet<String> = HashSet(listOf("xlsx","xls","xlsm","xlsb",".xltx","xltm","xlt","csv"))
        val EXTENSIONS_IMAGES:HashSet<String> = HashSet(listOf("jpg","jpeg","png","svg","img","heic"))

    }
}