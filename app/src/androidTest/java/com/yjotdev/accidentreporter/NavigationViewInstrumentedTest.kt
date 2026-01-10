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
import junit.framework.TestCase.assertEquals
import com.yjotdev.accidentreporter.application.navigation.PermissionView
import com.yjotdev.accidentreporter.application.navigation.ViewRoutes
import com.yjotdev.accidentreporter.application.theme.AccidentReporterTheme

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
        loadTestActivity()
        // 1. StartView -> Click en Continuar
        composeTestRule.onNodeWithTag("startview_button2")
            .performClick()

        // Esperamos navegación a MapView
        waitForRoute(ViewRoutes.Map.name)

        // 2. MapView -> Click en el Mapa Fake
        composeTestRule.onNodeWithTag("googleMap").performTouchInput {
            click(percentOffset(0.1f, 0.1f)) // Click en la esquina superior
        }

        // 3. Esperamos navegación a AddPositionView
        waitForRoute(ViewRoutes.AddPosition.name)

        // Selecciono el combobox para desplegar los tipos de incidentes
        composeTestRule.onNodeWithTag("addpositionview_combobox")
            .performClick()

        // Elijo el tipo de incidente (Ejemplo item 3, el 1ro es el encabezado)
        composeTestRule.onNodeWithTag("combobox_item:3")
            .performClick()

        // Escribo la descripcion del incidente
        composeTestRule.onNodeWithTag("addpositionview_textfield")
            .performTextInput("Hay un problema de transito entre la calle A y B.")

        // Hago click en el boton de agregar
        composeTestRule.onNodeWithTag("addpositionview_button")
            .performClick()

        // Verificación final
        assertEquals(ViewRoutes.AddPosition.name, navController.currentDestination?.route)
    }

    @Test
    fun navigationToEditPositionViewTest() {
        loadTestActivity()
        // 1. StartView -> Click en Continuar
        composeTestRule.onNodeWithTag("startview_button2")
            .performClick()

        // Esperamos navegación a MapView
        waitForRoute(ViewRoutes.Map.name)

        // 2. MapView -> Click en un Marcador Existente (Fake Marker)
        composeTestRule.onNodeWithTag("Market:1")
            .performClick()

        composeTestRule.waitForIdle()

        // 3. Position (AlertDialog) -> Click en "Ver/Editar"
        composeTestRule.onNodeWithContentDescription("mapview_lookbutton")
            .performClick()

        // 4. Esperamos navegación a EditPositionView
        waitForRoute(ViewRoutes.EditPosition.name)

        // Verificamos que exista un combobox
        composeTestRule.onNodeWithTag("editpositionview_combobox")
            .assertExists()

        // Verificación final
        assertEquals(ViewRoutes.EditPosition.name, navController.currentDestination?.route)
    }

    @Test
    fun navigationToTokenConfigViewTest() {
        loadTestActivity()
        // 1. StartView -> Click en Configurar Token
        composeTestRule.onNodeWithTag("startview_button1")
            .performClick()

        // 2. Esperamos navegación a TokenConfigView
        waitForRoute(ViewRoutes.TokenConfig.name)

        // Hago click en el boton editar
        composeTestRule.onNodeWithContentDescription("tokenconfigview_editbutton")
            .performClick()

        // Escribo el nuevo token
        composeTestRule.onNodeWithTag("tokenconfigview_textfield")
            .performTextReplacement("123456789")

        // Hago click en el boton actualizar
        composeTestRule.onNodeWithTag("tokenconfigview_updatebutton")
            .performClick()

        // Verificación final
        assertEquals(ViewRoutes.TokenConfig.name, navController.currentDestination?.route)
    }

    @Test
    fun navigationToMapViewAndGoBackTest() {
        loadTestActivity()
        // 1. StartView -> Click en Continuar
        composeTestRule.onNodeWithTag("startview_button2")
            .performClick()

        // 2. Esperamos navegación a MapView
        waitForRoute(ViewRoutes.Map.name)

        // 3. Verificamos que existe un marcador
        composeTestRule.onNodeWithTag("Market:1").assertExists()

        // 4. Volvemos a la vista de inicio
        composeTestRule.onNodeWithContentDescription("backbutton")
            .performClick()

        // 5. Esperamos navegación a StartView
        waitForRoute(ViewRoutes.Start.name)

        // 6. Verificamos que existe un boton "Continuar"
        composeTestRule.onNodeWithTag("startview_button2")
            .assertExists()

        // Verificación final
        assertEquals(ViewRoutes.Start.name, navController.currentDestination?.route)
    }

    /**
     * Funcion para cargar la vista de prueba.
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