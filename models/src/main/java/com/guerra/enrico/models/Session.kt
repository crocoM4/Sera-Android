package com.guerra.enrico.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Session")
data class Session constructor(
  @PrimaryKey @ColumnInfo(name = "id") val id: String = "",
  @ColumnInfo(name = "userId") val userId: String,
  @ColumnInfo(name = "accessToken") val accessToken: String,
  @ColumnInfo(name = "createdAt") val createdAt: Date = Date()
)