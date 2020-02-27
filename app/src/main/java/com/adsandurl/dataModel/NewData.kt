package com.adsandurl.dataModel

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

class NewData {

    @Ignore
    @SerializedName("children")
    private var listOfChildrenData: List<NewChildren>? = null

    fun getListOfChildrenData(): List<NewChildren>? {
        return listOfChildrenData
    }

    fun setListOfChildrenData(listOfChildrenData: List<NewChildren>) {
        this.listOfChildrenData = listOfChildrenData
    }
}