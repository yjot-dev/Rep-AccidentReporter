package com.yjotdev.accidentreporter.presentation.mvvm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.presentation.components.Position
import com.yjotdev.accidentreporter.presentation.theme.AccidentReporterTheme
import com.yjotdev.accidentreporter.presentation.utils.ComponentPreview
import com.yjotdev.accidentreporter.domain.model.ReportModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(
    modifier: Modifier = Modifier,
    isTestMode: Boolean,
    location: LatLng,
    itemsMarker: List<ReportModel>,
    indexMarker: Int,
    showPosition: Boolean,
    enableBtnDelete: Boolean,
    onShowPosition: (Boolean) -> Unit,
    onToLook: () -> Unit,
    onDelete: () -> Unit,
    onMapClick: (LatLng) -> Unit,
    onInfoWindowClick: (LatLng, Int) -> Unit
){
    //Mapa
    if(isTestMode) {
        Box(modifier = modifier
            .background(Color.LightGray)
            .clickable { onMapClick(LatLng(-3.245, -79.832)) }
            .testTag("googleMap")
        ){
            itemsMarker.forEachIndexed { index, pos ->
                //Coordenada de marcador en mapa
                val marker = LatLng(pos.latitude, pos.longitude)
                Button(
                    onClick = { onInfoWindowClick(marker, index) },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("Market:${index + 1}")
                ) {
                    Text("${index + 1}: ${pos.type}")
                }
            }
        }
    }else {
        //Observa estado de la camara del mapa
        val cameraPositionState = rememberCameraPositionState()
        LaunchedEffect(key1 = location) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(location, 18f),
                1000
            )
        }
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
                    state = rememberMarkerState(
                        position = marker
                    ),
                    title = "${index + 1}: ${pos.type}",
                    contentDescription = "Market:${index + 1}",
                    onClick = {
                        it.showInfoWindow()
                        false
                    },
                    onInfoWindowClick = { onInfoWindowClick(it.position, index) }
                )
            }
        }
    }
    //Muestra e oculta la informacion del marcador seleccionado
    if(showPosition){
        BasicAlertDialog(
            onDismissRequest = { onShowPosition(false) },
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
                date = itemsMarker[indexMarker].date,
                type = itemsMarker[indexMarker].type,
                enabledDelete = enableBtnDelete,
                onToLook = onToLook,
                onDelete = onDelete
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PreviewMapView(){
    AccidentReporterTheme {
        MapView(
            modifier = Modifier.fillMaxSize(),
            isTestMode = true,
            location = LatLng(-3.245, -79.832),
            itemsMarker = listOf(
                ReportModel(
                    id = 0,
                    latitude = -3.245448,
                    longitude = -79.832331,
                    date = "15/03/2025",
                    type = "Accidentes",
                    description = "Hubo un accidente en la calle 12",
                    token = "a7cf5ac786824acaccff4d533832f1f5"
                )
            ),
            indexMarker = 0,
            showPosition = true,
            enableBtnDelete = true,
            onShowPosition = {},
            onToLook = {},
            onDelete = {},
            onMapClick = {},
            onInfoWindowClick = {_, _ ->}
        )
    }
}