package com.example.core.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class UserEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "login")
    val login: String? = null,
    @ColumnInfo(name = "avatarUrl")
    val avatarUrl: String? = null,
)