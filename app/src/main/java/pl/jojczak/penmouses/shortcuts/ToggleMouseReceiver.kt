package pl.jojczak.penmouses.shortcuts

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.accessibility.AccessibilityManager
import pl.jojczak.penmouses.service.AppToServiceEvent
import pl.jojczak.penmouses.service.MouseService // Import MouseService

// Define a unique action string for your custom broadcast
const val ACTION_TOGGLE_MOUSE_SERVICE = "pl.jojczak.penmouses.action.TOGGLE_MOUSE_SERVICE"

class ToggleMouseReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Ensure this broadcast is for our intended action
        if (intent.action == ACTION_TOGGLE_MOUSE_SERVICE) {
            // Get the current status of the MouseService
            val isMouseServiceActive = isAccessibilityServiceEnabled(
                context, // Use the context provided to onReceive
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
        }
    }

    /**
     * Helper function to check if a specific Accessibility Service is enabled.
     * This function is identical to the one in the previous activity, but needs to be here.
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