package com.yjotdev.accidentreporter.presentation.navigation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import com.yjotdev.accidentreporter.presentation.components.LoadingScreen
import com.yjotdev.accidentreporter.presentation.components.TitleBar
import com.yjotdev.accidentreporter.presentation.mvvm.ui.AddPositionView
import com.yjotdev.accidentreporter.presentation.mvvm.ui.LocationConfigView
import com.yjotdev.accidentreporter.presentation.mvvm.ui.EditPositionView
import com.yjotdev.accidentreporter.presentation.mvvm.ui.MapView
import com.yjotdev.accidentreporter.presentation.mvvm.ui.StartView
import com.yjotdev.accidentreporter.presentation.mvvm.ui.TokenConfigView
import com.yjotdev.accidentreporter.presentation.mvvm.viewmodel.UiViewModel
import com.yjotdev.accidentreporter.presentation.utils.Helper
import com.yjotdev.accidentreporter.R

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    viewModel: UiViewModel,
    isTestMode: Boolean,
){
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    //Actualiza el titulo de la barra de navegacion segun la ruta
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ViewRoutes.valueOf(
        backStackEntry?.destination?.route ?: ViewRoutes.Start.name
    )
    //Items del combobox
    val optionList = listOf(
        stringResource(R.string.combobox_title),
        stringResource(R.string.combobox_option1),
        stringResource(R.string.combobox_option2),
        stringResource(R.string.combobox_option3)
    )
    viewModel.setItemsComboBox(optionList)
    //Observa clicks en el mapa
    ObserveClickOnMap(viewModel = viewModel)
    //Observa estados asincrónicos
    ObserveViewModelState(
        viewModel = viewModel,
        navController = navController,
        context = context
    )
    //UI
    Scaffold(
        topBar = {
            TitleBar(
                viewRoutes = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ViewRoutes.Start.name,
            enterTransition = { slideInHorizontally(animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )){ -it } },
            exitTransition = { slideOutHorizontally(animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )){ -it } },
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = ViewRoutes.Start.name) {
                StartView(
                    modifier = Modifier.fillMaxSize(),
                    onTokenConfig = {
                        viewModel.getToken()
                        navController.navigate(ViewRoutes.TokenConfig.name)
                    },
                    onLocationConfig = {
                        viewModel.getLocation()
                        navController.navigate(ViewRoutes.LocationConfig.name)
                    }
                )
            }
            composable(route = ViewRoutes.TokenConfig.name) {
                TokenConfigView(
                    modifier = Modifier.fillMaxSize(),
                    tokenText = state.textToken,
                    onTokenText = { viewModel.setTextToken(it) },
                    enableControls = state.enableUpdate,
                    onEnableControls = { viewModel.setEnableUpdate(!it) },
                    onUpdate = {
                        if (state.textToken.isNotEmpty()) {
                            if (Helper.isValidToken(state.textToken)) {
                                viewModel.editToken(state.textToken)
                            } else {
                                Toast.makeText(
                                    context, context.getString(R.string.toast_invalid_data), Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context, context.getString(R.string.toast_empty_field), Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
            composable(route = ViewRoutes.LocationConfig.name) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LocationConfigView(
                        modifier = Modifier.fillMaxSize(),
                        country = state.textCountry,
                        province = state.textProvince,
                        city = state.textCity,
                        enableOnMap = state.location != LatLng(0.0, 0.0),
                        onCountry = { viewModel.setTextCountry(it) },
                        onProvince = { viewModel.setTextProvince(it) },
                        onCity = { viewModel.setTextCity(it) },
                        onSearchLocation = {
                            if (state.textCountry.isNotEmpty()
                                && state.textProvince.isNotEmpty()
                                && state.textCity.isNotEmpty()) {
                                if (Helper.isValidMessage(state.textCountry)
                                    && Helper.isValidMessage(state.textProvince)
                                    && Helper.isValidMessage(state.textCity)) {
                                    viewModel.selectGeocoding()
                                } else {
                                    Toast.makeText(
                                        context, context.getString(R.string.toast_invalid_data), Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context, context.getString(R.string.toast_empty_field), Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        onMap = { viewModel.selectReports() }
                    )
                    if(state.isLoading) LoadingScreen()
                }
            }
            composable(route = ViewRoutes.Map.name) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    MapView(
                        modifier = Modifier.fillMaxSize(),
                        isTestMode = isTestMode,
                        location = state.location,
                        itemsMarker = state.itemsMarker,
                        indexMarker = state.indexMarker,
                        showPosition = state.showPosition,
                        enableBtnDelete = viewModel.verifyUser(),
                        onShowPosition = { viewModel.setShowPosition(it) },
                        onToLook = {
                            viewModel.setShowPosition(false)
                            navController.navigate(ViewRoutes.EditPosition.name)
                        },
                        onDelete = {
                            viewModel.setShowPosition(false)
                            viewModel.deleteReport()
                        },
                        onMapClick = {
                            viewModel.setPosMarker(it)
                            navController.navigate(ViewRoutes.AddPosition.name)
                        },
                        onInfoWindowClick = { position, index ->
                            viewModel.setPosMarker(position)
                            viewModel.setIndexMarker(index)
                            viewModel.setShowPosition(viewModel.showMarker())
                            viewModel.setEnableUpdate(false)
                        }
                    )
                    if(state.isLoading) LoadingScreen()
                }
            }
            composable(route = ViewRoutes.AddPosition.name) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    AddPositionView(
                        modifier = Modifier.fillMaxSize(),
                        itemsComboBox = state.itemsComboBox,
                        enableBtnReport = viewModel.enabledForm(),
                        textDescription = state.textDescription,
                        onTextDescription = { viewModel.setTextDescription(it) },
                        indexSelected = state.indexComboBox,
                        onIndexSelected = { viewModel.setIndexComboBox(it) },
                        onAdd = {
                            if (state.textDescription.isNotEmpty()) {
                                if (Helper.isValidMessage(state.textDescription)) {
                                    viewModel.insertReport()
                                } else {
                                    Toast.makeText(
                                        context, context.getString(R.string.toast_invalid_data), Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context, context.getString(R.string.toast_empty_field), Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                    if(state.isLoading) LoadingScreen()
                }
            }
            composable(route = ViewRoutes.EditPosition.name) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    EditPositionView(
                        modifier = Modifier.fillMaxSize(),
                        itemsComboBox = state.itemsComboBox,
                        enableComboBox = state.enableUpdate && viewModel.verifyUser(),
                        enableTextDescription = state.enableUpdate && viewModel.verifyUser(),
                        enableBtnEdit = viewModel.verifyUser(),
                        enableBtnUpdate = viewModel.enabledForm()
                                && state.enableUpdate && viewModel.verifyUser(),
                        onEnableBtnUpdate = { viewModel.setEnableUpdate(!it) },
                        textDescription = state.textDescription,
                        onTextDescription = { viewModel.setTextDescription(it) },
                        indexSelected = state.indexComboBox,
                        onIndexSelected = { viewModel.setIndexComboBox(it) },
                        onUpdate = {
                            if (state.textDescription.isNotEmpty()) {
                                if (Helper.isValidMessage(state.textDescription)) {
                                    viewModel.updateReport()
                                } else {
                                    Toast.makeText(
                                        context, context.getString(R.string.toast_invalid_data), Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context, context.getString(R.string.toast_empty_field), Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                    if(state.isLoading) LoadingScreen()
                }
            }
        }
    }
}

@Composable
private fun ObserveClickOnMap(
    viewModel: UiViewModel
){
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = state.posMarker) {
        //Obtiene información del reporte seleccionado
        if(viewModel.showMarker()){
            val type = state.itemsMarker[state.indexMarker].type
            viewModel.setIndexComboBox(state.itemsComboBox.indexOf(type))
            val description = state.itemsMarker[state.indexMarker].description
            viewModel.setTextDescription(description)
        }else{
            viewModel.setIndexComboBox(0)
            viewModel.setTextDescription("")
            viewModel.setShowPosition(false)
        }
    }
}

@Composable
private fun ObserveViewModelState(
    viewModel: UiViewModel,
    navController: NavHostController,
    context: Context
){
    LaunchedEffect(key1 = true) {
        viewModel.eventChannel.collect { event ->
            when (event) {
                // Country -> Map (Revisar AppViewModel.kt lineas 208 - 210)
                is UiEvent.Navigate -> navController.navigate(event.route)
                // Muestra un mensaje de exito o error en el Toast
                is UiEvent.ShowToast -> Toast.makeText(
                        context, event.message, Toast.LENGTH_SHORT
                    ).show()
                // Muestra el error en el Log
                is UiEvent.ShowLog -> Log.d("Https",event.message)
            }
        }
    }
}