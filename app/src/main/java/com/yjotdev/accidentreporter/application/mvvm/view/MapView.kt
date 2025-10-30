package com.yjotdev.accidentreporter.application.mvvm.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.application.components.Position
import com.yjotdev.accidentreporter.application.mvvm.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    onToLook: () -> Unit,
    onDelete: () -> Unit,
    onMapClick: (LatLng) -> Unit
){
    val state by viewModel.uiState.collectAsState()
    //Zoom a El Guabo por coordenadas
    val elGuabo = LatLng(-3.245274, -79.832028)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(elGuabo, 18f)
    }
    //Refresca el mapa
    LaunchedEffect(cameraPositionState) {
        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(elGuabo, 18f))
    }
    //Mapa
    state.itemsMarker?.let { itemsMarker ->
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            contentDescription = "googleMap",
            onMapClick = onMapClick
        ) {
            itemsMarker.forEachIndexed { index, pos ->
                //Coordenada de marcador en mapa
                val marker = LatLng(pos.latitude, pos.longitude)
                Marker(
                    state = rememberUpdatedMarkerState(
                        position = marker
                    ),
                    title = "${index + 1}: ${pos.type}",
                    contentDescription = "${index + 1}",
                    onClick = {
                        it.showInfoWindow()
                        false
                    },
                    onInfoWindowClick = {
                        viewModel.setPosMarker(it.position)
                        viewModel.setIndexMarker(index)
                        viewModel.setShowPosition(viewModel.showMarker())
                        viewModel.setEnableUpdate(false)
                    }
                )
            }
        }
        //Muestra e oculta la informacion del marcador seleccionado
        if(state.showPosition){
            BasicAlertDialog(
                onDismissRequest = { viewModel.setShowPosition(false) },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .border(
                        width = dimensionResource(R.dimen.dp_1),
                        color = MaterialTheme.colorScheme.secondary,
                        shape = MaterialTheme.shapes.extraLarge
                    )
            ) {
                Position(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    date = itemsMarker[state.indexMarker].date,
                    type = itemsMarker[state.indexMarker].type,
                    enabledDelete = viewModel.verifyUser(),
                    onToLook = {
                        viewModel.setShowPosition(false)
                        onToLook()
                    },
                    onDelete = {
                        viewModel.setShowPosition(false)
                        onDelete()
                    }
                )
            }
        }
    }
}