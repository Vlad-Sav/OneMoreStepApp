package kg.android.onemorestepapp.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.preference.PreferenceManager
import android.view.View
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kg.android.onemorestepapp.models.responses.TopUsersResponse
import kg.android.onemorestepapp.repository.StatisticsRepository
import kg.android.onemorestepapp.service.foregroundservice.StepCounterService
import kg.android.onemorestepapp.ui.statistics.StatisticsUiEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepCounterViewModel @Inject constructor(
    private val repository: StatisticsRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val TODAY_STEPS_COUNT = "today_steps_count"
    val stepCount = MutableLiveData<Int>()
    val level = MutableLiveData<Int>()
    val progress = MutableLiveData<Int>()
    val topUsersResponse = MutableLiveData<List<TopUsersResponse>?>()
    init {
        loadStepsCount()
        getLevel()
        getProgress()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        getTopUsers()
    }

    fun onEvent(event: StatisticsUiEvent) {
        when(event) {
            is StatisticsUiEvent.GetTopUsers -> {
                getTopUsers()
            }
        }
    }

    private fun getTopUsers() {
        viewModelScope.launch {
            //state.value = state.value?.copy(isLoading = true)
            val result = repository.getTopUsers()
            topUsersResponse.postValue(result)
            //state.value = state.value?.copy(isLoading = false)
        }
    }

    private fun getLevel() {
        viewModelScope.launch {
            //state.value = state.value?.copy(isLoading = true)
            val result = repository.getLevel()
            level.postValue(result?.value)
            //state.value = state.value?.copy(isLoading = false)
        }
    }

    private fun getProgress() {
        viewModelScope.launch {
            //state.value = state.value?.copy(isLoading = true)
            val result = repository.getLevel()
            progress.postValue(result?.value)
            //state.value = state.value?.copy(isLoading = false)
        }
    }

    private fun loadStepsCount() {
        var steps = sharedPreferences.getInt(TODAY_STEPS_COUNT, 0)
        if(steps < 0) steps = 0
        stepCount.postValue(steps)
    }

    override fun onCleared() {
        // Удаляем слушатель изменений SharedPreferences при очистке ViewModel
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onCleared()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == TODAY_STEPS_COUNT) {
            // Обновляем значение наблюдаемой переменной
            val newStepCount = sharedPreferences?.getInt(key, 0) ?: 0
            stepCount.postValue(newStepCount)
        }
    }
}