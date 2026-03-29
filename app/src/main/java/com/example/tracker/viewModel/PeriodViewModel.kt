package com.example.tracker.viewModel


import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


import com.example.tracker.data.local.entity.PeriodEntity
import com.example.tracker.data.repository.PeriodRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import androidx.compose.runtime.mutableStateOf

import kotlinx.coroutines.flow.*

class PeriodViewModel(
    private val repository: PeriodRepository
) : ViewModel() {

    // --- DATA ---
    val allPeriods = repository.allPeriods

    val currentPage = MutableStateFlow(0)
    val pageSize = 10

    val paginatedPeriods = allPeriods.map { list ->
        val sortedList = list.sortedByDescending { it.startDate }
        val startIndex = currentPage.value * pageSize
        sortedList.drop(startIndex).take(pageSize)
    }

    // --- STATES ---
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var overlappingPeriod by mutableStateOf<PeriodEntity?>(null)
        private set

    var saveSuccess by mutableStateOf(false)
        private set

    // Track editing mode
    var editingPeriod by mutableStateOf<PeriodEntity?>(null)
        private set

    // --- ACTIONS ---
    fun nextPage() {
        currentPage.value++
    }

    fun prevPage() {
        if (currentPage.value > 0) currentPage.value--
    }

    fun clearError() {
        errorMessage = null
    }

    fun startEditing(period: PeriodEntity) {
        editingPeriod = period
        errorMessage = null
        overlappingPeriod = null
    }

    fun cancelEditing() {
        editingPeriod = null
        onSelectionChanged()
    }

    fun onSelectionChanged() {
        errorMessage = null
        overlappingPeriod = null
    }

    fun resetSuccess() {
        saveSuccess = false
    }

    fun deletePeriod(period: PeriodEntity) {
        viewModelScope.launch {
            repository.delete(period)
        }
    }

    // Chart data (last 6 entries)
    val chartData = allPeriods
        .map { list ->
            list.sortedBy { it.startDate }.takeLast(6)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    // --- SAVE / UPDATE ---
    fun saveSelectedPeriod(
        startDateMillis: Long,
        endDateMillis: Long
    ) {
        viewModelScope.launch {

            val existingPeriods = allPeriods.first()

            // Check overlap (ignore current editing item)
            val conflict = existingPeriods.find { existing ->
                existing.id != (editingPeriod?.id ?: -1) &&
                        startDateMillis <= existing.endDate &&
                        endDateMillis >= existing.startDate
            }

            if (conflict != null) {
                overlappingPeriod = conflict
                errorMessage = "Dates overlap with an existing entry"
                saveSuccess = false
                return@launch
            } else {
                errorMessage = null
                overlappingPeriod = null
            }

            if (editingPeriod != null) {
                // UPDATE
                val updatedPeriod = editingPeriod!!.copy(
                    startDate = startDateMillis,
                    endDate = endDateMillis
                )
                repository.update(updatedPeriod)

            } else {
                // INSERT
                val newPeriod = PeriodEntity(
                    startDate = startDateMillis,
                    endDate = endDateMillis
                )
                repository.insert(newPeriod)
            }

            saveSuccess = true
            editingPeriod = null
        }
    }
}
class PeriodViewModelFactory(
    private val repository: PeriodRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeriodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeriodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}