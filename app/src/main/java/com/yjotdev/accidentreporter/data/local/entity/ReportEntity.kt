package com.yjotdev.accidentreporter.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val date: String = "",
    val type: String = "",
    val description: String = "",
    val token: String = ""
)