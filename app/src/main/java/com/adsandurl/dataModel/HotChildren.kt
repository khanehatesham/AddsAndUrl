package com.adsandurl.dataModel

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "HotJson")
class HotChildren {
    @PrimaryKey(autoGenerate = true)
    private var id: Long = 0

    @Embedded
    @SerializedName("data")
    private var childrenObject: ChildrenObject? = null

    fun getChildrenObject(): ChildrenObject? {
        return childrenObject
    }

    fun setChildrenObject(childrenObject: ChildrenObject) {
        this.childrenObject = childrenObject
    }

    fun getId(): Long {
        return id
    }

    fun setId(id: Long) {
        this.id = id
    }
}