package uz.coderqwerty.taskwithgooglemap.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Branch(
    val name: String,
    val address: String,
    val openingTime: String,
    val closingTime: String,
    val weekends: String,
    val location: Location
) : Parcelable
