package com.example.protasks.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


class User(@Expose
    @SerializedName("name")
    private val name: String?, @Expose
    @SerializedName("surname")
    private val surname: String?,
    @Expose
    @SerializedName("username")
    private val username: String?,
    @Expose
    @SerializedName("password")
    private val password: String?,
    @Expose
    @SerializedName("email")
    private val email: String?
) : IUser {

    @Expose
    @SerializedName("id")
    private var id: Long? = null

    @Expose
    @SerializedName("photo")
    private var photo: String? = null
    @Expose
    @SerializedName("create_date")
    private val create_date: Date? = null

    @Expose
    @SerializedName("write_date")
    private val write_date: Date? = null

    override fun getPhoto(): String? {
        return photo
    }
    override fun getName(): String? {
        return name
    }

    override fun getPasswd(): String? {
        return password
    }

    override fun getEmail(): String? {
        return email
    }

    override fun getUsername(): String? {
        return username
    }

    override fun getSurname(): String? {
        return surname
    }

    override fun getId(): Long? {
        return id
    }
    fun setPhoto(img:String){
        photo=img
    }
    fun setId(id:Long){
        this.id=id
    }
    fun getCompleteName(): String{
        return this.getName()!! + " " + this.getSurname()!!
    }


}
