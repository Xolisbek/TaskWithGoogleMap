package uz.coderqwerty.taskwithgooglemap.presentation.ui.screens.main

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.coderqwerty.taskwithgooglemap.domain.model.Branch
import uz.coderqwerty.taskwithgooglemap.domain.model.Location
import uz.coderqwerty.taskwithgooglemap.domain.model.MyClusterItem
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class MainScreenViewModelImpl @Inject constructor(
    private val application: Application
) : MainScreenContract.Model, ViewModel() {

    private val branches: List<Branch> = listOf(
        // 1. Amir Temur Xiyoboni (Markaz)
        Branch(
            name = "Markaziy Filial",
            address = "Amir Temur ko'chasi, 1-uy",
            openingTime = "09:00",
            closingTime = "21:00",
            weekends = "Yo'q",
            location = Location(
                lat = 41.311151,
                long = 69.279737
            )
        ),
        // 2. Chorsu Bozori
        Branch(
            name = "Chorsu Filiali",
            address = "Navoiy ko'chasi, Eski shahar",
            openingTime = "08:00",
            closingTime = "18:00",
            weekends = "Du",
            location = Location(
                lat = 41.327587,
                long = 69.239274
            )
        ),
        // 3. Toshkent Teleminorasi
        Branch(
            name = "Teleminora Filiali",
            address = "Amir Temur shoh ko'chasi, 109",
            openingTime = "10:00",
            closingTime = "20:00",
            weekends = "Yo'q",
            location = Location(
                lat = 41.378536,
                long = 69.287413
            )
        ),
        // 4. Magic City
        Branch(
            name = "Magic City Filiali",
            address = "Bobur ko'chasi, 174",
            openingTime = "10:00",
            closingTime = "23:00",
            weekends = "Yo'q",
            location = Location(
                lat = 41.304033,
                long = 69.253018
            )
        ),
        // 5. Samarqand Darvoza (AVM)
        Branch(
            name = "Samarqand Darvoza",
            address = "Qoratosh ko'chasi, 5A",
            openingTime = "10:00",
            closingTime = "22:00",
            weekends = "Yo'q",
            location = Location(
                lat = 41.316474,
                long = 69.230198
            )
        ),
        // 6. Xalqaro Aeroport (Islom Karimov nomidagi)
        Branch(
            name = "Aeroport Filiali",
            address = "Qumariq ko'chasi, 13",
            openingTime = "00:00", // 24 soat
            closingTime = "23:59",
            weekends = "Yo'q",
            location = Location(
                lat = 41.257989,
                long = 69.281222
            )
        ),
        // 7. Minor Masjidi
        Branch(
            name = "Minor Filiali",
            address = "Kichik Halqa Yo'li, Minor",
            openingTime = "08:00",
            closingTime = "20:00",
            weekends = "Juma",
            location = Location(
                lat = 41.342125,
                long = 69.274577
            )
        ),
        // 8. Tashkent City (Hilton)
        Branch(
            name = "Tashkent City",
            address = "Islom Karimov ko'chasi, 2",
            openingTime = "09:00",
            closingTime = "22:00",
            weekends = "Yo'q",
            location = Location(
                lat = 41.313498,
                long = 69.251845
            )
        ),
        // 9. Mega Planet (Yunusobod)
        Branch(
            name = "Mega planet savdo majmuasi",
            address = "Ahmad Donish ko'chasi, 2B",
            openingTime = "09:00",
            closingTime = "21:00",
            weekends = "Yo'q",
            location = Location(
                lat = 41.367252,
                long = 69.291776
            )
        ),
        // 10. Buyuk Ipak Yo'li (Maksim Gorkiy)
        Branch(
            name = "Buyuk Ipak Yo'li",
            address = "Mirzo Ulug'bek shoh ko'chasi",
            openingTime = "08:00",
            closingTime = "20:00",
            weekends = "Yak",
            location = Location(
                lat = 41.327660,
                long = 69.329430
            )
        ),
        // 11. Janubiy Vokzal
        Branch(
            name = "Vokzal Filiali",
            address = "Usmon Nosir ko'chasi",
            openingTime = "07:00",
            closingTime = "22:00",
            weekends = "Yo'q",
            location = Location(
                lat = 41.268310,
                long = 69.245840
            )
        ),
        // 12. Oloy Bozori
        Branch(
            name = "Oloy Filiali",
            address = "Amir Temur ko'chasi",
            openingTime = "08:00",
            closingTime = "19:00",
            weekends = "Du",
            location = Location(
                lat = 41.318850,
                long = 69.283120
            )
        )
    )

    private val _uiState = MutableStateFlow(
        MainScreenContract.UiState()
    )

    override val uiState: StateFlow<MainScreenContract.UiState> = _uiState.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    init {
        loadBranches()
    }

    override fun loadBranches() {
        _uiState.update {
            it.copy(branches = branches)
        }
    }

    override fun onBranchSelected(selectedBranch: MyClusterItem) {

        if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            _uiState.update {
                it.copy(
                    selectedBranch = selectedBranch,
                    distance = "Ruxsat berilmagan"
                )
            }
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val distanceStr = if (location != null) {
                calculateDistance(
                    location.latitude, location.longitude,
                    selectedBranch.position.latitude, selectedBranch.position.longitude
                )
            } else {
                "Aniqlanmadi"
            }

            _uiState.update {
                it.copy(selectedBranch = selectedBranch, distance = distanceStr)
            }
        }
    }

    override fun onBottomSheetClosed() {
        _uiState.update {
            it.copy(selectedBranch = null)
        }
    }

    override fun getDirections(branch: MyClusterItem) {

        val lat = branch.position.latitude
        val lng = branch.position.longitude

        // Google Maps navigatsiya URL manzili
        val uri = "google.navigation:q=$lat,$lng"
        val intent = Intent(Intent.ACTION_VIEW, uri.toUri()).apply {
            setPackage("com.google.android.apps.maps")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            application.startActivity(intent)
        } catch (e: Exception) {
            val browserUri = "https://www.google.com/maps/dir/?api=1&destination=$lat,$lng"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(browserUri))
            application.startActivity(browserIntent)
        }

    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): String {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        val meters = results[0]
        return if (meters < 1000) "${meters.toInt()} m" else "%.1f km".format(meters / 1000)
    }
}