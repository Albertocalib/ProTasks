package com.example.protasks.utils

class SpinnerImage {
    var text: String? = null
    var imageId: Int? = null
    var color:Int?=null

    constructor() {}
    constructor(text: String?, imageId: Int?,color:Int?) {
        this.text = text
        this.imageId = imageId
        this.color = color
    }

    override fun toString(): String {
        return this.text!!
    }
}