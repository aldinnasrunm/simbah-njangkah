package com.example.simbahnjangkah

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TrainingDao {

    @Upsert
    fun upsertTraining(training: Training)

//    update data by id
    @Query("UPDATE training SET dateRecorded = :value1, totalStep = :value2 WHERE id = :id")
    fun updateTrainingFields(id: Int, value1: String, value2: Int)



    @Insert
    suspend fun insertTraining(training: Training)

    @Query("DELETE FROM training")
    suspend fun deleteAllTrainings()


    @Query("SELECT * FROM training")
    fun getAllTrainings(): List<Training>

    //    select by date
    @Query("SELECT * FROM training WHERE dateRecorded = :date")
    fun getTrainingByDate(date: String): Training

    @Query("SELECT * FROM Training ORDER BY id DESC LIMIT 1")
    suspend fun getLastTraining(): Training


}