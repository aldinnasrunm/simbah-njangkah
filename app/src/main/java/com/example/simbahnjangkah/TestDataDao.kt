package com.example.simbahnjangkah

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TestDataDao {

    @Upsert
    fun upsertTestData(testData: TestData)

    @Insert
    suspend fun insertTestData(testData: TestData)

    @Query("SELECT * FROM testdata")
    suspend fun getAllTestData() : List<TestData>

    @Query("DELETE FROM testdata")
    suspend fun deleteAllTestDatas()
}