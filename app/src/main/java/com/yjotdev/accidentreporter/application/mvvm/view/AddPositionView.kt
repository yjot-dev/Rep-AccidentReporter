package com.yjotdev.accidentreporter.application.mvvm.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.application.theme.AccidentReporterTheme
import com.yjotdev.accidentreporter.application.components.ButtonAccidentReporter
import com.yjotdev.accidentreporter.application.components.ComboBoxAccidentReporter
import com.yjotdev.accidentreporter.application.components.TextFieldAccidentReporter
import com.yjotdev.accidentreporter.application.utils.ComponentPreview

@Composable
fun AddPositionView(
    modifier: Modifier = Modifier,
    itemsComboBox: List<String>,
    enableBtnReport: Boolean,
    textDescription: String,
    onTextDescription: (String) -> Unit,
    indexSelected: Int,
    onIndexSelected: (Int) -> Unit,
    onAdd: () -> Unit
){
    Column(
        modifier = modifier.padding(
            vertical = dimensionResource(R.dimen.dp_3)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        ComboBoxAccidentReporter(
            optionList = itemsComboBox,
            indexSelected = indexSelected,
            onIndexSelected = { onIndexSelected(it) },
            modifier = Modifier.testTag("addpositionview_combobox")
        )
        TextFieldAccidentReporter(
            value = textDescription,
            onValueChange = { onTextDescription(it) },
            labelText = stringResource(R.string.textfield_description),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .testTag("addpositionview_textfield")
        )
        ButtonAccidentReporter(
            onClick = onAdd,
            text = stringResource(R.string.addpositionview_button),
            enabled = enableBtnReport,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(dimensionResource(R.dimen.dp_5))
                .testTag("addpositionview_button")
        )
    }
}

@ComponentPreview
@Composable
private fun PreviewAddPositionView(){
    AccidentReporterTheme {
        AddPositionView(
            modifier = Modifier.fillMaxSize(),
            itemsComboBox = listOf("Item A", "Item B", "Item C"),
            enableBtnReport = false,
            textDescription = "Hay un problema de transito entre la calle A y B.",
            onTextDescription = {},
            indexSelected = 0,
            onIndexSelected = {},
            onAdd = {}
        )
    }
}