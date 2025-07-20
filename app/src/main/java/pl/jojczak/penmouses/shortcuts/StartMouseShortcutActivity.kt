package pl.jojczak.penmouses.shortcut

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.jojczak.penmouses.service.AppToServiceEvent // Make sure this import is here

class StartMouseShortcutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This line sends the signal to start the mouse service
        AppToServiceEvent.event.tryEmit(AppToServiceEvent.Event.Start)

        // This makes the activity close immediately after sending the signal
        finish()
    }
}
