package com.yjotdev.accidentreporter.application.navigation

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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.application.components.LoadingScreen
import com.yjotdev.accidentreporter.application.components.TitleBar
import com.yjotdev.accidentreporter.application.mvvm.view.AddPositionView
import com.yjotdev.accidentreporter.application.mvvm.view.EditPositionView
import com.yjotdev.accidentreporter.application.mvvm.view.MapView
import com.yjotdev.accidentreporter.application.mvvm.view.StartView
import com.yjotdev.accidentreporter.application.mvvm.view.TokenConfigView
import com.yjotdev.accidentreporter.application.mvvm.viewmodel.AppViewModel

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel,
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
    //Observa estado de la camara del mapa
    val elGuabo = LatLng(-3.245274, -79.832028)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(elGuabo, 18f)
    }
    ObserveMapCameraState(
        viewModel = viewModel,
        cameraPositionState = cameraPositionState,
        startPosition = elGuabo,
        isTestMode = isTestMode
    )
    //Observa estados asincronicos
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    StartView(
                        modifier = Modifier.fillMaxSize(),
                        onTokenConfig = {
                            navController.navigate(ViewRoutes.TokenConfig.name)
                        },
                        onNext = { viewModel.getReports() }
                    )
                    if(state.isLoading) LoadingScreen()
                }
            }
            composable(route = ViewRoutes.TokenConfig.name) {
                TokenConfigView(
                    modifier = Modifier.fillMaxSize(),
                    tokenText = state.textToken,
                    onTokenText = { viewModel.setTextToken(it) },
                    enableControls = state.enableUpdate,
                    onEnableControls = { viewModel.setEnableUpdate(!it) },
                    onUpdate = { viewModel.editToken(state.textToken) }
                )
            }
            composable(route = ViewRoutes.Map.name) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    MapView(
                        modifier = Modifier.fillMaxSize(),
                        isTestMode = isTestMode,
                        cameraPositionState = cameraPositionState,
                        itemsMarker = state.itemsMarker!!,
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
                        onAdd = { viewModel.insertReport() }
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
                        onUpdate = { viewModel.updateReport() }
                    )
                    if(state.isLoading) LoadingScreen()
                }
            }
        }
    }
}

@Composable
private fun ObserveMapCameraState(
    viewModel: AppViewModel,
    cameraPositionState: CameraPositionState,
    startPosition: LatLng,
    isTestMode: Boolean
){
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(
        key1 = cameraPositionState,
        key2 = state.posMarker
    ) {
        state.itemsMarker?.let { itemsMarker ->
            //Obtiene informacion del reporte seleccionado
            if(viewModel.showMarker()){
                val type = itemsMarker[state.indexMarker].type
                viewModel.setIndexComboBox(state.itemsComboBox.indexOf(type))
                val description = itemsMarker[state.indexMarker].description
                viewModel.setTextDescription(description)
            }else{
                viewModel.setIndexComboBox(0)
                viewModel.setTextDescription("")
                viewModel.setShowPosition(false)
            }
            //Refresca posicion del mapa (Solo si no esta en modo Test)
            if(!isTestMode){
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(startPosition, 18f))
            }
        }
    }
}

@Composable
private fun ObserveViewModelState(
    viewModel: AppViewModel,
    navController: NavHostController,
    context: Context
){
    LaunchedEffect(key1 = true) {
        viewModel.eventChannel.collect { event ->
            when (event) {
                is UiEvent.Navigate -> navController.navigate(event.route)
                is UiEvent.ShowToast -> Toast.makeText(
                        context, event.message, Toast.LENGTH_SHORT
                    ).show()
                is UiEvent.ShowLog -> Log.d("Test",event.message)
            }
        }
    }
}