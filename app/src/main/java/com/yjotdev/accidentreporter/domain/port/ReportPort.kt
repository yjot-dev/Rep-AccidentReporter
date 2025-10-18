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
    /**
     * Recupera una lista de todos los reportes existentes.
     *
     * Es una operación asíncrona que puede suspenderse.
     * @return [Result<List<ReportEntity>>] Un objeto [Result] que encapsula la lista de [ReportEntity] en caso de éxito,
     * o un error en caso de fallo.
     */
    suspend fun selectReports(): Result<List<ReportEntity>>

    /**
     * Inserta un nuevo reporte en la fuente de datos.
     *
     * @param report El objeto [ReportEntity] que contiene la información del nuevo reporte a crear.
     * @return [Result<Unit>] Un objeto [Result] que indica el éxito ([Result.Success]) o el fracaso ([Result.Error]) de la operación.
     */
    suspend fun insertReport(report: ReportEntity): Result<Unit>

    /**
     * Actualiza un reporte existente identificado por su ID.
     *
     * @param id El identificador único del reporte que se desea actualizar.
     * @param report El objeto [ReportEntity] con los nuevos datos para el reporte.
     * @return [Result<Unit>] Un objeto [Result] que indica el éxito ([Result.Success]) o el fracaso ([Result.Error]) de la operación.
     */
    suspend fun updateReport(id: Int, report: ReportEntity): Result<Unit>

    /**
     * Elimina un reporte de la fuente de datos usando su ID.
     *
     * @param id El identificador único del reporte que se va to eliminar.
     * @return [Result<Unit>] Un objeto [Result] que indica el éxito ([Result.Success]) o el fracaso ([Result.Error]) de la operación.
     */
    suspend fun deleteReport(id: Int): Result<Unit>
}