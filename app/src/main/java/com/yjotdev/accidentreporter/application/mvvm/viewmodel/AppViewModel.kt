package com.yjotdev.accidentreporter.application.mvvm.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.yjotdev.accidentreporter.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import com.yjotdev.accidentreporter.application.mvvm.model.AppModel
import com.yjotdev.accidentreporter.application.navigation.UiEvent
import com.yjotdev.accidentreporter.application.navigation.ViewRoutes
import com.yjotdev.accidentreporter.application.utils.Validation
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.usecase.string.StringUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.CreateTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.GetTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.EditTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.SelectReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.DeleteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.InsertReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.UpdateReportUseCase

@HiltViewModel
class AppViewModel @Inject constructor(
    private val getString: StringUseCase,
    private val selectReportUseCase: SelectReportUseCase,
    private val insertReportUseCase: InsertReportUseCase,
    private val updateReportUseCase: UpdateReportUseCase,
    private val deleteReportUseCase: DeleteReportUseCase,
    private val createTokenUseCase: CreateTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val editTokenUseCase: EditTokenUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(AppModel())
    private val _eventChannel = Channel<UiEvent>()
    val uiState: StateFlow<AppModel> = _uiState.asStateFlow()
    val eventChannel = _eventChannel.receiveAsFlow()

    override fun onCleared() {
        super.onCleared()
        resetViewModel()
    }

    init {
        loadToken()
    }

    /** Carga el token guardado **/
    private fun loadToken() {
        createTokenUseCase()
        setTextToken(getTokenUseCase().toString())
    }

    /** Edita el token guardado **/
    fun editToken(token: String) {
        editTokenUseCase(token.toInt())
        viewModelScope.launch {
            _eventChannel.send(UiEvent.ShowToast(
                getString(R.string.toast_update_token))
            )
        }
    }

    /** Actualiza la lista de reportes **/
    fun setItemsMarker(list: List<ReportEntity>){
        _uiState.update { state ->
            state.copy(itemsMarker = list)
        }
    }

    /** Actualiza el texto de la descripcion del reporte **/
    fun setTextDescription(text: String){
        _uiState.update { state ->
            state.copy(textDescription = text)
        }
    }

    /** Actualiza el texto del token en su configuracion **/
    fun setTextToken(text: String){
        _uiState.update { state ->
            state.copy(textToken = text)
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

    /** Habilita o deshabilita la edicion en EditPositionView o en TokenConfigView **/
    fun setEnableUpdate(enable: Boolean){
        _uiState.update { state ->
            state.copy(enableUpdate = enable)
        }
    }

    /** Obtiene los reportes (marcadores) de la BD **/
    fun getReports(){
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = selectReportUseCase()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            itemsMarker = result.data
                        )
                    }
                    _eventChannel.send(UiEvent.Navigate(
                        ViewRoutes.Map.name)
                    )
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            itemsMarker = null
                        )
                    }
                    _eventChannel.send(UiEvent.ShowLog(
                        result.exception.message!!)
                    )
                }
            }
        }
    }

    /** Inserta un reporte a la BD **/
    fun insertReport() {
        val state = _uiState.value
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
                token = state.textToken.toInt()
            )
            when (val result = insertReportUseCase(report)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                    _eventChannel.send(UiEvent.ShowToast(
                        getString(R.string.toast_insert_true))
                    )
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                    _eventChannel.send(UiEvent.ShowToast(
                        getString(R.string.toast_insert_false))
                    )
                    _eventChannel.send(UiEvent.ShowLog(
                        result.exception.message!!)
                    )
                }
            }
        }
    }

    /** Actualiza un reporte de la BD **/
    fun updateReport() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            state.itemsMarker?.let { itemsMarker ->
                val id = itemsMarker[state.indexMarker].id
                val report = itemsMarker[state.indexMarker].copy(
                    type = state.itemsComboBox[state.indexComboBox],
                    description = state.textDescription,
                    token = state.textToken.toInt()
                )
                when (val result = updateReportUseCase(id, report)) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _eventChannel.send(UiEvent.ShowToast(
                            getString(R.string.toast_update_true))
                        )
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _eventChannel.send(UiEvent.ShowToast(
                            getString(R.string.toast_update_false))
                        )
                        _eventChannel.send(UiEvent.ShowLog(
                            result.exception.message!!)
                        )
                    }
                }
            }
        }
    }

    /** Elimina un reporte de la BD **/
    fun deleteReport() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            state.itemsMarker?.let { itemsMarker ->
                val id = itemsMarker[state.indexMarker].id
                when (val result = deleteReportUseCase(id)) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _eventChannel.send(UiEvent.ShowToast(
                            getString(R.string.toast_delete_true))
                        )
                    }
                    is Result.Error -> {
                        _eventChannel.send(UiEvent.ShowToast(
                            getString(R.string.toast_delete_false))
                        )
                        _eventChannel.send(UiEvent.ShowLog(
                            result.exception.message!!)
                        )
                    }
                }
            }
        }
    }

    /** Habilita o deshabilita los botones **/
    fun enabledForm(): Boolean{
        val state = _uiState.value
        return state.indexComboBox != 0 && state.textDescription.isNotBlank()
    }

    /** Verifica si el token del usuario corresponde al reporte seleccionado **/
    fun verifyUser(): Boolean{
        val state = _uiState.value
        return if(!state.itemsMarker.isNullOrEmpty()){
            state.textToken.toInt() == state.itemsMarker[state.indexMarker].token }
        else false
    }

    /** Muestra y oculta la informacion del marcador seleccionado **/
    fun showMarker(): Boolean{
        val state = _uiState.value
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