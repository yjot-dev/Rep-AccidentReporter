package com.yjotdev.accidentreporter.data.local.dao

import androidx.room.Dao
import androidx.room.Update
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.yjotdev.accidentreporter.data.local.entity.ReportEntity

/**
 * Interfaz DAO (Data Access Object) para Room.
 * Esta es la implementación concreta del puerto del dominio usando la tecnología Room.
 * Pertenece a la capa de Infraestructura.
 */
@Dao
interface ReportDao {
    @Query("SELECT * FROM reports")
    fun getLocalReports(): Flow<List<ReportEntity>>
    @Insert
    suspend fun saveAllReports(reports: List<ReportEntity>)
    @Insert
    suspend fun insertLocalReport(report: ReportEntity)
    @Update
    suspend fun updateLocalReport(report: ReportEntity)
    @Delete
    suspend fun deleteLocalReport(report: ReportEntity)
}