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
    private val id: Int? = null

    override fun getName(): String? {
        return username
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
        return username
    }

    override fun getId(): Int? {
        return id
    }


}
