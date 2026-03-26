package com.yjotdev.accidentreporter.application.mvvm.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.application.components.ButtonAccidentReporter
import com.yjotdev.accidentreporter.application.components.TextFieldAccidentReporter
import com.yjotdev.accidentreporter.application.theme.AccidentReporterTheme
import com.yjotdev.accidentreporter.application.utils.ComponentPreview

@Composable
fun LocationConfigView(
    modifier: Modifier = Modifier,
    country: String,
    province: String,
    city: String,
    enableOnMap: Boolean,
    onCountry: (String) -> Unit,
    onProvince: (String) -> Unit,
    onCity: (String) -> Unit,
    onSearchLocation: () -> Unit,
    onMap: () -> Unit
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        TextFieldAccidentReporter(
            idHeight = R.dimen.dp_5,
            labelText = stringResource(R.string.textfield_country),
            value = country,
            onValueChange = onCountry,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .testTag("countryview_textfield1")
        )
        TextFieldAccidentReporter(
            idHeight = R.dimen.dp_5,
            labelText = stringResource(R.string.textfield_province),
            value = province,
            onValueChange = onProvince,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .testTag("countryview_textfield2")
        )
        TextFieldAccidentReporter(
            idHeight = R.dimen.dp_5,
            labelText = stringResource(R.string.textfield_city),
            value = city,
            onValueChange = onCity,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .testTag("countryview_textfield3")
        )
        ButtonAccidentReporter(
            onClick = onSearchLocation,
            text = stringResource(R.string.locationconfigview_button1),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(dimensionResource(R.dimen.dp_5))
                .testTag("countryview_button1")
        )
        ButtonAccidentReporter(
            enabled = enableOnMap,
            onClick = onMap,
            text = stringResource(R.string.locationconfigview_button2),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(dimensionResource(R.dimen.dp_5))
                .testTag("countryview_button2")
        )
    }
}

@ComponentPreview
@Composable
private fun PreviewLocationConfigView() {
    AccidentReporterTheme {
        LocationConfigView(
            modifier = Modifier.fillMaxSize(),
            country = "Ecuador",
            province = "El Oro",
            city = "El Guabo",
            enableOnMap = true,
            onCountry = {},
            onProvince = {},
            onCity = {},
            onSearchLocation = {},
            onMap = {}
        )
    }
}