package uz.coderqwerty.taskwithgooglemap.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.coderqwerty.taskwithgooglemap.assembly.navigation.NavigationHandler
import uz.coderqwerty.taskwithgooglemap.presentation.ui.screens.main.MainScreen
import uz.coderqwerty.taskwithgooglemap.presentation.ui.theme.TaskWithGoogleMapTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: NavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskWithGoogleMapTheme {
                //Navigation controller
                Navigator(
                    screen = MainScreen(),
                    onBackPressed = { true }
                )
                { navigator ->

                    LaunchedEffect(key1 = navigator) {
                        navigationHandler.screenState
                            .onEach {
                                it.invoke(navigator)
                            }
                            .launchIn(lifecycleScope)
                    }

                    SlideTransition(navigator = navigator)
                }
            }
        }
    }
}