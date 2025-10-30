package com.yjotdev.accidentreporter.application.mvvm.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.yjotdev.accidentreporter.application.mvvm.model.AppModel
import com.yjotdev.accidentreporter.application.utils.Validation
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.usecase.token.CreateTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.GetTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.SelectReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.DeleteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.InsertReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.UpdateReportUseCase

@HiltViewModel
class AppViewModel @Inject constructor(
    private val selectReportUseCase: SelectReportUseCase,
    private val insertReportUseCase: InsertReportUseCase,
    private val updateReportUseCase: UpdateReportUseCase,
    private val deleteReportUseCase: DeleteReportUseCase,
    private val createTokenUseCase: CreateTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(AppModel())
    val uiState: StateFlow<AppModel> = _uiState.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        resetViewModel()
    }

    /** Carga el token **/
    fun loadToken() {
        createTokenUseCase()
        setToken(getTokenUseCase())
    }

    /** Actualiza el texto de la descripcion **/
    fun setTextDescription(text: String){
        _uiState.update { state ->
            state.copy(textDescription = text)
        }
    }

    /** Actualiza el indice de la opcion seleccionada **/
    fun setIndexComboBox(index: Int){
        _uiState.update { state ->
            state.copy(indexComboBox = index)
        }
    }

    /** Actualiza la lista de opciones **/
    fun setItemsComboBox(list: List<String>){
        _uiState.update { state ->
            state.copy(itemsComboBox = list)
        }
    }

    /** Muestra u oculta la informacion del marcador **/
    fun setShowPosition(show: Boolean){
        _uiState.update { state ->
            state.copy(showPosition = show)
        }
    }

    /** Actualiza la posicion del marcador **/
    fun setPosMarker(pos: LatLng){
        _uiState.update { state ->
            state.copy(posMarker = pos)
        }
    }

    /** Actualiza el indice del marcador seleccionado **/
    fun setIndexMarker(index: Int){
        _uiState.update { state ->
            state.copy(indexMarker = index)
        }
    }

    /** Actualiza el token **/
    private fun setToken(token: Int){
        _uiState.update { state ->
            state.copy(token = token)
        }
    }

    /** Habilita o deshabilita la edicion en EditPositionView **/
    fun setEnableUpdate(enable: Boolean){
        _uiState.update { state ->
            state.copy(enableUpdate = enable)
        }
    }

    /** Resetea las banderas **/
    fun clearFlags() {
        _uiState.update { state ->
            state.copy(wasFound = false, wasInserted = false,
                wasUpdated = false, wasDeleted = false)
        }
    }

    /** Aumenta el contador de operaciones **/
    fun setOperationCompletedCount() {
        _uiState.update { state ->
            state.copy(operationCompletedCount =
                state.operationCompletedCount + 1)
        }
    }

    /** Obtiene los reportes (marcadores) de la BD **/
    fun getReports(){
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = selectReportUseCase()
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            itemsMarker = result.data,
                            wasFound = true,
                            operationCompletedCount = it.operationCompletedCount + 1
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            itemsMarker = null,
                            wasFound = false,
                            error = result.exception.message,
                            operationCompletedCount = it.operationCompletedCount + 1
                        )
                    }
                }
            }
        }
    }

    /** Inserta un reporte a la BD **/
    fun insertReport() {
        val state = uiState.value
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val report = ReportEntity(
                id = 0,
                latitude = state.posMarker.latitude,
                longitude = state.posMarker.longitude,
                date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                           Validation.getDateToString()
                       }else "",
                type = state.itemsComboBox[state.indexComboBox],
                description = state.textDescription,
                token = state.token
            )
            val result = insertReportUseCase(report)
            when (result) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            wasInserted = true,
                            operationCompletedCount = it.operationCompletedCount + 1
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            wasInserted = false,
                            error = result.exception.message,
                            operationCompletedCount = it.operationCompletedCount + 1
                        )
                    }
                }
            }
        }
    }

    /** Actualiza un reporte de la BD **/
    fun updateReport() {
        val state = uiState.value
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            state.itemsMarker?.let { itemsMarker ->
                val id = itemsMarker[state.indexMarker].id
                val report = ReportEntity(
                    id = id,
                    latitude = state.posMarker.latitude,
                    longitude = state.posMarker.longitude,
                    date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Validation.getDateToString()
                    }else "",
                    type = state.itemsComboBox[state.indexComboBox],
                    description = state.textDescription,
                    token = state.token
                )
                val result = updateReportUseCase(id, report)
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                wasUpdated = true,
                                operationCompletedCount = it.operationCompletedCount + 1
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                wasUpdated = false,
                                error = result.exception.message,
                                operationCompletedCount = it.operationCompletedCount + 1
                            )
                        }
                    }
                }
            }
        }
    }

    /** Elimina un reporte de la BD **/
    fun deleteReport() {
        val state = uiState.value
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            state.itemsMarker?.let { itemsMarker ->
                val id = itemsMarker[state.indexMarker].id
                val result = deleteReportUseCase(id)
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                wasDeleted = true,
                                operationCompletedCount = it.operationCompletedCount + 1
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                wasDeleted = false,
                                error = result.exception.message,
                                operationCompletedCount = it.operationCompletedCount + 1
                            )
                        }
                    }
                }
            }
        }
    }

    /** Habilita o deshabilita los botones **/
    fun enabledForm(): Boolean{
        val state = uiState.value
        return state.indexComboBox != 0 && state.textDescription.isNotBlank()
    }

    /** Verifica si el token del usuario corresponde al reporte seleccionado **/
    fun verifyUser(): Boolean{
        val state = uiState.value
        return if(!state.itemsMarker.isNullOrEmpty()){
            state.token == state.itemsMarker[state.indexMarker].token }
        else false
    }

    /** Muestra y oculta la informacion del marcador seleccionado **/
    fun showMarker(): Boolean{
        val state = uiState.value
        return if(!state.itemsMarker.isNullOrEmpty()){
            val pos = Validation.convertToPosition(state.itemsMarker[state.indexMarker])
            pos == state.posMarker
        }else{ false }
    }

    /** Resetea el viewmodel **/
    private fun resetViewModel(){
        _uiState.value = AppModel()
    }
}