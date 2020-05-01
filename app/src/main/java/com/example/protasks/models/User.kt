package com.example.protasks.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


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
    @SerializedName("Id")
    private val id: Long? = null

    @Expose
    @SerializedName("photo")
    private val photo: String? = null

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


}
