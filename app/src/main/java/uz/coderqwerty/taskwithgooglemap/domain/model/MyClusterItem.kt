package uz.coderqwerty.taskwithgooglemap.domain.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import uz.coderqwerty.taskwithgooglemap.R

data class MyClusterItem(
    private val position: LatLng,
    private val title: String,
    private val snippet: String,
    val iconResId: Int = R.drawable.charging_location_icon // Custom ikon resurs IDsi
) : ClusterItem {
    override fun getPosition(): LatLng = position
    override fun getTitle(): String = title
    override fun getSnippet(): String = snippet
    override fun getZIndex(): Float? {
        return null
    }
}