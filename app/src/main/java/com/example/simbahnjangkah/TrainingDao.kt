package com.example.simbahnjangkah

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TrainingDao {

    @Upsert
    fun upsertTraining(training: Training)

    @Insert
    suspend fun insertTraining(training: Training)

    @Query("DELETE FROM training")
    suspend fun deleteAllTrainings()


    @Query("SELECT * FROM training")
    fun getAllTrainings(): List<Training>

    //    select by date
    @Query("SELECT * FROM training WHERE dateRecorded = :date")
    fun getTrainingByDate(date: String): Training



}