package uz.coderqwerty.taskwithgooglemap.presentation.ui.screens.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import uz.coderqwerty.taskwithgooglemap.R
import uz.coderqwerty.taskwithgooglemap.domain.model.Branch
import uz.coderqwerty.taskwithgooglemap.domain.model.Location
import uz.coderqwerty.taskwithgooglemap.domain.model.MyClusterItem
import uz.coderqwerty.taskwithgooglemap.presentation.ui.screens.item.BranchSheetContent
import uz.coderqwerty.taskwithgooglemap.presentation.ui.utils.CustomBottomSheet

class MainScreen : Screen {

    @Composable
    override fun Content() {

        val context = LocalContext.current

        // Hozirgi ruxsat holati
        var isLocationGranted by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            )
        }

        // ruxsat berilmagan bo'lsa sozlamalarni ochish uchun
        var showOpenSettingsButton by remember { mutableStateOf(false) }

        // Ruxsat so'rovini ishga tushuruvchi (Launcher)
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                val fineLocationGranted =
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                val coarseLocationGranted =
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

                // Agar ikkalsidan biri berilgan bo'lsa ham true bo'ladi
                isLocationGranted = fineLocationGranted || coarseLocationGranted
            }
        )

        // App settingsdan qaytganda ruxsatni qayta tekshirish (Lifecycle orqali)
        val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
        androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
            val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
                if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                    // Appga qaytib kirganda tekshiramiz
                    isLocationGranted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        if (isLocationGranted) {
            val viewModel: MainScreenContract.Model = hiltViewModel<MainScreenViewModelImpl>()

            ScreenContent(
                viewModel = viewModel
            )
        }else{
            // Ruxsat yo'q -> Chiroyli so'rov oynasi
            PermissionRequestContent(
                isPermanentlyDenied = showOpenSettingsButton,
                onClick = {
                    if (showOpenSettingsButton) {
                        // Agar oldin rad etilgan bo'lsa -> Settingsni ochamiz
                        openAppSettings(context)
                    } else {
                        // Birinchi marta -> Ruxsat so'raymiz
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    viewModel: MainScreenContract.Model
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                state.branches[0].location.lat,
                state.branches[0].location.long
            ), 12f
        )
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddings ->
        Box(
            modifier = Modifier
                .padding(
                    top = paddings.calculateTopPadding(),
                    bottom = paddings.calculateBottomPadding()
                )
                .fillMaxSize()
        )
        {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL),
            ) {

                Clustering(
                    items = state.branches.map {
                        MyClusterItem(
                            position = LatLng(it.location.lat, it.location.long),
                            title = it.name,
                            snippet = it.address,
                        )
                    },
                    clusterContent = { cluster ->
                        Box {
                            Image(
                                painter = painterResource(id = R.drawable.charging_location_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.Center) // Ikon o‘lchami
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(color = Color(0xfff7a400)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    textAlign = TextAlign.Center,
                                    text = cluster.size.toString(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    lineHeight = 4.sp
                                )
                            }
                        }
                    },
                    clusterItemContent = { item ->
                        Image(
                            painter = painterResource(id = item.iconResId),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp) // Ikon o‘lchami
                        )

                    },
                    onClusterClick = { cluster ->
                        // Klaster bosilganda nima qilish kerak
                        true // True qaytarish klaster zoomini faollashtiradi
                    },
                    onClusterItemClick = {
                        // Alohida marker bosilganda nima qilish kerak
                        viewModel.onBranchSelected(it)
                        true
                    }
                )

            }

        }

        if (state.selectedBranch != null) {

            CustomBottomSheet {
                // Sheet ichidagi kontent
                BranchSheetContent(
                    distance = state.distance,
                    branch = state.selectedBranch!!,
                    onClose = {
                        viewModel.onBottomSheetClosed()
                    },
                    onClickDirections = {
                        viewModel.getDirections(it)
                    }
                )
            }

        }
    }

}

@Composable
fun PermissionRequestContent(
    isPermanentlyDenied: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Joylashuv ruxsati kerak",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Sizga eng yaqin filiallarni topish va masofani hisoblash uchun ilovaga joylashuvingizni bilish ruxsati zarur.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = if (isPermanentlyDenied) "Sozlamalarni ochish" else "Ruxsat berish",
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Agar settingsga o'tish kerak bo'lsa, kichik yordamchi tekst
            if (isPermanentlyDenied) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Sozlamalardan 'Permissions' bo'limiga o'ting va Location ni yoqing.",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        }
    }
}

private fun openAppSettings(context: android.content.Context) {
    val intent = android.content.Intent(
        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        android.net.Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}