package com.yjotdev.accidentreporter.presentation.mvvm.ui

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
import com.yjotdev.accidentreporter.presentation.theme.AccidentReporterTheme
import com.yjotdev.accidentreporter.presentation.components.ButtonAccidentReporter
import com.yjotdev.accidentreporter.presentation.components.ComboBoxAccidentReporter
import com.yjotdev.accidentreporter.presentation.components.TextFieldAccidentReporter
import com.yjotdev.accidentreporter.presentation.utils.ComponentPreview

const val EDIT_POSITION_VIEW_COMBO_BOX = "editpositionview_combobox"
const val EDIT_POSITION_VIEW_TEXT_FIELD = "editpositionview_textfield"
const val EDIT_POSITION_VIEW_EDIT_BUTTON = "editpositionview_editbutton"
const val EDIT_POSITION_VIEW_UPDATE_BUTTON = "editpositionview_updatebutton"

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
            modifier = Modifier.testTag(EDIT_POSITION_VIEW_COMBO_BOX)
        )
        TextFieldAccidentReporter(
            value = textDescription,
            onValueChange = { onTextDescription(it) },
            labelText = stringResource(R.string.textfield_description),
            enabled = enableTextDescription,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .testTag(EDIT_POSITION_VIEW_TEXT_FIELD)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(R.drawable.edit),
                contentDescription = EDIT_POSITION_VIEW_EDIT_BUTTON,
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
                    .testTag(EDIT_POSITION_VIEW_UPDATE_BUTTON)
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