package com.yjotdev.accidentreporter.presentation.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Anotación de Preview para modo claro.
 * Especifica un apiLevel estable (34) para evitar problemas de renderizado
 * con SDKs de desarrollo.
 */
@Preview(
    name = "Light Mode",
    showBackground = true,
    backgroundColor = 0xFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    apiLevel = 34
)
/**
 * Anotación de Preview para modo oscuro.
 * Especifica un apiLevel estable (34) para evitar problemas de renderizado
 * con SDKs de desarrollo.
 */
@Preview(
    name = "Dark Mode",
    showBackground = true,
    backgroundColor = 0x000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    apiLevel = 34
)
annotation class ComponentPreview