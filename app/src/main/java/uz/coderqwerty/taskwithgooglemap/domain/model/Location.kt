package uz.coderqwerty.taskwithgooglemap.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val lat: Double,
    val long: Double
) : Parcelable