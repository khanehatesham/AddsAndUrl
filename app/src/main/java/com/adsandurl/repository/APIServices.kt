package com.adsandurl.repository

import com.adsandurl.dataModel.HotDataObject
import com.adsandurl.dataModel.NewDataObject
import retrofit2.Call
import retrofit2.http.GET

interface APIServices {

    @GET("/hot.json")
    fun getHotJason(): Call<HotDataObject>

    @GET("/new.json")
    fun getNewJason(): Call<NewDataObject>

}