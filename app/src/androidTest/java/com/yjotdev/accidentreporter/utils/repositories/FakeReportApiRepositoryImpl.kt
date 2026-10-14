package com.yjotdev.accidentreporter.utils.repositories

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.model.ReportModel
import com.yjotdev.accidentreporter.domain.repository.ReportApiRepository
import com.yjotdev.accidentreporter.domain.core.Result

@Singleton
class FakeReportApiRepositoryImpl @Inject constructor(): ReportApiRepository {
    private val reportList = mutableListOf(
        ReportModel(
            id = 1,
            latitude = -3.245448,
            longitude = -79.832331,
            date = "15/03/2025",
            type = "Accidentes",
            description = "Hubo un accidente en la calle 12",
            token = "a7cf5ac786824acaccff4d533832f1f5"
        )
    )

    override suspend fun getRemoteReports(): Result<List<ReportModel>> {
        return if (reportList.isNotEmpty()){
            Result.Success(reportList)
        }else {
            Result.Error(Exception("Error al encontrar los reportes"))
        }
    }

    override suspend fun insertRemoteReport(report: ReportModel): Result<Unit> {
        return if (report != ReportModel()){
            Result.Success(Unit)
        }else {
            Result.Error(Exception("Error al insertar el reporte"))
        }
    }

    override suspend fun updateRemoteReport(id: Int, report: ReportModel): Result<Unit> {
        return if (id != 0 && report != ReportModel()){
            Result.Success(Unit)
        }else {
            Result.Error(Exception("Error al actualizar el reporte"))
        }
    }

    override suspend fun deleteRemoteReport(id: Int): Result<Unit> {
        return if (id != 0){
            Result.Success(Unit)
        }else {
            Result.Error(Exception("Error al eliminar el reporte"))
        }
    }

    override suspend fun createToken(): Result<String>{
        return if(reportList.isNotEmpty()) {
            Result.Success(reportList[0].token)
        } else {
            Result.Error(Exception("Error al crear token"))
        }
    }
}