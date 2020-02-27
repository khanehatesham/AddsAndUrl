package com.adsandurl.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adsandurl.dataModel.HotChildren
import com.adsandurl.dataModel.NewChildren

@Dao
interface DataDao {

    @Query("SELECT * FROM hotJson")
    fun getData(): LiveData<List<HotChildren>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: List<HotChildren>)

    @Query("SELECT COUNT(*) FROM hotJson")
    fun totalData(): Int

    @Query("SELECT * FROM NewJson")
    fun getNewData(): LiveData<List<NewChildren>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun newInsertList(list: List<NewChildren>)

    @Query("SELECT COUNT(*) FROM NewJson")
    fun totalNewData(): Int


}