package com.yjotdev.accidentreporter.utils.repositories

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.model.ReportModel
import com.yjotdev.accidentreporter.domain.repository.ReportRepository
import com.yjotdev.accidentreporter.domain.core.Result

@Singleton
class FakeReportRepositoryImpl @Inject constructor(): ReportRepository {
    private val reportList = mutableListOf(
        ReportModel(
            id = 0,
            latitude = -3.245448,
            longitude = -79.832331,
            date = "15/03/2025",
            type = "Accidentes",
            description = "Hubo un accidente en la calle 12",
            token = "a7cf5ac786824acaccff4d533832f1f5"
        )
    )

    override suspend fun selectReports(): Result<List<ReportModel>> {
        return if (reportList.isNotEmpty()){
            Result.Success(reportList)
        }else {
            Result.Error(Exception("Error al encontrar los reportes"))
        }
    }

    override suspend fun insertReport(report: ReportModel): Result<Unit> {
        return if (report != ReportModel()){
            Result.Success(Unit)
        }else {
            Result.Error(Exception("Error al insertar el reporte"))
        }
    }

    override suspend fun updateReport(id: Int, report: ReportModel): Result<Unit> {
        return if (report != ReportModel()){
            Result.Success(Unit)
        }else {
            Result.Error(Exception("Error al actualizar el reporte"))
        }
    }

    override suspend fun deleteReport(id: Int): Result<Unit> {
        return if (id != 0){
            Result.Success(Unit)
        }else {
            Result.Error(Exception("Error al eliminar el reporte"))
        }
    }

    override suspend fun createToken(): Result<String>{
        return Result.Success("a7cf5ac786824acaccff4d533832f1f5")
    }
}