package com.adsandurl.repository

import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adsandurl.dataModel.HotChildren
import com.adsandurl.dataModel.HotDataObject
import com.adsandurl.dataModel.NewChildren
import com.adsandurl.dataModel.NewDataObject
import com.adsandurl.database.AppDatabase
import com.adsandurl.util.AppClass
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class DataRepository {
    private var retrofit: Retrofit? = null
    private val db = AppDatabase.getDatabase(AppClass.getContext())

    companion object {
        fun getInstance() = DataRepository()
    }

    fun getAPIService(): APIServices {
        return getClient("https://www.reddit.com").create(APIServices::class.java)
    }

    fun getClient(baseUrl: String): Retrofit {


        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()


        retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

        return retrofit!!

    }

    fun getHotData(isNetworkError: MutableLiveData<Int>, isDataEnd: MutableLiveData<Boolean>):
            LiveData<List<HotChildren>> {
        isDataEnd.value = false
        getInstance().getAPIService().getHotJason().enqueue(object :
            Callback<HotDataObject> {
            override fun onResponse(@NonNull call: Call<HotDataObject>, @NonNull response: Response<HotDataObject>) {
                if (response.isSuccessful) {
                    db.dataDao().insertList(response.body()!!.getData()!!.getListOfChildrenData()!!)
                    isNetworkError.postValue(0)
                    if (response.body()!!.getData()!!.getListOfChildrenData()!!.size == 0) {
                        isDataEnd.value = true
                    }

                } else {
                    isNetworkError.postValue(1)
                }

            }

            override fun onFailure(@NonNull call: Call<HotDataObject>, @NonNull t: Throwable) {
                isNetworkError.postValue(1)

            }
        })
        return db.dataDao().getData()
    }


    /*Get Next Notification*/
    fun getNextHotData(isNetWorkError: MutableLiveData<Int>, isDataEnd: MutableLiveData<Boolean>) {
        getInstance().getAPIService().getHotJason()
            .enqueue(object : Callback<HotDataObject> {
                override fun onResponse(@NonNull call: Call<HotDataObject>, @NonNull response: Response<HotDataObject>) {
                    if (response.isSuccessful) {
                        isNetWorkError.setValue(0)
                        db.dataDao()
                            .insertList(response.body()!!.getData()!!.getListOfChildrenData()!!)
                        if (response.body()!!.getData()!!.getListOfChildrenData()!!.size == 0) {
                            isDataEnd.value = true
                        }

                    } else {
                        isNetWorkError.setValue(2)
                    }
                }

                override fun onFailure(@NonNull call: Call<HotDataObject>, @NonNull t: Throwable) {
                    isNetWorkError.value = 2

                }
            })
    }

    fun getNewData(isNetworkError: MutableLiveData<Int>, isDataEnd: MutableLiveData<Boolean>):
            LiveData<List<NewChildren>> {
        isDataEnd.value = false
        getInstance().getAPIService().getNewJason().enqueue(object :
            Callback<NewDataObject> {
            override fun onResponse(@NonNull call: Call<NewDataObject>, @NonNull response: Response<NewDataObject>) {
                if (response.isSuccessful) {
                    db.dataDao()
                        .newInsertList(response.body()!!.getData()!!.getListOfChildrenData()!!)
                    isNetworkError.postValue(0)
                    if (response.body()!!.getData()!!.getListOfChildrenData()!!.size == 0) {
                        isDataEnd.value = true
                    }

                } else {
                    isNetworkError.postValue(1)
                }

            }

            override fun onFailure(@NonNull call: Call<NewDataObject>, @NonNull t: Throwable) {
                isNetworkError.postValue(1)
            }
        })
        return db.dataDao().getNewData()
    }


    /*Get Next NewData*/
    fun getNextNewData(isNetWorkError: MutableLiveData<Int>, isDataEnd: MutableLiveData<Boolean>) {
        getInstance().getAPIService().getNewJason()
            .enqueue(object : Callback<NewDataObject> {
                override fun onResponse(@NonNull call: Call<NewDataObject>, @NonNull response: Response<NewDataObject>) {
                    if (response.isSuccessful) {
                        isNetWorkError.setValue(0)
                        db.dataDao()
                            .newInsertList(response.body()!!.getData()!!.getListOfChildrenData()!!)
                        if (response.body()!!.getData()!!.getListOfChildrenData()!!.size == 0) {
                            isDataEnd.value = true
                        }

                    } else {
                        isNetWorkError.setValue(2)
                    }
                }

                override fun onFailure(@NonNull call: Call<NewDataObject>, @NonNull t: Throwable) {
                    isNetWorkError.value = 2

                }
            })
    }
}


