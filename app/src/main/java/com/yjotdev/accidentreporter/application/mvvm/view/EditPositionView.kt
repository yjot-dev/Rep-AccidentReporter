package com.yjotdev.accidentreporter.application.mvvm.view

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.application.theme.AccidentReporterTheme
import com.yjotdev.accidentreporter.application.mvvm.viewmodel.AppViewModel
import com.yjotdev.accidentreporter.application.composable.ButtonAccidentReporter
import com.yjotdev.accidentreporter.application.composable.ComboBoxAccidentReporter
import com.yjotdev.accidentreporter.application.composable.TextFieldAccidentReporter

@Composable
fun EditPositionView(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    onUpdate: () -> Unit
){
    val state by viewModel.uiState.collectAsState()
    val optionList = state.itemsComboBox
    LaunchedEffect(Unit) {
        val type = state.itemsMarker[state.indexMarker].type
        viewModel.setIndexComboBox(optionList.indexOf(type))
        val description = state.itemsMarker[state.indexMarker].description
        viewModel.setTextDescription(description)
    }
    Column(
        modifier = modifier.padding(
            vertical = dimensionResource(R.dimen.dp_3)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        ComboBoxAccidentReporter(
            optionList = optionList,
            indexSelected = state.indexComboBox,
            onIndexSelected = { viewModel.setIndexComboBox(it) },
            enabled = state.enableUpdate && viewModel.verifyUser(),
            modifier = Modifier.testTag("editpositionview_combobox")
        )
        TextFieldAccidentReporter(
            value = state.textDescription,
            onValueChange = { viewModel.setTextDescription(it) },
            labelText = stringResource(R.string.textfield_description),
            enabled = state.enableUpdate && viewModel.verifyUser(),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .testTag("editpositionview_textfield")
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(R.drawable.edit),
                contentDescription = "editpositionview_editbutton",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(dimensionResource(R.dimen.dp_5))
                    .alpha(if(viewModel.verifyUser()) 1f else 0.5f)
                    .clickable(enabled = viewModel.verifyUser()){
                        viewModel.setEnableUpdate(!state.enableUpdate)
                    }
            )
            ButtonAccidentReporter(
                onClick = onUpdate,
                text = stringResource(R.string.editpositionview_button),
                enabled = viewModel.enabledForm()
                        && state.enableUpdate && viewModel.verifyUser(),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(dimensionResource(R.dimen.dp_5))
                    .testTag("editpositionview_updatebutton")
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun PreviewEditPositionView(){
    AccidentReporterTheme {
        EditPositionView(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel(),
            onUpdate = {}
        )
    }
}