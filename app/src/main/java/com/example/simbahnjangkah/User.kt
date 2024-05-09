package com.example.simbahnjangkah

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = false) val id : Int = 0,
    @ColumnInfo(name = "userName" ) val userName : String,
    @ColumnInfo(name =  "userAge") val userAge : Int = 0,
    @ColumnInfo(name =  "userGender") val userGender : String,
    @ColumnInfo(name = "uid") val uid : String,
)
