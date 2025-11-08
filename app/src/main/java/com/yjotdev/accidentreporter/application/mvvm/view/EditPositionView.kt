package com.yjotdev.accidentreporter.application.mvvm.view

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.yjotdev.accidentreporter.R
import com.yjotdev.accidentreporter.application.theme.AccidentReporterTheme
import com.yjotdev.accidentreporter.application.components.ButtonAccidentReporter
import com.yjotdev.accidentreporter.application.components.ComboBoxAccidentReporter
import com.yjotdev.accidentreporter.application.components.TextFieldAccidentReporter
import com.yjotdev.accidentreporter.application.utils.ComponentPreview

@Composable
fun EditPositionView(
    modifier: Modifier = Modifier,
    itemsComboBox: List<String>,
    enableComboBox: Boolean,
    enableTextDescription: Boolean,
    enableBtnEdit: Boolean,
    enableBtnUpdate: Boolean,
    onEnableBtnUpdate: (Boolean) -> Unit,
    textDescription: String,
    onTextDescription: (String) -> Unit,
    indexSelected: Int,
    onIndexSelected: (Int) -> Unit,
    onUpdate: () -> Unit
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
            enabled = enableComboBox,
            modifier = Modifier.testTag("editpositionview_combobox")
        )
        TextFieldAccidentReporter(
            value = textDescription,
            onValueChange = { onTextDescription(it) },
            labelText = stringResource(R.string.textfield_description),
            enabled = enableTextDescription,
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
                    .alpha(if(enableBtnEdit) 1f else 0.5f)
                    .clickable(enabled = enableBtnEdit){
                        onEnableBtnUpdate(enableBtnUpdate)
                    }
            )
            ButtonAccidentReporter(
                onClick = onUpdate,
                text = stringResource(R.string.editpositionview_button),
                enabled = enableBtnUpdate,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(dimensionResource(R.dimen.dp_5))
                    .testTag("editpositionview_updatebutton")
            )
        }
    }
}

@ComponentPreview
@Composable
private fun PreviewEditPositionView(){
    AccidentReporterTheme {
        EditPositionView(
            modifier = Modifier.fillMaxSize(),
            itemsComboBox = listOf("Item A", "Item B", "Item C"),
            enableComboBox = true,
            enableTextDescription = true,
            enableBtnEdit = true,
            enableBtnUpdate = true,
            onEnableBtnUpdate = {},
            textDescription = "Hay un problema de transito entre la calle A y B.",
            onTextDescription = {},
            indexSelected = 0,
            onIndexSelected = {},
            onUpdate = {}
        )
    }
}