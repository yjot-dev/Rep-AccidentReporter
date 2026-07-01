package com.yjotdev.accidentreporter

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import android.Manifest
import androidx.compose.ui.test.click
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import com.yjotdev.accidentreporter.presentation.utils.TestTags
import com.yjotdev.accidentreporter.presentation.navigation.PermissionView
import com.yjotdev.accidentreporter.presentation.navigation.ViewRoutes
import com.yjotdev.accidentreporter.presentation.theme.AccidentReporterTheme

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com.yjotdev.accidentreporter/tools/testing).
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationViewInstrumentedTest {

    @get:Rule(order = 0)
    var hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @get:Rule(order = 2)
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var navController: TestNavHostController // NavController del Test

    @Before
    fun init() {
        hiltRule.inject() // Inicializa Hilt
    }

    @Test
    fun navigationToAddPositionViewTest() {
        // Navegación de StartView -> CountryConfigView -> MapView
        navigationToCountryConfigViewTest()

        // Hago clic en continuar
        composeTestRule.onNodeWithTag(TestTags.COUNTRY_VIEW_BUTTON_2)
            .performClick()

        // Esperamos navegación a MapView
        waitForRoute(ViewRoutes.Map.name)

        // MapView -> Click en el Mapa Fake
        composeTestRule.onNodeWithTag(TestTags.GOOGLE_MAP).performTouchInput {
            click(percentOffset(0.1f, 0.1f)) // Click en la esquina superior
        }

        // Esperamos navegación a AddPositionView
        waitForRoute(ViewRoutes.AddPosition.name)

        // selecciono el combobox para desplegar los tipos de incidentes
        composeTestRule.onNodeWithTag(TestTags.ADD_POSITION_VIEW_COMBO_BOX)
            .performClick()

        // Elijo el tipo de incidente (Ejemplo item 3, el primero es el encabezado)
        composeTestRule.onNodeWithTag(TestTags.COMBO_BOX_ITEM_POS)
            .performClick()

        // Escribo la descripción del incidente
        composeTestRule.onNodeWithTag(TestTags.ADD_POSITION_VIEW_TEXT_FIELD)
            .performTextInput("Hay un problema de transito entre la calle A y B.")

        // Hago clic en el botón de agregar
        composeTestRule.onNodeWithTag(TestTags.ADD_POSITION_VIEW_BUTTON)
            .performClick()

        // Verificación final
        assertEquals(ViewRoutes.AddPosition.name, navController.currentDestination?.route)
    }

    @Test
    fun navigationToEditPositionViewTest() {
        // Navegación de StartView -> CountryConfigView -> MapView
        navigationToCountryConfigViewTest()

        // Hago clic en continuar
        composeTestRule.onNodeWithTag(TestTags.COUNTRY_VIEW_BUTTON_2)
            .performClick()

        // Esperamos navegación a MapView
        waitForRoute(ViewRoutes.Map.name)

        // MapView -> Click en un Marcador Existente (Fake Marker)
        composeTestRule.onNodeWithTag(TestTags.MARKET_POS)
            .performClick()

        composeTestRule.waitForIdle()

        // Position (AlertDialog) -> Click en "Ver/Editar"
        composeTestRule.onNodeWithContentDescription(TestTags.MAP_VIEW_LOOK_BUTTON)
            .performClick()

        // Esperamos navegación a EditPositionView
        waitForRoute(ViewRoutes.EditPosition.name)

        // Verificamos que exista un combobox
        composeTestRule.onNodeWithTag(TestTags.EDIT_POSITION_VIEW_COMBO_BOX)
            .assertExists()

        // Verificación final
        assertEquals(ViewRoutes.EditPosition.name, navController.currentDestination?.route)
    }

    @Test
    fun navigationToTokenConfigViewTest() {
        loadTestActivity()
        // StartView -> Click en Configurar Token
        composeTestRule.onNodeWithTag(TestTags.START_VIEW_BUTTON_1)
            .performClick()

        // Esperamos navegación a TokenConfigView
        waitForRoute(ViewRoutes.TokenConfig.name)

        // Hago clic en el botón editar
        composeTestRule.onNodeWithContentDescription(TestTags.TOKEN_CONFIG_VIEW_EDIT_BUTTON)
            .performClick()

        // Escribo el nuevo token
        composeTestRule.onNodeWithTag(TestTags.TOKEN_CONFIG_VIEW_TEXT_FIELD)
            .performTextReplacement("a7cf5ac786824acaccff4d533832f1f5")

        // Hago clic en el botón actualizar
        composeTestRule.onNodeWithTag(TestTags.TOKEN_CONFIG_VIEW_UPDATE_BUTTON)
            .performClick()

        // Verificación final
        assertEquals(ViewRoutes.TokenConfig.name, navController.currentDestination?.route)
    }

    @Test
    fun navigationToCountryConfigViewTest() {
        loadTestActivity()
        // StartView -> Clic en Configurar Pais
        composeTestRule.onNodeWithTag(TestTags.START_VIEW_BUTTON_2)
            .performClick()

        // Esperamos navegación a CountryConfigView
        waitForRoute(ViewRoutes.LocationConfig.name)

        // Escribo el país
        composeTestRule.onNodeWithTag(TestTags.COUNTRY_VIEW_TEXT_FIELD_1)
            .performTextReplacement("Ecuador")

        // Escribo la provincia
        composeTestRule.onNodeWithTag(TestTags.COUNTRY_VIEW_TEXT_FIELD_2)
            .performTextReplacement("El Oro")

        // Escribo la ciudad
        composeTestRule.onNodeWithTag(TestTags.COUNTRY_VIEW_TEXT_FIELD_3)
            .performTextReplacement("El Guabo")

        // Hago clic en el botón buscar ubicación
        composeTestRule.onNodeWithTag(TestTags.COUNTRY_VIEW_BUTTON_1)
            .performClick()

        // Verificación final
        assertEquals(ViewRoutes.LocationConfig.name, navController.currentDestination?.route)
    }

    @Test
    fun navigationToMapViewAndGoBackTest() {
        // Navegación de StartView -> CountryConfigView -> MapView
        navigationToCountryConfigViewTest()

        // Hago clic en continuar
        composeTestRule.onNodeWithTag(TestTags.COUNTRY_VIEW_BUTTON_2)
            .performClick()

        // Esperamos navegación a MapView
        waitForRoute(ViewRoutes.Map.name)

        // Verificamos que existe un marcador
        composeTestRule.onNodeWithTag(TestTags.MARKET_POS).assertExists()

        // Volvemos a la vista de inicio
        composeTestRule.onNodeWithContentDescription(TestTags.BACK_BUTTON)
            .performClick()

        // Esperamos navegación a CountryConfigView
        waitForRoute(ViewRoutes.LocationConfig.name)

        // Verificamos que existe un botón "Continuar"
        composeTestRule.onNodeWithTag(TestTags.COUNTRY_VIEW_BUTTON_2)
            .assertExists()

        // Verificación final
        assertEquals(ViewRoutes.LocationConfig.name, navController.currentDestination?.route)
    }

    /**
     * Función para cargar la vista de prueba.
     * Evita errores de aserción inmediata antes de que la vista se cargue.
     */
    private fun loadTestActivity() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            AccidentReporterTheme {
                PermissionView(
                    navController = navController,
                    isTestMode = true
                )
            }
        }
    }

    /**
     * Función auxiliar para esperar a que cambie la ruta de navegación.
     * Evita errores de aserción inmediata antes de que la transición termine.
     */
    private fun waitForRoute(routeName: String, timeoutMillis: Long = 5000) {
        composeTestRule.waitUntil(timeoutMillis) {
            navController.currentDestination?.route == routeName
        }
    }
}