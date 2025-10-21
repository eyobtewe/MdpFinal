package com.mdp.mdpfinal

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mdp.mdpfinal.worker.JokeWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class MdpFinalApp : Application(), Configuration.Provider {

    // Inject the HiltWorkerFactory
    @Inject lateinit var workerFactory: HiltWorkerFactory

    // Provide the Hilt-enabled configuration to WorkManager by overriding the property
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun onCreate() {
        super.onCreate()
        // WorkManager is automatically initialized, just schedule the work
        scheduleJokeFetching()
    }

    private fun scheduleJokeFetching() {
        // 1. Define the work request
        val jokeWorkRequest = PeriodicWorkRequestBuilder<JokeWorker>(30, TimeUnit.MINUTES).build()

        // 2. Enqueue the work as unique periodic work
        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "joke_fetch_worker", // A unique name for this work
                        ExistingPeriodicWorkPolicy
                                .KEEP, // Keep the existing work if it's already scheduled
                        jokeWorkRequest
                )
    }
}
