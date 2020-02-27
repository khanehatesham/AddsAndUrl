package com.adsandurl.dataModel

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

class HotDataObject {
    @Ignore
    @SerializedName("data")
    private var hotData: HotData? = null

    fun getData(): HotData? {
        return hotData
    }

    fun setData(hotData: HotData) {
        this.hotData = hotData
    }
}