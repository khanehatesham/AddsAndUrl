package com.adsandurl.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adsandurl.dataModel.HotChildren
import com.adsandurl.dataModel.NewChildren
import com.adsandurl.repository.DataRepository

class DataViewModel : ViewModel() {

    private var liveData: LiveData<List<HotChildren>>? = null
    private var liveDataNew: LiveData<List<NewChildren>>? = null
    var isNetworkError: MutableLiveData<Int> = MutableLiveData()
    var isDataEnd: MutableLiveData<Boolean> = MutableLiveData()


    fun getHotLiveDat(): LiveData<List<HotChildren>> {
        if (liveData == null) {
            isDataEnd.postValue(false)
            liveData = DataRepository.getInstance().getHotData(isNetworkError, isDataEnd)
        }
        return liveData as LiveData<List<HotChildren>>
    }

    fun getNextHotData() {
        DataRepository.getInstance().getNextHotData(isNetworkError, isDataEnd)
    }

    fun reloadHotData() {
        DataRepository.getInstance().getHotData(isNetworkError, isDataEnd)
    }


    fun getNewLiveData(): LiveData<List<NewChildren>> {
        if (liveDataNew == null) {
            isDataEnd.postValue(false)
            liveDataNew = DataRepository.getInstance().getNewData(isNetworkError, isDataEnd)
        }
        return liveDataNew as LiveData<List<NewChildren>>
    }

    fun getNextNewData() {
        DataRepository.getInstance().getNextNewData(isNetworkError, isDataEnd)
    }

    fun reloadNewData() {
        DataRepository.getInstance().getNewData(isNetworkError, isDataEnd)
    }

}