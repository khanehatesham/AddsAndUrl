package com.adsandurl.dataModel

import com.google.gson.annotations.SerializedName

class ChildrenObject {
    @SerializedName("name")
    private var name: String? = null
    @SerializedName("title")
    private var title: String? = null
    @SerializedName("thumbnail")
    private var thumbnail: String? = null

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun getThumbnail(): String? {
        return thumbnail
    }

    fun setThumbnail(thumbnail: String) {
        this.thumbnail = thumbnail
    }

}
