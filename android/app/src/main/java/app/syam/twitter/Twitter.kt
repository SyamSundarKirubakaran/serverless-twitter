package app.syam.twitter

import android.app.Application
import com.facebook.stetho.Stetho

open class Twitter : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}