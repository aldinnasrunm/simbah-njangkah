package com.example.simbahnjangkah

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class TestData(
    @PrimaryKey(autoGenerate = false) val id : Int = 0,
    @ColumnInfo(name = "dateRecorded" ) val dateRecorded : Date,
    @ColumnInfo(name = "totalStep" ) val totalSteps : Int = 0,
    @ColumnInfo(name = "distance" ) val distance : Double = 0.0
)