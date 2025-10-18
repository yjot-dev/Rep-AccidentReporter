package com.yjotdev.accidentreporter.application.navigation

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.application.components.LoadingScreen
import com.yjotdev.accidentreporter.application.mvvm.view.AddPositionView
import com.yjotdev.accidentreporter.application.mvvm.view.EditPositionView
import com.yjotdev.accidentreporter.application.mvvm.view.MapView
import com.yjotdev.accidentreporter.application.mvvm.view.StartView
import com.yjotdev.accidentreporter.application.mvvm.viewmodel.AppViewModel

@Composable
fun NavigationView(
    navController: NavHostController = rememberNavController(),
    viewModel: AppViewModel
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
    //Navegacion
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
                    onNext = {
                        if(state.token == 0) viewModel.loadToken()
                        if(state.itemsComboBox.isEmpty()) viewModel.setItemsComboBox(optionList)
                        if(state.itemsMarker.isNullOrEmpty()) {
                            viewModel.getReports()
                            if(state.wasFound) viewModel.clearFlags()
                        }
                        navController.navigate(ViewRoutes.Map.name)
                    }
                )
            }
            composable(route = ViewRoutes.Map.name) {
                if(state.isLoading){
                    LoadingScreen()
                } else {
                    MapView(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = viewModel,
                        onToLook = { navController.navigate(ViewRoutes.EditPosition.name) },
                        onDelete = {
                            viewModel.deleteReport()
                            if(state.wasDeleted){
                                Toast.makeText(
                                    context, context.getString(R.string.toast_delete_true),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else {
                                Toast.makeText(
                                    context, context.getString(R.string.toast_delete_false),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            viewModel.clearFlags()
                        },
                        onMapClick = {
                            viewModel.setPosMarker(it)
                            viewModel.setIndexComboBox(0)
                            viewModel.setTextDescription("")
                            viewModel.setShowPosition(false)
                            navController.navigate(ViewRoutes.AddPosition.name)
                        }
                    )
                }
            }
            composable(route = ViewRoutes.AddPosition.name) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    AddPositionView(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = viewModel,
                        onAdd = {
                            viewModel.insertReport()
                            if(state.wasInserted){
                                Toast.makeText(context, context.getString(R.string.toast_insert_true),
                                    Toast.LENGTH_SHORT).show()
                            }else {
                                Toast.makeText(context, context.getString(R.string.toast_insert_false),
                                    Toast.LENGTH_SHORT).show()
                            }
                            viewModel.clearFlags()
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
                        viewModel = viewModel,
                        onUpdate = {
                            viewModel.updateReport()
                            if (state.wasUpdated){
                                Toast.makeText(context, context.getString(R.string.toast_update_true),
                                    Toast.LENGTH_SHORT).show()
                            }else {
                                Toast.makeText(context, context.getString(R.string.toast_update_false),
                                    Toast.LENGTH_SHORT).show()
                            }
                            viewModel.clearFlags()
                        }
                    )
                    if(state.isLoading) LoadingScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleBar(
    viewRoutes: ViewRoutes,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit
){
    if(canNavigateBack) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = viewRoutes.idTitle),
                    style = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back),
                    contentDescription = "backbutton",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.dp_4))
                        .clickable { navigateUp() }
                        .testTag("backbutton")
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}