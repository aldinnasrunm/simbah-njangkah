package com.example.simbahnjangkah

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Training(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "dateRecorded" ) val dateRecorded: String?,
    @ColumnInfo(name = "totalStep" ) val totalSteps: Int = 0,
    @ColumnInfo(name = "status" ) val status: Boolean = false
)
