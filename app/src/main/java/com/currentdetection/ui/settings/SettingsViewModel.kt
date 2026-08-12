package com.currentdetection.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currentdetection.data.local.PowerEventDao
import com.currentdetection.data.local.SettingsManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsManager: SettingsManager,
    private val powerEventDao: PowerEventDao
) : ViewModel() {

    val monitoringEnabled = settingsManager.monitoringEnabledFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)
    val outageNotifications = settingsManager.outageNotificationsFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)
    val powerRestoredNotifications = settingsManager.powerRestoredNotificationsFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)
    val dailySummary = settingsManager.dailySummaryFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun toggleMonitoring(enabled: Boolean) {
        viewModelScope.launch { settingsManager.setMonitoringEnabled(enabled) }
    }

    fun toggleOutageNotifications(enabled: Boolean) {
        viewModelScope.launch { settingsManager.setOutageNotifications(enabled) }
    }

    fun togglePowerRestoredNotifications(enabled: Boolean) {
        viewModelScope.launch { settingsManager.setPowerRestoredNotifications(enabled) }
    }

    fun toggleDailySummary(enabled: Boolean) {
        viewModelScope.launch { settingsManager.setDailySummary(enabled) }
    }
}
