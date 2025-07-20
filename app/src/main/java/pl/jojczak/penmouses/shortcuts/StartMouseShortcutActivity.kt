package pl.jojczak.penmouses.shortcuts

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.os.Bundle
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AppCompatActivity
import pl.jojczak.penmouses.service.AppToServiceEvent // Already imported
import pl.jojczak.penmouses.service.MouseService // Import MouseService to get its class name

class StartMouseShortcutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the current status of the MouseService
        val isMouseServiceActive = isAccessibilityServiceEnabled(
            this,
            MouseService::class.java.name // Get the fully qualified name of the service
        )

        // Toggle the service based on its current state
        if (isMouseServiceActive) {
            // If it's active, send the STOP event
            AppToServiceEvent.event.tryEmit(AppToServiceEvent.Event.Stop)
        } else {
            // If it's not active, send the START event
            AppToServiceEvent.event.tryEmit(AppToServiceEvent.Event.Start)
        }

        // Finish the activity immediately so it doesn't show a blank screen
        finish()
    }

    /**
     * Helper function to check if a specific Accessibility Service is enabled.
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