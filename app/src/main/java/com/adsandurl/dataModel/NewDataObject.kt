package com.adsandurl.dataModel

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

class NewDataObject {
    @Ignore
    @SerializedName("data")
    private var newData: NewData? = null

    fun getData(): NewData? {
        return newData
    }

    fun setData(newData: NewData) {
        this.newData = newData
    }
}