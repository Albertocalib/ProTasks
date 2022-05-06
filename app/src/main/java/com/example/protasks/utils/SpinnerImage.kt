package com.example.protasks.utils

class SpinnerImage(var text: String?, var imageId: Int?, var color: Int?) {

    override fun toString(): String {
        return this.text!!
    }
}