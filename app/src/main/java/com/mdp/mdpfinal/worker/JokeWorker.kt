package com.mdp.mdpfinal.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mdp.mdpfinal.data.local.datastore.UserPreferencesRepository
import com.mdp.mdpfinal.data.remote.api.JokeApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class JokeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    // Hilt can inject dependencies you've already defined in your modules
    private val jokeApiService: JokeApiService,
    private val userPreferencesRepository: UserPreferencesRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // 1. Fetch the joke from the remote API
            val joke = jokeApiService.getRandomJoke()

            // 2. Save the joke locally using the repository
            userPreferencesRepository.saveJoke(joke.setup, joke.punchline)

            // 3. Indicate that the work succeeded
            Result.success()
        } catch (e: Exception) {
            // If anything goes wrong (e.g., no network), indicate failure
            Result.failure()
        }
    }
}