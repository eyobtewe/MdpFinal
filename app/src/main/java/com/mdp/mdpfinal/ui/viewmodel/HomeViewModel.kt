package com.mdp.mdpfinal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdp.mdpfinal.data.local.datastore.UserPreferencesRepository
import com.mdp.mdpfinal.data.remote.api.JokeApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// Define a simple data class for a category
data class InventoryCategory(
    val name: String
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val jokeApiService: JokeApiService
) : ViewModel() {

    private val _categories = MutableStateFlow<List<InventoryCategory>>(emptyList())
    val categories = _categories.asStateFlow()

    val joke: StateFlow<String> = userPreferencesRepository.savedJoke
        .map { (setup, punchline) -> "$setup\n$punchline" } // Combine setup and punchline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Loading joke..."
        )


    init {
        loadCategories()
        fetchJokeImmediately()
    }

    private fun loadCategories() {
        // In a real app, this might come from a repository or database.
        // For this project, a static list is sufficient.
        _categories.value = listOf(
            InventoryCategory("ELECTRONICS"),
            InventoryCategory("CLOTHING"),
            InventoryCategory("BOOKS")
        )
    }

    private fun fetchJokeImmediately() {
        viewModelScope.launch {
            try {
                // Fetch the joke directly from the API
                val joke = jokeApiService.getRandomJoke()
                // Save it to preferences
                userPreferencesRepository.saveJoke(joke.setup, joke.punchline)
            } catch (e: Exception) {
                // Handle error silently for now
            }
        }
    }
}
