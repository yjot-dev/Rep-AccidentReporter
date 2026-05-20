package com.yjotdev.accidentreporter.presentation.mvvm.viewmodel

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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import com.yjotdev.accidentreporter.presentation.mvvm.state.UiState
import com.yjotdev.accidentreporter.presentation.navigation.UiEvent
import com.yjotdev.accidentreporter.presentation.navigation.ViewRoutes
import com.yjotdev.accidentreporter.presentation.utils.Helper
import com.yjotdev.accidentreporter.domain.model.ReportModel
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.usecase.string.StringUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.GetTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.EditTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.CreateTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.SelectReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.DeleteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.InsertReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.UpdateReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.geocoding.SelectGeocodingUseCase
import com.yjotdev.accidentreporter.domain.usecase.geocoding.EditLocationUseCase
import com.yjotdev.accidentreporter.domain.usecase.geocoding.GetLocationUseCase
import com.yjotdev.accidentreporter.R

@HiltViewModel
class UiViewModel @Inject constructor(
    private val getString: StringUseCase,
    private val selectGeocodingUseCase: SelectGeocodingUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val editLocationUseCase: EditLocationUseCase,
    private val selectReportUseCase: SelectReportUseCase,
    private val insertReportUseCase: InsertReportUseCase,
    private val updateReportUseCase: UpdateReportUseCase,
    private val deleteReportUseCase: DeleteReportUseCase,
    private val createTokenUseCase: CreateTokenUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val editTokenUseCase: EditTokenUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    private val _eventChannel = Channel<UiEvent>()
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val eventChannel = _eventChannel.receiveAsFlow()

    override fun onCleared() {
        super.onCleared()
        resetViewModel()
    }

    init {
        getToken()
        getLocation()
    }

    /** Carga el token guardado **/
    fun getToken() {
        if (getTokenUseCase().isEmpty()) {
            _uiState.update { it.copy(isLoading = true) }
            viewModelScope.launch {
                when (val result = createTokenUseCase()) {
                    is Result.Success -> {
                        editTokenUseCase(result.data)
                        _uiState.update { it.copy(
                            isLoading = false,
                            textToken = result.data)
                        }
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _eventChannel.send(UiEvent.ShowToast(
                            getString(R.string.toast_create_token_false))
                        )
                        _eventChannel.send(UiEvent.ShowLog(
                            "getToken: ${result.exception.message!!}")
                        )
                    }
                }
            }
        } else {
            _uiState.update { it.copy(textToken = getTokenUseCase()) }
        }
    }

    /** Carga la ubicacion guardada **/
    fun getLocation() {
        val location = getLocationUseCase()
        if(location.isNotEmpty()) {
            val item = location.split(",")
            _uiState.update { state ->
                state.copy(
                    textCountry = item[0],
                    textProvince = item[1],
                    textCity = item[2]
                )
            }
        }
    }

    /** Edita el token guardado **/
    fun editToken(token: String) {
        editTokenUseCase(token)
        viewModelScope.launch {
            _eventChannel.send(UiEvent.ShowToast(
                getString(R.string.toast_update_token_true))
            )
        }
    }

    /** Edita la ubicacion guardada **/
    fun editLocation(country: String, province: String, city: String) {
        val location = "$country,$province,$city"
        editLocationUseCase(location)
    }

    /** Actualiza la lista de reportes **/
    fun setItemsMarker(list: List<ReportModel>){
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

    /** Actualiza el texto del pais en su configuracion **/
    fun setTextCountry(text: String){
        _uiState.update { state ->
            state.copy(textCountry = text)
        }
    }

    /** Actualiza el texto de la provincia en su configuracion **/
    fun setTextProvince(text: String){
        _uiState.update { state ->
            state.copy(textProvince = text)
        }
    }

    /** Actualiza el texto de la ciudad en su configuracion **/
    fun setTextCity(text: String){
        _uiState.update { state ->
            state.copy(textCity = text)
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

    /** Obtiene la ubicacion del usuario en base a su pais, provincia y ciudad **/
    fun selectGeocoding(){
        val state = _uiState.value
        val country = state.textCountry
        val province = state.textProvince
        val city = state.textCity
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when(val result = selectGeocodingUseCase(country, province, city)) {
                is Result.Success -> {
                    editLocation(country, province, city)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            location = LatLng(
                                result.data.lat,
                                result.data.lng
                            )
                        )
                    }
                    _eventChannel.send(UiEvent.ShowToast(
                        getString(R.string.toast_geocoding_true))
                    )
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(UiEvent.ShowToast(
                        getString(R.string.toast_geocoding_false))
                    )
                    _eventChannel.send(UiEvent.ShowLog(
                        "selectGeocoding: ${result.exception.message!!}")
                    )
                }
            }
        }
    }

    /** Obtiene los reportes (marcadores) de la BD **/
    fun selectReports(){
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
                            itemsMarker = emptyList()
                        )
                    }
                    _eventChannel.send(UiEvent.ShowLog(
                        "selectReports: ${result.exception.message!!}")
                    )
                }
            }
        }
    }

    /** Inserta un reporte a la BD **/
    fun insertReport() {
        val state = _uiState.value
        val report = ReportModel(
            id = 0,
            latitude = state.posMarker.latitude,
            longitude = state.posMarker.longitude,
            date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Helper.getDateToString()
            }else "",
            type = state.itemsComboBox[state.indexComboBox],
            description = state.textDescription,
            token = state.textToken
        )
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = insertReportUseCase(report)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(UiEvent.ShowToast(
                        getString(R.string.toast_insert_true))
                    )
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(UiEvent.ShowToast(
                        getString(R.string.toast_insert_false))
                    )
                    _eventChannel.send(UiEvent.ShowLog(
                        "insertReport: ${result.exception.message!!}")
                    )
                }
            }
        }
    }

    /** Actualiza un reporte de la BD **/
    fun updateReport() {
        val state = _uiState.value
        val id = state.itemsMarker[state.indexMarker].id
        val report = state.itemsMarker[state.indexMarker].copy(
            type = state.itemsComboBox[state.indexComboBox],
            description = state.textDescription,
            token = state.textToken
        )
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = updateReportUseCase(id, report)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(UiEvent.ShowToast(
                        getString(R.string.toast_update_true))
                    )
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(UiEvent.ShowToast(
                        getString(R.string.toast_update_false))
                    )
                    _eventChannel.send(UiEvent.ShowLog(
                        "updateReport: ${result.exception.message!!}")
                    )
                }
            }
        }
    }

    /** Elimina un reporte de la BD **/
    fun deleteReport() {
        val state = _uiState.value
        val id = state.itemsMarker[state.indexMarker].id
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = deleteReportUseCase(id)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(UiEvent.ShowToast(
                        getString(R.string.toast_delete_true))
                    )
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _eventChannel.send(UiEvent.ShowToast(
                        getString(R.string.toast_delete_false))
                    )
                    _eventChannel.send(UiEvent.ShowLog(
                        "deleteReport: ${result.exception.message!!}")
                    )
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
        return if(!state.itemsMarker.isEmpty()){
            state.textToken == state.itemsMarker[state.indexMarker].token }
        else false
    }

    /** Muestra y oculta la informacion del marcador seleccionado **/
    fun showMarker(): Boolean{
        val state = _uiState.value
        return if(!state.itemsMarker.isEmpty()){
            val pos = Helper.convertToPosition(state.itemsMarker[state.indexMarker])
            pos == state.posMarker
        }else{ false }
    }

    /** Resetea el viewmodel **/
    private fun resetViewModel(){
        _uiState.value = UiState()
    }
}