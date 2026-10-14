package com.yjotdev.accidentreporter.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yjotdev.accidentreporter.data.local.dao.ReportDao
import com.yjotdev.accidentreporter.data.local.entity.ReportEntity

@Database(
    entities = [ReportEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AriDatabase: RoomDatabase() {
    companion object {
        const val NAME = "bd_ari"
    }

    abstract fun reportDao(): ReportDao
}