package com.jei.lostfound.ui.myreports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.jei.lostfound.R
import com.jei.lostfound.data.AppDatabase
import com.jei.lostfound.data.entity.Item
import com.jei.lostfound.data.repository.LostFoundRepository
import com.jei.lostfound.util.SessionManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MyReportsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LostFoundRepository(AppDatabase.getDatabase(application))
    private val userId = SessionManager(application).currentUserId
    private val returnedOnly = MutableStateFlow<Boolean?>(null)

    val reports: StateFlow<List<Item>?> =
        combine(repository.getMyReports(userId), returnedOnly) { all, returned ->
            when (returned) {
                null -> all
                true -> all.filter { it.status == Item.Status.RETURNED }
                false -> all.filter { it.status != Item.Status.RETURNED }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val messages = Channel<Int>(Channel.BUFFERED)
    val events = messages.receiveAsFlow()

    fun setReturnedFilter(returned: Boolean?) { returnedOnly.value = returned }

    fun delete(itemId: Int) {
        viewModelScope.launch {
            repository.deleteItem(itemId)
            messages.trySend(R.string.report_deleted)
        }
    }

    fun markReturned(itemId: Int) {
        viewModelScope.launch {
            repository.markReturned(itemId)
            messages.trySend(R.string.marked_returned)
        }
    }
}
