package com.imtmobileapps.cryptocompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority.VERBOSE



@HiltAndroidApp
class CryptoComposeApplication: Application()  {
    // Log all priorities in debug builds, no-op in release builds.
    //AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = VERBOSE)

    override fun onCreate() {
        super.onCreate()

        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = VERBOSE)
        // USAGE
        /*class MouseController {

            fun play() {
                val state = "CHEEZBURGER"
                logcat { "I CAN HAZ $state?" }
                // logcat output: D/MouseController: I CAN HAZ CHEEZBURGER?

                logcat(INFO) { "DID U ASK 4 MOAR INFO?" }
                // logcat output: I/MouseController: DID U ASK 4 MOAR INFO?

                logcat { exception.asLog() }
                // logcat output: D/MouseController: java.lang.RuntimeException: FYLEZ KERUPTED
                //                        at sample.MouseController.play(MouseController.kt:22)
                //                        ...

                logcat("Lolcat") { "OH HI" }
                // logcat output: D/Lolcat: OH HI
            }
        }*/
    }
}