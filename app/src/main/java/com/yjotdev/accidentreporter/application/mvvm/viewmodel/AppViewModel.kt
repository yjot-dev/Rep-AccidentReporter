package com.yjotdev.accidentreporter.application.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.yjotdev.accidentreporter.application.mvvm.model.AppModel
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.usecase.DeleteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.InsertReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.SelectReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.CreateTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.GetTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.UpdateReportUseCase
import com.yjotdev.accidentreporter.domain.utils.Validation

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

    /** Carga el token y los reportes en la IU **/
    fun loadData() {
        createTokenUseCase()
        setToken(getTokenUseCase())
        getReports()
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

    /** Actualiza la lista de marcadores **/
    private fun setItemsMarker(list: List<ReportEntity>){
        _uiState.update { state ->
            state.copy(itemsMarker = list)
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

    /** Obtiene los reportes (marcadores) de la BD **/
    private fun getReports(){
        viewModelScope.launch {
            val reports = try{
                withContext(Dispatchers.IO) { selectReportUseCase() }
            }catch (e: Exception){ emptyList() }
            setItemsMarker(reports)
        }
    }

    /** Inserta un reporte a la BD **/
    fun insertReport(result: (Boolean) -> Unit) {
        viewModelScope.launch {
            val state = uiState.value
            val report = ReportEntity(
                id = 0,
                latitude = state.posMarker.latitude,
                longitude = state.posMarker.longitude,
                date = Validation.getDateToString(),
                type = state.itemsComboBox[state.indexComboBox],
                description = state.textDescription,
                token = state.token
            )
            try {
                withContext(Dispatchers.IO){ insertReportUseCase(report) }
                getReports()
                result(true)
            }catch (e: Exception){
                result(false)
            }
        }
    }

    /** Actualiza un reporte de la BD **/
    fun updateReport(result: (Boolean) -> Unit) {
        viewModelScope.launch {
            val state = uiState.value
            val id = state.itemsMarker[state.indexMarker].id
            val report = ReportEntity(
                id = id,
                latitude = state.posMarker.latitude,
                longitude = state.posMarker.longitude,
                date = Validation.getDateToString(),
                type = state.itemsComboBox[state.indexComboBox],
                description = state.textDescription,
                token = state.token
            )
            try {
                withContext(Dispatchers.IO){ updateReportUseCase(id, report) }
                getReports()
                result(true)
            }catch (e: Exception){
                result(false)
            }
        }
    }

    /** Elimina un reporte de la BD **/
    fun deleteReport(result: (Boolean) -> Unit) {
        viewModelScope.launch {
            val state = uiState.value
            val id = state.itemsMarker[state.indexMarker].id
            try{
                withContext(Dispatchers.IO){ deleteReportUseCase(id) }
                getReports()
                result(true)
            }catch (e: Exception){
                result(false)
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
        return state.token == state.itemsMarker[state.indexMarker].token
    }

    /** Muestra y oculta la informacion del marcador seleccionado **/
    fun showMarker(): Boolean{
        val state = uiState.value
        return if(state.itemsMarker.isNotEmpty()){
            val pos = Validation.convertToPosition(state.itemsMarker[state.indexMarker])
            pos == state.posMarker
        }else{ false }
    }

    /** Resetea el viewmodel **/
    private fun resetViewModel(){
        _uiState.value = AppModel()
    }
}