package com.yjotdev.accidentreporter

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.google.android.gms.maps.model.LatLng
import com.yjotdev.accidentreporter.application.mvvm.viewmodel.AppViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import javax.inject.Inject
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.runner.RunWith
import com.yjotdev.accidentreporter.application.navigation.PermissionView
import com.yjotdev.accidentreporter.application.navigation.ViewRoutes
import com.yjotdev.accidentreporter.application.theme.AccidentReporterTheme

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationViewInstrumentedTest {

    @get:Rule(order = 0)
    var hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var navController: TestNavHostController // NavController del Test

    @Before
    fun setup() {
        hiltRule.inject() // Inicializa Hilt
    }

    @Test
    fun navigationToMapViewTest() {
        //Usa UiDevice para interactuar con el mapa
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        //Usa ViewModelProvider para obtener una instancia del ViewModel
        val viewModel = ViewModelProvider(composeTestRule.activity)[AppViewModel::class.java]
        //Contenido de la vista
        composeTestRule.setContent {
            AccidentReporterTheme {
                PermissionView(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
        //Hace click en el boton Continuar
        composeTestRule.onNodeWithTag("startview_button")
            .performClick()
        //Verifica si la navegacion a MapView fue exitosae
        assertEquals(ViewRoutes.Map.name, navController.currentDestination?.route)
        //Espera un momento para que el mapa y los marcadores se carguen
        composeTestRule.waitForIdle()
        //Encuentra el marcador mediante su título o descripción
        val marker = uiDevice.findObject(UiSelector().descriptionContains("1"))
        //Hace click en el marcador para hacer visible el InfoWindow
        marker.click()
        //Espera un momento para que se vea el InfoWindow
        marker.waitForExists(500)
        //Hace click en el InfoWindow para hacer visible el AlertDialog
        composeTestRule.activity.runOnUiThread {
            //Selecciona el marcador deseado
            val state = viewModel.uiState.value
            val d = state.itemsMarker[0]
            val it = LatLng(d.latitude, d.longitude)
            viewModel.setPosMarker(it)
            viewModel.setIndexMarker(0)
            viewModel.setShowPosition(true)
            viewModel.setEnableUpdate(false)
        }
        //Espera un momento para que se vea el AlertDialog
        composeTestRule.waitForIdle()
    }

    @Test
    fun navigationToEditPositionViewTest(){
        navigationToMapViewTest()
        //Hace click en ver informacion del marcador del AlertDialog
        composeTestRule.onNodeWithContentDescription("mapview_lookbutton")
            .performClick()
        //Verifica si la navegacion a EditPositionView fue exitosae
        assertEquals(ViewRoutes.EditPosition.name, navController.currentDestination?.route)
        //Verifica si EditPositionView es visible
        composeTestRule.onNodeWithTag("editpositionview_combobox")
            .assertExists()
        //Hace click en el boton de regresar
        composeTestRule.onNodeWithTag("backbutton")
            .performClick()
        //Espera un momento para que el mapa y los marcadores se carguen
        composeTestRule.waitForIdle()
        //Verifica si la navegacion a MapView fue exitosae
        assertEquals(ViewRoutes.Map.name, navController.currentDestination?.route)
    }

    @Test
    fun navigationToAddPositionViewTest(){
        //Usa UiDevice para interactuar con el mapa
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        //Usa ViewModelProvider para obtener una instancia del ViewModel
        val viewModel = ViewModelProvider(composeTestRule.activity)[AppViewModel::class.java]
        //Contenido de la vista
        composeTestRule.setContent {
            AccidentReporterTheme {
                PermissionView(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
        //Hace click en el boton Continuar
        composeTestRule.onNodeWithTag("startview_button")
            .performClick()
        //Verifica si la navegacion a MapView fue exitosae
        assertEquals(ViewRoutes.Map.name, navController.currentDestination?.route)
        //Espera un momento para que el mapa y los marcadores se carguen
        composeTestRule.waitForIdle()
        //Encuentra el mapa mediante su título o descripción
        val map = uiDevice.findObject(UiSelector().descriptionContains("googleMap"))
        //Hace click en el mapa para hacer visible AddPositionView
        map.click()
        composeTestRule.activity.runOnUiThread {
            //Selecciona la ubicacion en el mapa
            val it = LatLng(-3.245300, -79.832161)
            viewModel.setPosMarker(it)
            viewModel.setIndexComboBox(0)
            viewModel.setTextDescription("")
            viewModel.setShowPosition(false)
            navController.navigate(ViewRoutes.AddPosition.name)
        }
        //Espera un momento para que se vea AddPositionView
        composeTestRule.waitForIdle()
        //Verifica si la navegacion a AddPositionView fue exitosae
        assertEquals(ViewRoutes.AddPosition.name, navController.currentDestination?.route)
    }

    @Test
    fun actionsAddPositionViewTest(){
        navigationToAddPositionViewTest()
        //Despliega el comboBox
        composeTestRule.onNodeWithTag("addpositionview_combobox")
            .performClick()
        //Da click en el item 2
        composeTestRule.onNodeWithTag("combobox_item:2")
            .performClick()
        //Espera un momento para que se vean los cambios
        composeTestRule.waitForIdle()
        //Escribe una descripcion del reporte
        composeTestRule.onNodeWithTag("addpositionview_textfield")
            .performTextInput("En la calle A diagonal calle B hay una inundacion.")
        //Da click en reportar para guardar el reporte
        composeTestRule.onNodeWithTag("addpositionview_button")
            .performClick()
        //Espera un momento para que se vean los cambios
        composeTestRule.waitForIdle()
        //Hace click en el boton de regresar
        composeTestRule.onNodeWithTag("backbutton")
            .performClick()
        //Espera un momento para que el mapa y los marcadores se carguen
        composeTestRule.waitForIdle()
        //Verifica si la navegacion a MapView fue exitosae
        assertEquals(ViewRoutes.Map.name, navController.currentDestination?.route)
    }
}