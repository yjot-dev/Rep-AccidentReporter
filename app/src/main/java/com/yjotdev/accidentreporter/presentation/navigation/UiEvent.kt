package com.yjotdev.accidentreporter.presentation.navigation

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
    data class ShowLog(val message: String) : UiEvent()
}