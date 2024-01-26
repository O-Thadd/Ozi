package com.othadd.ozi.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import com.othadd.ozi.common.WORKER_USER_KEY
import com.othadd.ozi.domain.model.OziNotificationChannel
import com.othadd.ozi.domain.model.User
import com.othadd.ozi.domain.workers.UpdateUserStateWorker
import com.othadd.ozi.ui.model.DefaultSplashScreenSetupRunner
import com.othadd.ozi.ui.model.DialogData
import com.othadd.ozi.ui.model.SplashScreenSetupRunner
import com.othadd.ozi.ui.theme.OziComposeTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "oziStore")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val mainActivityViewModel: MainActivityViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_LONG)
                    .show()
            } else {
                mainActivityViewModel.postAlertDialog(
                    "Notification permission denied",
                    "Notifications will not be sent when app is in the background."
                )
            }
        }

    private var notificationPermissionAlreadyRequestedFromHomeScreen = false

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SplashRunnerProvider {
        fun getRunner(): SplashScreenSetupRunner
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU) // for notification permission request
    override fun onCreate(savedInstanceState: Bundle?) {
        val provider = EntryPointAccessors.fromApplication(this, SplashRunnerProvider::class.java)
        val splashRunner = provider.getRunner()
        splashRunner(this)

        super.onCreate(savedInstanceState)
        setContent {
            val uiState by mainActivityViewModel.uiState.collectAsStateWithLifecycle(initialValue = null)
            OziComposeTheme(
                darkTheme = uiState?.appState?.darkTheme ?: false
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OziApp(
                        mainActivityViewModel = mainActivityViewModel,
                        updateCurrentDestination = {
                            mainActivityViewModel.updateCurrentDestination(it)
                        },
                        switchTheme = { mainActivityViewModel.changeAppTheme() },
                        sortOutNotificationPermission = { sortOutNotificationPermissionFromHome() },
                        runOnUiThread = myRunOnUiThread,
                        exitApp = { finish() },
                        setUiReady = { mainActivityViewModel.setUiReady() }
                    )
                }
            }
        }

        lifecycleScope.launch {
            mainActivityViewModel.toastFlow.collect { toast ->
                toast?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                }
            }
        }

        setUpNotificationChannels()
        mainActivityViewModel.incrementAppStartCount()
        sortOutNotificationPermission()
    }

    override fun onResume() {
        super.onResume()
        mainActivityViewModel.updateForegroundState(true)
        mainActivityViewModel.clearNotifications()
    }

    override fun onPause() {
        super.onPause()
        mainActivityViewModel.updateForegroundState(false)
    }

    override fun onStart() {
        lifecycleScope.launch {
            super.onStart()
            val user = mainActivityViewModel.getThisUser()
            user ?: return@launch

            val newStateUser = user.copy(online = true)
            scheduleUserStateUpdate(newStateUser)
        }
    }

    override fun onStop() {
        lifecycleScope.launch {
            super.onStop()
            val user = mainActivityViewModel.getThisUser()
            user ?: return@launch

            val newStateUser = user.copy(online = false)
            scheduleUserStateUpdate(newStateUser)
        }
    }

    private fun scheduleUserStateUpdate(newStateUser: User) {
        val workManager = WorkManager.getInstance(this@MainActivity)
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest = OneTimeWorkRequestBuilder<UpdateUserStateWorker>()
            .setInputData(workDataOf(WORKER_USER_KEY to Gson().toJson(newStateUser)))
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)
    }

    private fun setUpNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val chatChannel = createChannel(
                "Chat",
                "Notifications for chat messages",
                OziNotificationChannel.CHAT.id,
                NotificationManagerCompat.IMPORTANCE_DEFAULT
            )
            val gameChannel = createChannel(
                "Game",
                "Notifications related to gaming",
                OziNotificationChannel.GAME.id,
                NotificationManagerCompat.IMPORTANCE_DEFAULT
            )
            val quietChannel = createChannel(
                "Silent",
                "For sending notifications silently",
                OziNotificationChannel.SILENT.id,
                NotificationManagerCompat.IMPORTANCE_LOW
            )

            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(
                listOf(
                    chatChannel,
                    gameChannel,
                    quietChannel
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(
        name: String,
        description: String,
        channelId: String,
        importance: Int
    ): NotificationChannel {
        return NotificationChannel(channelId, name, importance).also {
            it.description = description
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sortOutNotificationPermission() {
        lifecycleScope.launch {
            val appStarts = mainActivityViewModel.uiState.first().appState.appStartsCount
            val signedIn = mainActivityViewModel.uiState.first().appState.signedIn

            if (appStarts < 3 && signedIn) {
                when {
                    ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED -> {  }

                    ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivity, Manifest.permission.POST_NOTIFICATIONS
                    ) -> {

                        val title = "Ozi needs notification permission"
                        val body =
                            "So as to be able to notify you of important updates as they happen, such as new messages or game requests." +
                                    if (appStarts == 2) "\n\nThis permission will not be sought again." +
                                            " If not granted now, granting later will have to be done from the device app settings"
                                    else " "
                        val button1Action = {
                            mainActivityViewModel.postDialog(null)
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        val button2Action = {
                            mainActivityViewModel.postAlertDialog(
                                "Notification permission denied",
                                "Notifications will not be sent when app is in the background."
                            )
                        }
                        val dialog = DialogData(
                            title,
                            body,
                            Pair("Okay", button1Action),
                            Pair("Cancel", button2Action)
                        )
                        mainActivityViewModel.postDialog(dialog)
                    }

                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun sortOutNotificationPermissionFromHome() {
        lifecycleScope.launch {
            if (mainActivityViewModel.uiState.first().appState.appStartsCount == 0 &&
                !notificationPermissionAlreadyRequestedFromHomeScreen
            ) {
                notificationPermissionAlreadyRequestedFromHomeScreen = true
                sortOutNotificationPermission()
            }
        }
    }

    private val myRunOnUiThread = { blockToRun: () -> Unit ->
        runOnUiThread { blockToRun.invoke() }
    }
}
