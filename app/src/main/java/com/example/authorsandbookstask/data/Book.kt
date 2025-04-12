package com.example.authorsandbookstask.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "books_table")
data class Book(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val authorsId: Int,
    val author: String
) : Serializable