package com.yjotdev.accidentreporter.domain.repository

import kotlinx.coroutines.flow.Flow
import com.yjotdev.accidentreporter.domain.model.ReportModel

/**
 * Define el contrato para las operaciones de datos relacionadas con los reportes.
 *
 * Esta interfaz pertenece a la capa de Dominio y actúa como un puerto en la Arquitectura Hexagonal.
 * Abstrae por completo la fuente de datos (ya sea una API remota, una base de datos local, etc.),
 * permitiendo que la lógica de negocio (casos de uso) dependa de esta abstracción y no de una
 * implementación concreta.
 */
interface ReportDaoRepository {

    fun getLocalReports(): Flow<List<ReportModel>>

    suspend fun saveAllReports(reports: List<ReportModel>)

    suspend fun insertLocalReport(report: ReportModel)

    suspend fun updateLocalReport(report: ReportModel)

    suspend fun deleteLocalReport(report: ReportModel)
}