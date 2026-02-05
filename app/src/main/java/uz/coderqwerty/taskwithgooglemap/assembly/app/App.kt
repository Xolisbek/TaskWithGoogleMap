package uz.coderqwerty.taskwithgooglemap.assembly.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import uz.coderqwerty.taskwithgooglemap.BuildConfig

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        // Timber’ni sozlash
        if (BuildConfig.DEBUG) {
            // Debug rejimida loglarni chop etish uchun
            Timber.plant(Timber.DebugTree())
        }
    }
}