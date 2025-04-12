package com.example.authorsandbookstask.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "authors_table")
data class Author(
    var name:String,
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0
)
