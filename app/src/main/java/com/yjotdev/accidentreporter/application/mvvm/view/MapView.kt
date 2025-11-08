package com.yjotdev.accidentreporter.application.mvvm.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.application.components.Position
import com.yjotdev.accidentreporter.domain.entity.ReportEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    itemsMarker: List<ReportEntity>,
    indexMarker: Int,
    showPosition: Boolean,
    enableBtnDelete: Boolean,
    onShowPosition: (Boolean) -> Unit,
    onToLook: () -> Unit,
    onDelete: () -> Unit,
    onMapClick: (LatLng) -> Unit,
    onInfoWindowClick: (Marker, Int) -> Unit
){
    //Mapa
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
                onInfoWindowClick = { onInfoWindowClick(it, index) }
            )
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