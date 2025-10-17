package com.yjotdev.accidentreporter.domain.port

import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.entity.ReportEntity

/**
 * Define el contrato para las operaciones del repositorio de reportes.
 * Esta interfaz pertenece a la capa de Dominio. No conoce Retrofit ni detalles de la API.
 * Devuelve tipos de datos del Dominio (ReportEntity) o resultados encapsulados.
 */
interface ReportPort {
    /**
     * Seleccionar reporte
     */
    suspend fun selectReports(): Result<List<ReportEntity>>

    /**
     * Insertar reporte
     */
    suspend fun insertReport(report:ReportEntity): Result<Unit>

    /**
     * Actualizar reporte
     */
    suspend fun updateReport(id:Int, report:ReportEntity): Result<Unit>

    /**
     * Eliminar reporte
     */
    suspend fun deleteReport(id:Int): Result<Unit>
}