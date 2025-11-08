package com.yjotdev.accidentreporter.domain.port

import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.entity.ReportEntity

/**
 * Define el contrato para las operaciones de datos relacionadas con los reportes.
 *
 * Esta interfaz pertenece a la capa de Dominio y actúa como un puerto en la Arquitectura Hexagonal.
 * Abstrae por completo la fuente de datos (ya sea una API remota, una base de datos local, etc.),
 * permitiendo que la lógica de negocio (casos de uso) dependa de esta abstracción y no de una
 * implementación concreta.
 */
interface ReportPort {

    suspend fun selectReports(): Result<List<ReportEntity>>

    suspend fun insertReport(report: ReportEntity): Result<Unit>

    suspend fun updateReport(id: Int, report: ReportEntity): Result<Unit>

    suspend fun deleteReport(id: Int): Result<Unit>
}