package com.adsandurl.dataModel

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

class HotData {

    @Ignore
    @SerializedName("children")
    private var listOfChildrenData: List<HotChildren>? = null

    fun getListOfChildrenData(): List<HotChildren>? {
        return listOfChildrenData
    }

    fun setListOfChildrenData(listOfChildrenData: List<HotChildren>) {
        this.listOfChildrenData = listOfChildrenData
    }
}