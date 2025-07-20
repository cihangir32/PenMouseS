package pl.jojczak.penmouses.shortcuts // MAKE SURE THIS IS 'shortcuts' (plural)

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings // Import for launching settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AppCompatActivity
import pl.jojczak.penmouses.service.MouseService // Import MouseService

// ACTION_TOGGLE_MOUSE_SERVICE is defined in ToggleMouseReceiver.kt and is a const val.
// It will be accessible here due to being in the same package.

class StartMouseShortcutActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "StartToggleAct"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: StartMouseShortcutActivity launched.")

        // 1. Check if the Accessibility Service is enabled in system settings
        val isServiceEnabledInSettings = isAccessibilityServiceEnabled(
            this,
            MouseService::class.java.name
        )
        Log.d(TAG, "MouseService enabled in settings: $isServiceEnabledInSettings")

        if (!isServiceEnabledInSettings) {
            // 2. If the service is NOT enabled in settings, open Accessibility Settings
            Log.d(TAG, "Service not enabled in settings. Opening Accessibility Settings.")
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            // Use NEW_TASK flag as we're starting an activity from a non-activity context (implicitly)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            // Optionally, you might want to show a Toast message here to guide the user
            // Toast.makeText(this, "Please enable PenMouseS Accessibility Service", Toast.LENGTH_LONG).show()
        } else {
            // 3. If the service IS enabled in settings, proceed with the toggle logic via broadcast
            Log.d(TAG, "Service enabled in settings. Sending toggle broadcast.")
            val toggleIntent = Intent(ACTION_TOGGLE_MOUSE_SERVICE).apply {
                setPackage(this@StartMouseShortcutActivity.packageName)
            }
            sendBroadcast(toggleIntent)
            Log.d(TAG, "Toggle broadcast sent.")
        }

        // Finish this activity immediately, regardless of what happened
        finish()
    }

    /**
     * Helper function to check if a specific Accessibility Service is enabled by the user in settings.
     */
    private fun isAccessibilityServiceEnabled(context: Context, serviceClassName: String): Boolean {
        val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

        for (service in enabledServices) {
            if (service.resolveInfo.serviceInfo.name == serviceClassName) {
                return true
            }
        }
        return false
    }
}