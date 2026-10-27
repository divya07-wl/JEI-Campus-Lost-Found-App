package com.jei.lostfound.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jei.lostfound.data.AppDatabase
import com.jei.lostfound.data.entity.Item
import com.jei.lostfound.data.repository.LostFoundRepository
import com.jei.lostfound.util.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemDetailViewModel(application: Application, savedStateHandle: SavedStateHandle) : AndroidViewModel(application) {
    private val itemId: Int = savedStateHandle["itemId"] ?: -1
    private val repository = LostFoundRepository(AppDatabase.getDatabase(application))
    val currentUserId = SessionManager(application).currentUserId

    val item: StateFlow<Item?> = repository.observeItem(itemId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun submitClaim(message: String, onDone: (Boolean) -> Unit) {
        viewModelScope.launch {
            onDone(repository.submitClaim(itemId, currentUserId, message.trim()))
        }
    }
}
