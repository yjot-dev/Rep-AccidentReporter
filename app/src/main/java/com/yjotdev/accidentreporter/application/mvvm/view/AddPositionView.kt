package com.yjotdev.accidentreporter.application.mvvm.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
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
fun AddPositionView(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel,
    onAdd: () -> Unit
){
    val state by viewModel.uiState.collectAsState()
    val optionList = state.itemsComboBox
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
            modifier = Modifier.testTag("addpositionview_combobox")
        )
        TextFieldAccidentReporter(
            value = state.textDescription,
            onValueChange = { viewModel.setTextDescription(it) },
            labelText = stringResource(R.string.textfield_description),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .testTag("addpositionview_textfield")
        )
        ButtonAccidentReporter(
            onClick = onAdd,
            text = stringResource(R.string.addpositionview_button),
            enabled = viewModel.enabledForm(),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(dimensionResource(R.dimen.dp_5))
                .testTag("addpositionview_button")
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun PreviewAddPositionView(){
    AccidentReporterTheme {
        AddPositionView(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel(),
            onAdd = {}
        )
    }
}