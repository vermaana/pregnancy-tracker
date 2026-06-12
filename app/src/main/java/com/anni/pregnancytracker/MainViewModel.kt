package com.anni.pregnancytracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anni.pregnancytracker.core.Constants
import com.anni.pregnancytracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {

    val isOnboardingComplete: StateFlow<Boolean?> = userRepository
        .isOnboardingComplete()
        .map { it as Boolean? }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Constants.STOP_TIMEOUT_MS),
            initialValue = null,
        )
}
